package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import lombok.Getter;

@Getter
public class ProductImageDto {

    private String uploadName;
    private String storeName;

    public ProductImageDto(final String uploadName, final String storeName) {
        this.uploadName = uploadName;
        this.storeName = storeName;
    }

    public static ProductImageDto fromStoreImageDto(StoreImageDto storeImageDto) {
        return new ProductImageDto(storeImageDto.getUploadName(), storeImageDto.getStoreName());
    }

}
