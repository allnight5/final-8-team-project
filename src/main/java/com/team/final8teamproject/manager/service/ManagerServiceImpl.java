package com.team.final8teamproject.manager.service;

import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.base.repository.BaseRepository;
import com.team.final8teamproject.manager.dto.ManagerResponseDto;
import com.team.final8teamproject.manager.dto.ManagerSignupRequestDto;
import com.team.final8teamproject.security.jwt.JwtUtil;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.dto.*;
import com.team.final8teamproject.manager.entity.Manager;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.manager.repository.ManagerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final BaseRepository baseRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    //1. 회원가입
    @Transactional
    public MessageResponseDto signUp(@Valid ManagerSignupRequestDto managerSignupRequestDto) {

        String username = managerSignupRequestDto.getUsername();
        String password = passwordEncoder.encode(managerSignupRequestDto.getPassword());
        String nickName = managerSignupRequestDto.getNickName();
        String phoneNumber = managerSignupRequestDto.getPhoneNumber();
        String email = managerSignupRequestDto.getEmail();
        Long experience = managerSignupRequestDto.getExperience();

        Optional<BaseEntity> findUserName = baseRepository.findByUsername(username);
        if (findUserName.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_USERNAME);
        }

        Optional<BaseEntity> findNickName = baseRepository.findByNickName(nickName);
        if (findNickName.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_NICKNAME);
        }

        Optional<BaseEntity> findEmail = baseRepository.findByEmail(email);
        if (findEmail.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_EMAIL);
        }

        Optional<Manager> findPhoneNumber = managerRepository.findByPhoneNumber(phoneNumber);
        if (findPhoneNumber.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_PHONENUMBER);
        }

        UserRoleEnum userRoleEnum = UserRoleEnum.WAIT;
        Manager manager = Manager.builder()
                .username(username).password(password)
                .nickName(nickName).phoneNumber(phoneNumber)
                .email(email).experience(experience)
                .role(userRoleEnum)
                .build();
        manager.changeApplyStatus(true);
        managerRepository.save(manager);
        return new MessageResponseDto("회원가입 신청 완료");
    }

    //2. 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto requestDto) {

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        BaseEntity user = baseRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ExceptionStatus.WRONG_USERNAME)
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ExceptionStatus.WRONG_USERNAME);
        }
        LoginResponseDto loginResponseDto = jwtUtil.createUserToken(user.getUsername(), user.getRole());

//        if(redisUtil.hasKey("RT:" +user.getUsername())){
//            throw new SecurityException("이미 접속중인 사용자 입니다.");
//        }
//        redisUtil.setRefreshToken(user.getUsername(), loginResponseDto.getRefreshToken(), loginResponseDto.getRefreshTokenExpirationTime());

        return loginResponseDto;
    }


    //3. 로그아웃
    @Transactional
    public String logout(String accessToken, String username) {


        // 레디스에 accessToken 사용못하도록 등록
        Long expiration = jwtUtil.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "logout", expiration);

        // 레디스에서 리프레쉬 토큰 삭제
        if (redisUtil.hasKey("RT:" + username)) {
            redisUtil.deleteRefreshToken("RT:" + username);
        } else {
            throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
        }
        return "로그아웃 완료";
    }

    //4. 매니저 가입대기자 조회
    @Override
    @Transactional
    public List<ManagerResponseDto> getStandByList(UserRoleEnum userRoleEnum) {

        List<BaseEntity> list = baseRepository.findByRole(UserRoleEnum.WAIT);
        List<ManagerResponseDto> managerResponseDtoList = new ArrayList<>();

        for (BaseEntity baseEntity : list) {
            managerResponseDtoList.add(new ManagerResponseDto((Manager) baseEntity));
        }
        return managerResponseDtoList;
    }

    //5. 매니저 가입 승인
    @Override
    @Transactional
    public void allowStandBy(Long id) {
        Manager manager = managerRepository.findById(id).orElseThrow();
        BaseEntity base = baseRepository.findById(id).orElseThrow();
        if (manager.isApplyManager() == true) {
            base.approvalManager(UserRoleEnum.MANAGER);
            manager.changeRole(UserRoleEnum.MANAGER);
            manager.changeApplyStatus(false);
        } else {
            throw new IllegalArgumentException("신청되지 않은 사용자입니다.");
        }
    }

    //6. 매니저 가입 거부
    @Override
    @Transactional
    public void refuseStandBy(Long id) {
        Manager manager = managerRepository.findById(id).orElseThrow();
        if (manager.isApplyManager() == true) {
            managerRepository.delete(manager);
        } else {
            throw new IllegalArgumentException("신청되지 않은 사용자입니다.");
        }
    }

}
