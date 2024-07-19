package com.example.rewardservice.shop.application.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class UpdateProductRequest {
    private String category;
    private String companyEmail;
    private String productName;
    private long price;
    private List<MultipartFile> productImages;
    private String description;
}
