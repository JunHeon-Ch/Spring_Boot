package com.example.demo.mfa.data.dto;

import com.example.demo.mfa.data.entities.MfaEntity;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
public class MfaProveDto implements Serializable {

    private String secretKey;
    private String type;

    public MfaProveDto(MfaEntity mfaEntity) {
        this.secretKey = mfaEntity.getSecretKey();
        this.type = mfaEntity.getType();
    }

    @Builder
    public MfaProveDto(String secretKey, String type) {
        this.secretKey = secretKey;
        this.type = type;
    }
}
