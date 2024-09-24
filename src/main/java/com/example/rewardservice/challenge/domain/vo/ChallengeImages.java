package com.example.rewardservice.challenge.domain.vo;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class ChallengeImages {

    @Column(name = "main_image", nullable = true)
    private String mainImage;

    @Column(name = "success_image", nullable = true)
    private String successImage;

    @Column(name = "fail_image", nullable = true)
    private String failImage;

    public ChallengeImages() {

    }

    public ChallengeImages(String mainImage, String successImage, String failImage) {
        this.mainImage = mainImage;
        this.successImage = successImage;
        this.failImage = failImage;
    }

    public void changeMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public void changeSuccessImage(String successImage) {
        this.successImage = successImage;
    }

    public void changeFailImage(String failImage) {
        this.failImage = failImage;
    }
}
