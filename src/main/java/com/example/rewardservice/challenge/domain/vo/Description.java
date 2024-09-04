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
public class Description {

    private static final int MAX_LENGTH = 255;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String value;


    public Description(String value) {
        if(Objects.isNull(value) || value.isBlank()) {
            this.value = null;
            return;
        }
        validate(value);
        this.value = value.trim();
    }

    private static void validate(final String value) {
        if(value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("제목 이름이 255자를 초과했습니다.");
        }
    }

    public boolean isExist() {
        return Objects.nonNull(value);
    }
}

