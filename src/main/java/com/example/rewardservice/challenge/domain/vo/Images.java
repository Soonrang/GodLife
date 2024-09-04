package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Images {
    private String mainImage;
    private String successImage;
    private String failImage;

    public Images(String mainImage, String successImage, String failImage) {
        validate(mainImage);
        validate(successImage);
        validate(failImage);
        this.mainImage = mainImage;
        this.successImage = successImage;
        this.failImage = failImage;
    }

    private void validate(String value) {
        if (value == null || value.isEmpty()) {
            throw new IllegalArgumentException("이미지는 빈 값이 될 수 없습니다.");
        }
    }
}
