package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.image.application.service.ImageFileService;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.UsePointRequest;
import com.example.rewardservice.point.domain.UsedPoint;
import com.example.rewardservice.shop.application.ProductImageDto;
import com.example.rewardservice.shop.application.request.RegisterProductRequest;
import com.example.rewardservice.shop.application.request.UpdateProductRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageFileService imageFileService;
    private final UserRepository userRepository;
    private final PointService pointService;


    public ProductInfoResponse createProduct(User company, RegisterProductRequest registerProductRequest) {
        List<ProductImage> productImages = imageFileService.storeImageFiles(registerProductRequest.getProductImages()).stream()
                .map(ProductImageDto::fromStoreImageDto)
                .map(dto -> new ProductImage(UUID.randomUUID(), dto.getStoreName(), null))
                .collect(Collectors.toList());

        Product product = new Product(
                UUID.randomUUID(),
                registerProductRequest.getCategory(),
                company,
                company.getName(),
                registerProductRequest.getProductName(),
                registerProductRequest.getPrice(),
                registerProductRequest.getStock(),
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

        User company = userRepository.findByEmail(updateProductRequest.getCompanyEmail())
                .orElseThrow(() -> new IllegalArgumentException("기업ID가 없습니다."));

        List<ProductImage> newProductImages = imageFileService.storeImageFiles(updateProductRequest.getProductImages()).stream()
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

    @Transactional
    public void purchaseProduct(String email, UUID productId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new IllegalArgumentException("회원 ID가 없습니다."));

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new IllegalArgumentException("상품 ID가 없습니다."));

        product.reduceStock(1);
        productRepository.save(product);

        UsePointRequest usePointRequest = UsePointRequest.builder()
                .userEmail(email)
                .productId(productId)
                .pointChange(product.getPrice())
                .description("상품 구매")
                .build();

        pointService.usedPoints(usePointRequest);
    }



}
