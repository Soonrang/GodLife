package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class Title {

    @Column(name = "title", nullable = false)
    private String value;

    public Title(String value) {
        validate(value);
    }

    private void validate(String value) {
        if (Objects.isNull(value)) {
            throw new NullPointerException("챌린지 제목이 null 일 수 없습니다.");
        }

        if (value.isBlank()) {
            throw new IllegalArgumentException("챌린지 제목이 빈칸일 수 없습니다.");
        }
    }

    public void changeTitle(String newTitle) {
        this.value = newTitle;
    }

}
