package com.example.rewardservice.Image.application.dto;

import com.example.rewardservice.Image.domain.ProfileImage;
import lombok.Getter;

@Getter
public class StoreImageDto {

    private String uploadName;
    private String storeName;

    public StoreImageDto(final String uploadName, final String storeName) {
       this.uploadName = uploadName;
       this.storeName = storeName;
    }

    public ProfileImage toProfileImageEntity() {
        return new ProfileImage(uploadName, storeName);
    }


}
