package com.example.rewardservice.Image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProfileImage {
    public static final String DEFAULT_PROFILE_IMAGE_ID = "1";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Image image;

    public ProfileImage(final String uploadName, final String storeName){
        this.image = new Image(uploadName, storeName);
    }

}
