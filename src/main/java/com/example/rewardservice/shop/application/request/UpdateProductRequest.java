package com.example.rewardservice.shop.application.request;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
public class UpdateProductRequest {
    private String category;
    private String companyEmail;
    private String productName;
    private long price;
    private List<MultipartFile> productImages;
    private String description;
}
