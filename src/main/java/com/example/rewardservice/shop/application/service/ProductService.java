package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.image.application.service.ImageFileService;
import com.example.rewardservice.shop.application.ProductImageDto;
import com.example.rewardservice.shop.application.request.RegisterProductRequest;
import com.example.rewardservice.shop.application.request.UpdateProductRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageFileService imageFileService;
    private final UserRepository userRepository;


    public ProductInfoResponse createProduct(User company,RegisterProductRequest registerProductRequest) {
        List<ProductImage> productImages = imageFileService.storeImageFiles(registerProductRequest.getProductImages()).stream()
                .map(ProductImageDto::fromStoreImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), null))
                .collect(Collectors.toList());

        Product product = new Product(
                UUID.randomUUID(),
                registerProductRequest.getCategory(),
                company,
                company.getName(), // 회사 이름 저장
                registerProductRequest.getProductName(),
                registerProductRequest.getPrice(),
                productImages,
                registerProductRequest.getDescription()
        );

        productImages.forEach(productImage -> productImage.setProduct(product));

        Product savedProduct = productRepository.save(product);
        return ProductInfoResponse.from(savedProduct);
    }

    public ProductInfoResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return ProductInfoResponse.from(product);
    }

    public List<ProductInfoResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductInfoResponse::from)
                .collect(Collectors.toList());
    }

    public Product getProductByCategory(String category) {
        return productRepository.findByCategory(category)
                .orElseThrow(()-> new IllegalArgumentException("카테고리가 없습니다."));
    }

    public ProductInfoResponse updateProduct(UUID productId, UpdateProductRequest updateProductRequest){
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품아이디가 없습니다."));

        List<ProductImage> newProductImages = imageFileService.storeImageFiles(updateProductRequest.getProductImages()).stream()
                .map(ProductImageDto::fromStoreImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), product))
                .collect(Collectors.toList());

        product.updateProduct(
                updateProductRequest.getCategory(),
                updateProductRequest.getCompanyEmail(),
                updateProductRequest.getProductName(),
                updateProductRequest.getPrice(),
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
