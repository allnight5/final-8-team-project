package com.team.final8teamproject.user.service;

import com.amazonaws.thirdparty.jackson.databind.ObjectMapper;
import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.base.repository.BaseRepository;
import com.team.final8teamproject.security.redis.RedisUtil;
import com.team.final8teamproject.share.exception.CustomException;
import com.team.final8teamproject.share.exception.ExceptionStatus;
import com.team.final8teamproject.user.dto.*;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.entity.UserRoleEnum;
import com.team.final8teamproject.user.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.team.final8teamproject.security.jwt.JwtUtil;

import java.util.*;

import static com.team.final8teamproject.share.exception.ExceptionStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final JavaMailSender mailSender;
    private final UserRepository userRepository;
    private final BaseRepository baseRepository;
    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> redisTemplate;
    //1. 회원가입
    @Transactional
    public MessageResponseDto signUp(@Valid SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = passwordEncoder.encode(signupRequestDto.getPassword());
        String nickName = signupRequestDto.getNickName();
        String email = signupRequestDto.getEmail();
        String phoneNumber = signupRequestDto.getPhoneNumber();
        Long experience = signupRequestDto.getExperience();

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

        Optional<User> findPhoneNumber = userRepository.findByPhoneNumber(phoneNumber);
        if (findPhoneNumber.isPresent()) {
            throw new CustomException(ExceptionStatus.DUPLICATED_PHONENUMBER);
        }

        UserRoleEnum role = UserRoleEnum.MEMBER;
        User user = User.builder()
                .nickName(nickName).email(email)
                .phoneNumber(phoneNumber).password(password)
                .username(username).role(role).experience(experience)
                .build();
        userRepository.save(user);
        return new MessageResponseDto("회원가입 성공");
    }

    //2. 로그인
    @Transactional
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {

        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        BaseEntity user = baseRepository.findByUsername(username).orElseThrow(
                () -> new CustomException(ExceptionStatus.WRONG_USERNAME)

        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new CustomException(ExceptionStatus.WRONG_USERNAME);
        }
        LoginResponseDto loginResponseDto = jwtUtil.createUserToken(user.getUsername(), user.getRole());
        SetRedisRefreshToken refreshToken = new SetRedisRefreshToken(loginResponseDto.getRefreshToken(), user.getUsername(), user.getRole());
        redisUtil.setRefreshToken(loginResponseDto.getAccessToken(), refreshToken, loginResponseDto.getRefreshTokenExpirationTime());
        return loginResponseDto;
    }


    //3. 로그아웃
    @Transactional
    public String logout(String accessToken, String username) {

        // 레디스에 accessToken 사용못하도록 등록
        Long expiration = jwtUtil.getExpiration(accessToken);
        redisUtil.setBlackList(accessToken, "logout", expiration);

        if (redisUtil.hasKey(username)) {
            redisUtil.deleteRefreshToken(username);
        } else {
            throw new IllegalArgumentException("이미 로그아웃한 유저입니다.");
        }
        return "로그아웃 완료";
    }

    //4. 유저 아이디 찾기
    @Transactional
    public FindByResponseDto findByUsername(String email) {
        BaseEntity user = baseRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("이메일을 다시 입력해주시기 바랍니다.")
        );
        if (user.getUsername().isEmpty()) {
            throw new NoSuchElementException("소셜 회원 가입자는 찾을수 없습니다.");
        }
        String username = user.getUsername();
        return new FindByResponseDto(username);
    }

    //5. 비밀번호찾기
    @Transactional
    public FindByResponseDto userFindPassword(FindPasswordRequestDto vo) {
        BaseEntity user = baseRepository.findByEmail(vo.getEmail()).orElseThrow(
                () -> new IllegalArgumentException("이메일을 다시 입력해주시기 바랍니다.")
        );
        // 가입된 아이디가 없으면
        if (user.isUsername(vo.getUsername())) {
            throw new IllegalArgumentException("등록되지 않은 사용자입니다.");
        }

        // 가입된 이메일이 아니면
        else if (user.isEmail(vo.getEmail())) {
            throw new IllegalArgumentException("등록되지 않은 사용자입니다.");
        } else {

            // 임시 비밀번호 생성
            StringBuilder pw = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                pw.append((char) ((Math.random() * 26) + 97));
            }
            user.changePassword(passwordEncoder.encode(pw.toString()));
            // 비밀번호 변경 메일 발송
            sendEmail(vo, pw.toString());

        }
        return new FindByResponseDto("임시 패스워드 발송 성공");
    }

    //5-1.이메일 발송
    @Async
    public void sendEmail(FindPasswordRequestDto vo, String password) {
        // Mail Server 설정
        MimeMessage mail = mailSender.createMimeMessage();
        // 받는 사람 E-Mail 주소
        String userMail = vo.getEmail();
        String htmlStr = "<h2>안녕하세요 '" + vo.getUsername() + "' 님</h2><br><br>"
                + "<p>비밀번호 찾기를 신청해주셔서 임시 비밀번호를 발급해드렸습니다.</p>"
                + "<p>임시로 발급 드린 비밀번호는 <h2 style='color : blue'>'" + password
                + "'</h2>이며 로그인 후 마이페이지에서 비밀번호를 변경해주시면 됩니다.</p><br>"
                + "<h3><a href='http://localhost:5500/index.html'>MS :p 홈페이지 접속 ^0^</a></h3><br><br>"
                + "(혹시 잘못 전달된 메일이라면 이 이메일을 무시하셔도 됩니다)";

        try {
            mail.setSubject("[MS :p] 임시 비밀번호가 발급되었습니다", "utf-8");
            mail.setText(htmlStr, "utf-8", "html");
            mail.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(userMail));
            mailSender.send(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    //6. 엑세스 토큰 재발급

    /**
     *
     * @param requestDto 만료되어 받아오는 access토큰
     * @return
     * 서버 DB조회없이 redis에서 조회해온 데이터를 이용하여 처리
     * 여기서 문제 하루동안 접속을 안하거나
     * 하루종일 있다면 한명이 최대 48개의 redis칸을 차지함
     * 그로인하여 속도 저하도 있을 수 있다.
     * 그렇게 되면 DB조회하는것과 redis만 이용 하는것 둘중 뭐가 좋은지.. 애매하다.
     */
    @Transactional
    public ResponseEntity<String> regenerateToken(
            RegenerateTokenRequestDto requestDto){
        try{
            if(!redisUtil.hasKey(requestDto.getAccessToken())){
                throw new CustomException(NOT_FOUNT_TOKEN);
            }
            Object redisToken = redisUtil.getRefreshToken(requestDto.getAccessToken());
            if(redisToken instanceof SetRedisRefreshToken){
                long REFRESH_TOKEN_EXPIRE_TIME = 24 * 60 * 60 * 1000L; //1일
                SetRedisRefreshToken refreshToken = (SetRedisRefreshToken)redisToken;
                String reToken = jwtUtil.reCreateUserToken(refreshToken.getUsername(),
                        refreshToken.getRole());
                RegenerateTokenResponseDto tokenResponseDto
                        = new RegenerateTokenResponseDto(reToken);
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JwtUtil.AUTHORIZATION_HEADER,tokenResponseDto.getAccessToken());
                redisUtil.setRefreshToken(reToken, refreshToken,REFRESH_TOKEN_EXPIRE_TIME);
                redisUtil.setBlackList(requestDto.getAccessToken(), null,REFRESH_TOKEN_EXPIRE_TIME);
                return new ResponseEntity<>("생성성공", httpHeaders, HttpStatus.OK);
            }
            throw new CustomException(NOT_FOUNT_TOKEN);
        }catch (AuthenticationException e) {
            throw new CustomException(NOT_FOUNT_TOKEN);
        }

    }
    public String getUserNickname(BaseEntity base) {
        String username = base.getUsername();
        BaseEntity user = baseRepository.findByUsername(username).orElseThrow(() -> new CustomException(ExceptionStatus.WRONG_USERNAME));
        return user.getNickName();
    }

}

