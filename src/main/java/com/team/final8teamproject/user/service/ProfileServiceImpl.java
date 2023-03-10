package com.team.final8teamproject.user.service;

import com.team.final8teamproject.manager.entity.Manager;
import com.team.final8teamproject.manager.repository.ManagerRepository;
import com.team.final8teamproject.owner.dto.OwnerProfileModifyRequestDto;
import com.team.final8teamproject.owner.dto.OwnerProfileResponseDto;
import com.team.final8teamproject.owner.entity.Owner;
import com.team.final8teamproject.owner.repository.OwnerRepository;
import com.team.final8teamproject.user.dto.ProfileModifyRequestDto;
import com.team.final8teamproject.user.dto.ProfileResponseDto;
import com.team.final8teamproject.user.entity.KaKao;
import com.team.final8teamproject.user.entity.User;
import com.team.final8teamproject.user.repository.KakaoRepository;
import com.team.final8teamproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final UserRepository userRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final KakaoRepository kakaoRepository;

    //유저
    @Override
    @Transactional
    public void modifyProfile(ProfileModifyRequestDto profileModifyRequestDto, Long id,String imageUrl) {
        User user = getUserById(id);
        String password = passwordEncoder.encode(profileModifyRequestDto.getPassword());
        user.modifyProfile(profileModifyRequestDto.getNickname(), imageUrl);
    }

    //프로필 조회
    @Override
    @Transactional
    public ProfileResponseDto getProfile(Long id) {
        User user = getUserById(id);
        return ProfileResponseDto.of(user);
    }

    @Transactional
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("프로필을 찾을 수 없습니다");
        }
        return user.get();
    }

    //관리자
    @Override
    @Transactional
    public void modifyManagerProfile(ProfileModifyRequestDto profileModifyRequestDto, Long id,String iamgeUrl) {
        Manager manager = getManagerById(id);
        String password = passwordEncoder.encode(profileModifyRequestDto.getPassword());
        manager.modifyManagerProfile(profileModifyRequestDto.getNickname(), iamgeUrl);
    }

    //프로필 조회
    @Override
    @Transactional
    public ProfileResponseDto getManagerProfile(Long id) {
        Manager manager = getManagerById(id);
        return ProfileResponseDto.of(manager);
    }

    @Transactional
    public Manager getManagerById(Long id) {
        Optional<Manager> manager = managerRepository.findById(id);
        if (manager.isEmpty()) {
            throw new IllegalArgumentException("프로필을 찾을 수 없습니다");
        }
        return manager.get();
    }



    @Override
    @Transactional
    public ProfileResponseDto getKakaoProfile(Long id) {
        KaKao kaKao = getKakaoById(id);
        return ProfileResponseDto.of(kaKao);
    }

    @Transactional
    public KaKao getKakaoById(Long id) {
        Optional<KaKao> kaKao = kakaoRepository.findById(id);
        if (kaKao.isEmpty()) {
            throw new IllegalArgumentException("프로필을 찾을 수 없습니다");
        }
        return kaKao.get();
    }

//    ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ이전 버전ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
//    //유저
//    //1. 프로필 조회
//    @Transactional
//    public ProfileResponseDto getProfile(User user) {
//        return new ProfileResponseDto(user.getId(), user.getUsername(), user.getNickName(), user.getEmail(), user.getProfileImage(), user.getPhoneNumber());
//    }
//
//    //2. 프로필 수정
//    @Override
//    @Transactional
//    public void modifyProfile(ProfileModifyRequestDto profileModifyRequestDto, User user) {
//        user.changeProfile(profileModifyRequestDto.getNickname(), profileModifyRequestDto.getImage(), profileModifyRequestDto.getPhoneNumber());
//        userRepository.save(user);
//    }


//    //사업자
//    //1. 프로필 조회
//    @Transactional
//    public ProfileResponseDto getOwnerProfile(Owner owner) {
//        return new ProfileResponseDto(owner.getId(),owner.getUsername(), owner.getNickName(), owner.getEmail(), owner.getProfileImage(), owner.getPhoneNumber());
//    }
//
//    //2. 프로필 수정
//    @Override
//    @Transactional
//    public void modifyOwnerProfile(ProfileModifyRequestDto profileModifyRequestDto, Owner owner) {
//        owner.changeOwnerProfile(profileModifyRequestDto);
//        ownerRepository.save(owner);
//    }

//    //관리자
//    //1. 프로필 조회
//    @Transactional
//    public ProfileResponseDto getManagerProfile(Manager manager) {
//        return new ProfileResponseDto(manager.getId(),manager.getUsername(), manager.getNickName(), manager.getEmail() ,manager.getProfileImage(), manager.getPhoneNumber());
//    }
//
//    //2. 프로필 수정
//    @Override
//    @Transactional
//    public void modifyManagerProfile(ProfileModifyRequestDto profileModifyRequestDto, Manager manager) {
//        manager.changeManagerProfile(profileModifyRequestDto);
//        managerRepository.save(manager);
//    }

}
