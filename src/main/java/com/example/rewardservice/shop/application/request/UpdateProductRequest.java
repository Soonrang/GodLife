package com.example.rewardservice.shop.application.request;

import com.example.rewardservice.user.domain.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

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
