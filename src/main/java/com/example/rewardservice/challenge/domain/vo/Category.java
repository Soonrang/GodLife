package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Column(name = "category")
    private String value;

    public Category(String value) {
         validate(value);
         this.value = value;
    }

    private void validate(final String value) {
        if(value == null || value.isEmpty()) {
            throw new IllegalArgumentException("값은 비어있을 수 없습니다.");
        }
    }
}
