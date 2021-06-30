package com.example.demo.mfa.datas.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Getter
@ToString
@EqualsAndHashCode(callSuper = false)
@Table(name = "mfa", schema = "security")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MfaEntity implements Serializable {

    @Id
    @Column(nullable = false)
    private long id;

    @Column(length = 50, nullable = false)
    private String username;

    @Column(length = 512, name = "secret_key")
    private String secretKey;

    @Column(length = 100)
    private String type;

    @Builder
    public MfaEntity(long id, String username, String secretKey, String type) {
        this.id = id;
        this.username = username;
        this.secretKey = secretKey;
        this.type = type;
    }
}
