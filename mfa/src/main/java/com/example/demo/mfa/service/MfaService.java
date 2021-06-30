package com.example.demo.mfa.service;

import com.example.demo.mfa.data.dto.MfaDto;
import com.example.demo.mfa.data.dto.MfaInitDto;
import com.example.demo.mfa.data.dto.MfaProveDto;

public interface MfaService {

    MfaDto getMfa(String username);

    MfaProveDto getMfaSecretKey(String username);

    // 생성
    MfaInitDto setMfa(MfaInitDto mfaInitDto);

    // 수정
    MfaDto setMfa(MfaDto mfaDto);

    // 삭제
    void deleteMfa(String username);
}
