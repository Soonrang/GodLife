package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.request.ProductRegisterRequest;
import com.example.rewardservice.admin.application.dto.request.ProductUpdateRequest;
import com.example.rewardservice.image.s3.S3ImageService;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final ProductRepository productRepository;
    private final S3ImageService s3ImageService;

    public ProductInfoResponse createEasyProduct(ProductRegisterRequest productRegisterRequest) {
        // 단일 이미지를 리스트로 처리하는 메서드 호출
        List<ProductImage> productImages = handleImageUpload(productRegisterRequest.getProductImage());

        Product product = Product.builder()
                .id(UUID.randomUUID())
                .productName(productRegisterRequest.getProductName())
                .productCompany(productRegisterRequest.getProductCompany())
                .price(productRegisterRequest.getPrice())
                .category(productRegisterRequest.getCategory())
                .stock(productRegisterRequest.getStock())
                .productImages(productImages)
                .description(productRegisterRequest.getDescription())
                .status(productRegisterRequest.getState())
                .build();

        productImages.forEach(image -> image.setProduct(product));

        Product savedProduct = productRepository.save(product);
        return ProductInfoResponse.from(savedProduct);
    }

    private List<ProductImage> uploadAndSaveImages(MultipartFile image) {
        return handleImageUpload(image);  // 단일 이미지 처리 메서드를 호출
    }

    public ProductInfoResponse updateProduct(UUID productId, ProductUpdateRequest productUpdateRequest) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        List<ProductImage> productImages = handleImageUpload(productUpdateRequest.getProductImage()); // 단일 이미지 처리

        product.updateProduct(
                productUpdateRequest.getCategory(),
                productUpdateRequest.getProductName(),
                productUpdateRequest.getPrice(),
                productUpdateRequest.getStock(),
                productImages,
                productUpdateRequest.getDescription(),
                productUpdateRequest.getState()
        );

        productImages.forEach(image -> image.setProduct(product));

        Product updatedProduct = productRepository.save(product);
        return ProductInfoResponse.from(updatedProduct);
    }


    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        product.getProductImages().forEach(image -> s3ImageService.deleteImageFromS3(image.getImageUrl()));

        productRepository.delete(product);
    }


    private List<ProductImage> handleImageUpload(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("이미지가 없습니다.");
        }

        String imageUrl = s3ImageService.upload(image);
        ProductImage productImage = new ProductImage(UUID.randomUUID(), imageUrl);

        return List.of(productImage); // 단일 이미지를 리스트로 반환
    }

}