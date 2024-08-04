package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.ProductRegisterRequest;
import com.example.rewardservice.admin.application.dto.UpdateProductRequest;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.shop.application.response.ProductEasyInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final ProductRepository productRepository;
    private final S3ImageService s3ImageService;

    public ProductEasyInfoResponse createEasyProduct(ProductRegisterRequest productRegisterRequest) {
        List<ProductImage> productImages = uploadAndSaveImages(productRegisterRequest.getProductImages());

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .productName(productRegisterRequest.getProductName())
                .price(productRegisterRequest.getPrice())
                .category(productRegisterRequest.getCategory())
                .stock(productRegisterRequest.getStock())
                .productImages(productImages)
                .description(productRegisterRequest.getDescription())
                .build();

        productImages.forEach(image -> image.setProduct(product));

        Product savedProduct = productRepository.save(product);
        return ProductEasyInfoResponse.from(savedProduct);
    }

    private List<ProductImage> uploadAndSaveImages(List<MultipartFile> images) {
        return images.stream()
                .map(image -> {
                    String imageUrl = s3ImageService.upload(image);
                    return new ProductImage(UUID.randomUUID(), imageUrl);
                })
                .collect(Collectors.toList());
    }

    public ProductEasyInfoResponse updateProduct(UUID productId, UpdateProductRequest updateProductRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        List<ProductImage> productImages = uploadAndSaveImages(updateProductRequest.getProductImages());
        product.updateProduct(
                updateProductRequest.getCategory(),
                updateProductRequest.getProductName(),
                updateProductRequest.getPrice(),
                updateProductRequest.getStock(),
                productImages,
                updateProductRequest.getDescription()
        );

        productImages.forEach(image -> image.setProduct(product));

        Product updatedProduct = productRepository.save(product);
        return ProductEasyInfoResponse.from(updatedProduct);
    }

    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        product.getProductImages().forEach(image -> s3ImageService.deleteImageFromS3(image.getImageUrl()));

        productRepository.delete(product);
    }
}