package com.example.demo.mfa.service;

import com.example.demo.mfa.exception.OtpNotProveException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
    UserDetails loadUserByUsername(String username, String otp) throws UsernameNotFoundException, OtpNotProveException;
}
