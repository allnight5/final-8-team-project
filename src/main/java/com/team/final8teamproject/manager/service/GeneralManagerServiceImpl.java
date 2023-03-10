package com.team.final8teamproject.manager.service;

import com.team.final8teamproject.manager.dto.ManagerLoginRequestDto;
import com.team.final8teamproject.manager.dto.ManagerMessageDto;
import com.team.final8teamproject.manager.dto.WaitMangerResponseDto;
import com.team.final8teamproject.manager.entity.GeneralManager;
import com.team.final8teamproject.manager.entity.Manager;
import com.team.final8teamproject.manager.repository.GeneralManagerRepository;
import com.team.final8teamproject.manager.repository.ManagerRepository;
import com.team.final8teamproject.security.jwt.JwtUtil;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.user.dto.LoginResponseDto;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GeneralManagerServiceImpl implements GeneralManagerService {
    private final GeneralManagerRepository generalManagerRepository;
    private final ManagerRepository managerRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    //실행시 테이블에 저장
    @Transactional
    @Override
    public void save(GeneralManager generalManager){

        Optional<GeneralManager> found = generalManagerRepository.findByUsername(generalManager.getUsername());
        if (found.isPresent()) {
            throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
        }
        generalManagerRepository.save(generalManager);
    }
    //총 관리자 로그인
    @Transactional
    @Override
    public LoginResponseDto login(ManagerLoginRequestDto requestDto){

        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        GeneralManager manager = generalManagerRepository.findByUsername(username).orElseThrow(
                () -> new SecurityException("사용자를 찾을수 없습니다.")
        );
        if (!passwordEncoder.matches(password, manager.getPassword())) {
            throw new SecurityException("사용자를 찾을수 없습니다.");
        }
        LoginResponseDto loginResponseDto =jwtUtil.createManagerToken(manager.getUsername(), manager.getRole());

        if(redisUtil.hasKey("RT:" +manager.getUsername())){
            throw new SecurityException("이미 접속중인 사용자 입니다.");
        }
//        redisUtil.setRefreshToken("RT:" +manager.getUsername(), loginResponseDto.getRefreshToken(), loginResponseDto.getRefreshTokenExpirationTime());
        return loginResponseDto;
    }
    //관리자 조회
    public List<WaitMangerResponseDto> waitManagerList(Pageable pageable) {
        Pageable page = pageableSetting(pageable.getPageNumber(), pageable.getPageSize());
        Page<Manager> approval = managerRepository.findByRole(page, UserRoleEnum.WAIT);
        return approval.stream().map(WaitMangerResponseDto::new).collect(Collectors.toList());
    }
    public Pageable pageableSetting(int page, int size){
        Sort.Direction direction = Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "id");
        return PageRequest.of(page-1, size, sort);
    }
    //관리자 승급 승인
    @Transactional
    @Override
    public ManagerMessageDto approval(String manager) {
        Manager manager1 = managerRepository.findByUsername(manager).orElseThrow(
                ()-> new IllegalArgumentException("해당 신청자를 찾을수 없습니다.")
        );
        manager1.approvalManager(UserRoleEnum.MANAGER);
        return new ManagerMessageDto(manager+"의 매니저 활동을 승인하셨습니다.");
    }


    //총 관리자 로그아웃
    @Transactional
    @Override
    public void logout(String accessToken, String general) {
        if(redisUtil.getRefreshToken(accessToken) != null){
            redisUtil.deleteRefreshToken(general);
        }
        // 레디스에 accessToken 사용못하도록 등록
        Long expiration = jwtUtil.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "logout", expiration);
    }


}
