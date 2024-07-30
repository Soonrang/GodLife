package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.image.application.dto.ImageDto;
import lombok.Getter;

@Getter
public class ProductImageDto {

    private String uploadName;
    private String storeName;

    public ProductImageDto(final String uploadName, final String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }

    public static ProductImageDto fromImageDto(ImageDto ImageDto) {
        return new ProductImageDto(ImageDto.getUploadName(), ImageDto.getStoreName());
    }

}
