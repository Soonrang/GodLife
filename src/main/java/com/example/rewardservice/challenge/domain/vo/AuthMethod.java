package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class AuthMethod {

    @Column(name = "auth_method", nullable = false)
    private String value;

    public AuthMethod(String value) {
        validate(value);
        this.value = value;
    }

    private static void validate(final String value) {
        if(Objects.isNull(value)) {
            throw new IllegalArgumentException("인증방식은 빈 값일 수 없습니다.");
        }
    }

}
