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
        return new MessageResponseDto("???????????? ??????");
    }

    //2.?????????
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        BaseEntity base = baseRepository.findByUsername(username).orElseThrow(
                () -> new SecurityException("???????????? ????????? ????????????.")
        );
        if (!passwordEncoder.matches(password, base.getPassword())){
            throw new SecurityException("???????????? ????????? ????????????.");
        }
//        String refreshToken = (String)redisUtil.getRefreshToken("RT:" +base.getUsername());
//        if(!ObjectUtils.isEmpty(refreshToken)){
//            throw new IllegalArgumentException("?????? ????????? ?????? ????????????..");
//        }
        LoginResponseDto loginResponseDto =jwtUtil.createUserToken(base.getUsername(), base.getRole());

        redisUtil.setRefreshToken("RT:" +base.getUsername(), loginResponseDto.getRefreshToken(), loginResponseDto.getRefreshTokenExpirationTime());

        return loginResponseDto;
    }

    //3. ????????????
    public String logout(String accessToken, String username) {

        // refreshToken ???????????? refreshToken ??????
        redisUtil.deleteRefreshToken("RT:" + username);
//        refreshTokenRepository.deleteRefreshTokenByEmail(users.getEmail());

        // ???????????? accessToken ?????????????????? ??????
        redisUtil.setBlackList("RT:"+accessToken, "accessToken", 5L);

        return "???????????? ??????";
    }

    public Owner getOwnerById(Long id) {
        Optional<Owner> optionalOwner = ownerRepository.findById(id);
        if (optionalOwner.isEmpty()){
            throw new NoSuchElementException("????????? ???????????? ????????????.");
        }
        return optionalOwner.get();
    }
}
