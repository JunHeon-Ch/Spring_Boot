package com.example.demo.mfa.service;

import com.example.demo.mfa.data.dto.MfaDto;
import com.example.demo.mfa.data.dto.MfaInitDto;
import com.example.demo.mfa.data.dto.MfaProveDto;
import com.example.demo.mfa.data.entities.MfaEntity;
import com.example.demo.mfa.repositories.MfaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MfaServiceImpl implements MfaService {

    private final MfaRepository mfaRepository;

    @Autowired
    public MfaServiceImpl(MfaRepository mfaRepository) {
        this.mfaRepository = mfaRepository;
    }

    @Override
    public MfaDto getMfa(String username) {
        return new MfaDto(mfaRepository.findByUsername(username));
    }

    @Override
    public MfaProveDto getMfaSecretKey(String username) {
        return new MfaProveDto(mfaRepository.findByUsername(username));
    }

    // 생성
    @Override
    public MfaInitDto setMfa(MfaInitDto mfaInitDto) {
        mfaRepository.save(new MfaEntity(mfaInitDto));
        return mfaInitDto;
    }

    // 수정
    @Override
    public MfaDto setMfa(MfaDto mfaDto) {
        mfaRepository.save(new MfaEntity(mfaDto));
        return mfaDto;
    }

    // 삭제
    @Override
    public void deleteMfa(String username) {
        mfaRepository.delete(mfaRepository.findByUsername(username));
    }
}
