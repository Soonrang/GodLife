package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.application.dto.ProductRegisterRequest;
import com.example.rewardservice.image.application.service.ImageService;
import com.example.rewardservice.shop.application.response.ProductImageDto;
import com.example.rewardservice.shop.application.response.ProductEasyInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductEasyInfoResponse createEasyProduct(ProductRegisterRequest productRegisterRequest) {
        List<ProductImage> productImages = imageService.saveImages(productRegisterRequest.getProductImages()).stream()
                .map(ProductImageDto::fromImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), null))
                .collect(Collectors.toList());

        Product product = new Product(
                UUID.randomUUID(),
                productRegisterRequest.getProductName(),
                productRegisterRequest.getPrice(),
                productRegisterRequest.getCategory(),
                productRegisterRequest.getStock(),
                productImages
        );
        productImages.forEach(productImage -> productImage.setProduct(product));

        Product savedProduct = productRepository.save(product);
        return ProductEasyInfoResponse.from(savedProduct);
    }


   /* public ProductEasyInfoResponse updateProduct(UUID productId, UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품아이디가 없습니다."));

        User company = userRepository.findByEmail(updateProductRequest.getCompanyEmail())
                .orElseThrow(() -> new IllegalArgumentException("기업ID가 없습니다."));

        List<ProductImage> newProductImages = profileImageService.storeProfileImages(updateProductRequest.getProductImages()).stream()
                .map(ProductImageDto::fromImageDto)
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
*/

    public void deleteProduct(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(()->new IllegalArgumentException("상품 아이디를 찾을 수 없습니다."));

        productRepository.delete(product);
    }



}
