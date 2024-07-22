package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import com.example.rewardservice.shop.application.ProductImageDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class RegisterProductRequest {
    private String category;
    private String productName;
    private long price;
    private int stock;
    private List<MultipartFile> productImages;
    private String description;
}
