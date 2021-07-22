package com.example.oauth2.authorizeserver.services;

import com.example.oauth2.authorizeserver.models.entities.UserEntity;

public interface UserService {

    public UserEntity getUser(UserEntity userEntity);
}
