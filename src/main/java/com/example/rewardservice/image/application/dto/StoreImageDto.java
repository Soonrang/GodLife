package com.example.rewardservice.image.application.dto;

import com.example.rewardservice.image.domain.ProfileImage;
import lombok.Getter;

@Getter
public class StoreImageDto {
    //사진의 경우 업로드 이름과 저장이름 두가지가 저장되는게 좋습니다.
    //현재 프로필 이미지는 저장이름만 저장하고 있습니다.

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
