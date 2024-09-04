package com.example.rewardservice.user.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@Getter
public class Name {
    public final int MAX_LENGTH = 20;

    @Column(name = "user_name", nullable = false, length = MAX_LENGTH)
    private String value;

    public Name(final String value) {
        validateNull(value);
        final String trimmedValue = value.trim();
        validate(trimmedValue);
        this.value = trimmedValue;
    }

    private void validateNull(final String value) {
        if(Objects.isNull(value)) {
            throw new IllegalArgumentException("멤버 이름은 null이 불가합니다.");
        }
    }

    private void validate(final String value) {
        if(value.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("글자수를 초과했습니다.");
        }

        if(value.isBlank()) {
            throw new IllegalArgumentException("멤버 이름은 비어있을 수 없습니다.");
        }
    }

    public Name change(final String name) {
        return new Name(value);
    }
}
