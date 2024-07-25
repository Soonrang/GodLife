package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.ProductRegisterRequest;
import com.example.rewardservice.admin.application.dto.UpdateProductRequest;
import com.example.rewardservice.image.application.service.ProfileImageService;
import com.example.rewardservice.shop.application.ProductImageDto;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final ProductRepository productRepository;
    private final ProfileImageService profileImageService;
    private final UserRepository userRepository;

    public ProductInfoResponse createProduct(User company, ProductRegisterRequest productRegisterRequest) {
        List<ProductImage> productImages = profileImageService.storeProfileImages(productRegisterRequest.getProductImages()).stream()
                .map(ProductImageDto::fromStoreImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), null))
                .collect(Collectors.toList());

        Product product = new Product(
                UUID.randomUUID(),
                productRegisterRequest.getCategory(),
                company,
                company.getName(),
                productRegisterRequest.getProductName(),
                productRegisterRequest.getPrice(),
                productRegisterRequest.getStock(),
                productImages,
                productRegisterRequest.getDescription()
        );

        productImages.forEach(productImage -> productImage.setProduct(product));

        Product savedProduct = productRepository.save(product);
        return ProductInfoResponse.from(savedProduct);
    }

    public ProductInfoResponse updateProduct(UUID productId, UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품아이디가 없습니다."));

        User company = userRepository.findByEmail(updateProductRequest.getCompanyEmail())
                .orElseThrow(() -> new IllegalArgumentException("기업ID가 없습니다."));

        List<ProductImage> newProductImages = profileImageService.storeProfileImages(updateProductRequest.getProductImages()).stream()
                .map(ProductImageDto::fromStoreImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), product))
                .collect(Collectors.toList());

        product.updateProduct(
                updateProductRequest.getCategory(),
                company,
                updateProductRequest.getProductName(),
                company.getName(),
                updateProductRequest.getPrice(),
                updateProductRequest.getStock(),
                newProductImages,
                updateProductRequest.getDescription()
        );

        Product updatedProduct = productRepository.save(product);
        return ProductInfoResponse.from(updatedProduct);
    }


    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        productRepository.delete(product);
    }



}
