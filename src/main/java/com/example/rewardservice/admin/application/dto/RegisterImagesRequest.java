package com.example.rewardservice.admin.application.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class RegisterImagesRequest {
    MultipartFile file;
}
