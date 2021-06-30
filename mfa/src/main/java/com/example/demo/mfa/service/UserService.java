package com.example.demo.mfa.service;

import com.example.demo.mfa.data.entities.UserEntity;

public interface UserService {

    UserEntity getUser(String username);

    UserEntity getUser(UserEntity userEntity);
}
