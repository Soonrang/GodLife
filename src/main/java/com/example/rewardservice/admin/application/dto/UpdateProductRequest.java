package com.example.rewardservice.admin.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateProductRequest {
    private String category;
    private String companyEmail; // User ID
    private String productName;
    private long price;
    private int stock;
    private List<MultipartFile> productImages;
    private String description;
}
