package com.example.demo.mfa.repositories;

import com.example.demo.mfa.data.entities.MfaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MfaRepository extends JpaRepository<MfaEntity, Long> {

    MfaEntity findByUsername(String username);
}
