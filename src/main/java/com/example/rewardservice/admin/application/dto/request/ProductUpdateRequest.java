package com.example.rewardservice.admin.application.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ProductUpdateRequest {
    private String category;
    private String companyEmail; // User ID
    private String productName;
    private long price;
    private int stock;
    private MultipartFile productImage;
    private String description;
    private String state;

}
