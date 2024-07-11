package com.example.rewardservice.Image.domain;

import com.example.rewardservice.Image.dto.StoreImageDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StoreImageProcessor {

    StoreImageDto storeImageFile(MultipartFile file);
    List<StoreImageDto> storeImageFiles(List<MultipartFile> files);
}
