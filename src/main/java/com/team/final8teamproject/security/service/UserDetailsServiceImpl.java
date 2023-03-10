package com.team.final8teamproject.security.service;

import com.team.final8teamproject.base.entity.BaseEntity;
import com.team.final8teamproject.base.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final BaseRepository baseRepository;

//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
//        return new UserDetailsImpl(user, user.getUsername());
//    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BaseEntity base = baseRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
        return new UserDetailsImpl(base, base.getUsername());
    }

    public BaseEntity loadUserByUsernameUseRefreshToken(String username) {
        return baseRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found "));
    }
}