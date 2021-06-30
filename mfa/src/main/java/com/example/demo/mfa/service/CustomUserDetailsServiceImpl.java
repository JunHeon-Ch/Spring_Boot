package com.example.demo.mfa.service;

import com.example.demo.mfa.data.dto.CustomUserDetails;
import com.example.demo.mfa.data.entities.UserEntity;
import com.example.demo.mfa.exception.OtpNotProveException;
import com.example.demo.mfa.util.OTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsServiceImpl implements CustomUserDetailsService {

    private final UserService userService;
    private final MfaService mfaService;
    private String otp;

    @Autowired
    public CustomUserDetailsServiceImpl(UserService userService, MfaService mfaService) {
        this.userService = userService;
        this.mfaService = mfaService;
    }

    @Override
    public UserDetails loadUserByUsername(String username, String otp) throws UsernameNotFoundException, OtpNotProveException {
        this.otp = otp;

        if(otp != null) {
            String secretKey = mfaService.getMfaSecretKey(username).getSecretKey();
            if(!OTPUtil.checkCode(otp, secretKey)) {
                throw new OtpNotProveException("OTP number didn't prove.");
            }
        }

        return loadUserByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.getUser(username);

        if(userEntity == null) {
            throw new UsernameNotFoundException("The user not exist.");
        }

        CustomUserDetails.CustomUserDetailsBuilder customUserDetailsBuilder = CustomUserDetails.builder();
        customUserDetailsBuilder.username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .accountNonExpired(false)
                .accountNonLocked(true)
                .credentialsNonExpired(false)
                .enabled(true);

        return customUserDetailsBuilder.build();
    }
}
