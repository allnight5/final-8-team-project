package com.team.final8teamproject.owner.service;

import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.base.repository.BaseRepository;
import com.team.final8teamproject.business.dto.BusinessRequestDto;
import com.team.final8teamproject.owner.dto.OwnerSignupRequestDto;
import com.team.final8teamproject.owner.entity.Owner;
import com.team.final8teamproject.security.jwt.JwtUtil;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.dto.*;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.owner.repository.OwnerRepository;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OwnerService {
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final OwnerRepository ownerRepository;

    private final BaseRepository baseRepository;
    public MessageResponseDto signUp(OwnerSignupRequestDto OwnerSignupRequestDto) {
        String username = OwnerSignupRequestDto.getUsername();
        String password = passwordEncoder.encode(OwnerSignupRequestDto.getPassword());
        String nickName = OwnerSignupRequestDto.getNickName();
        String email = OwnerSignupRequestDto.getEmail();
        String phoneNumber =OwnerSignupRequestDto.getPhoneNumber();
        String storeName = OwnerSignupRequestDto.getStoreName();
        String ownerNumber = OwnerSignupRequestDto.getB_no();
        String start_dt = OwnerSignupRequestDto.getStart_dt();
        String ownerName = OwnerSignupRequestDto.getP_nm();

        Optional<Owner> findUserName = ownerRepository.findByUsername(username);
        if (findUserName.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_USERNAME);
        }

        Optional<Owner> findNickName = ownerRepository.findByNickName(nickName);
        if (findNickName.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_NICKNAME);
        }

        Optional<Owner> findEmail = ownerRepository.findByEmail(email);
        if (findEmail.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_EMAIL);
        }

        Optional<Owner> findPhoneNumber = ownerRepository.findByPhoneNumber(phoneNumber);
        if (findPhoneNumber.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_PHONENUMBER);
        }

        UserRoleEnum role = UserRoleEnum.OWNER;
        Owner owner = Owner.builder()
                .nickName(nickName).email(email)
                .phoneNumber(phoneNumber).password(password)
                .username(username).role(role).storeName(storeName)
                .ownerName(ownerName).ownerNumber(ownerNumber).start_dt(start_dt)
                .experience(0L)
                .build();
        ownerRepository.save(owner);
        return new MessageResponseDto("회원가입 성공");
    }

    //2.로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        BaseEntity base = baseRepository.findByUsername(username).orElseThrow(
                () -> new SecurityException("사용자를 찾을수 없습니다.")
        );
        if (!passwordEncoder.matches(password, base.getPassword())){
            throw new SecurityException("사용자를 찾을수 없습니다.");
        }
        LoginResponseDto loginResponseDto =jwtUtil.createUserToken(base.getUsername(), base.getRole());
        SetRedisRefreshToken refreshToken = new SetRedisRefreshToken(loginResponseDto.getRefreshToken(), base.getUsername(), base.getRole());
        redisUtil.setRefreshToken(loginResponseDto.getAccessToken(), refreshToken, loginResponseDto.getRefreshTokenExpirationTime());
        return loginResponseDto;
//        redisUtil.setRefreshToken(base.getUsername(), loginResponseDto.getRefreshToken(), loginResponseDto.getRefreshTokenExpirationTime());

    }

    //3. 로그아웃
    public String logout(String accessToken, String username) {

        // refreshToken 테이블의 refreshToken 삭제
        redisUtil.deleteRefreshToken(username);
//        refreshTokenRepository.deleteRefreshTokenByEmail(users.getEmail());

        // 레디스에 accessToken 사용못하도록 등록
        redisUtil.setBlackList(accessToken, "accessToken", 5L);

        return "로그아웃 완료";
    }

    public Owner getOwnerById(Long id) {
        Optional<Owner> optionalOwner = ownerRepository.findById(id);
        if (optionalOwner.isEmpty()){
            throw new NoSuchElementException("오너가 존재하지 않습니다.");
        }
        return optionalOwner.get();
    }
}
