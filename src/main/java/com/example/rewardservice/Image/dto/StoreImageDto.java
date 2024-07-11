package com.example.rewardservice.Image.dto;

import com.example.rewardservice.Image.domain.ProfileImage;

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
