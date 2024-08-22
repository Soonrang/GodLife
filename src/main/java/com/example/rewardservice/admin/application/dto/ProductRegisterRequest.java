package com.example.rewardservice.admin.application.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class ProductRegisterRequest {
    private String category;
    private String productCompany;
    private String productName;
    private long price;
    private int stock;
    private MultipartFile productImage;
    private String description;
}

