package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.image.application.dto.StoreImageDto;
import com.example.rewardservice.image.application.service.ImageFileService;
import com.example.rewardservice.shop.application.request.RegisterProductRequest;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageFileService imageFileService;
    private final UserRepository userRepository;


    public Product createProduct(RegisterProductRequest registerProductRequest) {
        User company = userRepository.findByEmail(registerProductRequest.getCompanyEmail());



        List<StoreImageDto>
        StoreImageDto storeImageDto = imageFileService.storeImageFile(registerProductRequest.getProductImages())
    }



}
