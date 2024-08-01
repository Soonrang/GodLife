package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.image.application.service.ImageService;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.point.application.dto.UsePointRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ImageService imageService;

    public ProductInfoResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        byte[] productImageData = getProductImageData(product);
        return ProductInfoResponse.from(product,productImageData);
    }

    public List<ProductInfoResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    byte[] productImageData = getProductImageData(product);
                    return ProductInfoResponse.from(product, productImageData);
                })
                .collect(Collectors.toList());
    }

    public Product getProductByCategory(String category) {
        return productRepository.findByCategory(category)
                .orElseThrow(()-> new IllegalArgumentException("카테고리가 없습니다."));
    }


    private byte[] getProductImageData(Product product) {
        Optional<ProductImage> firstImageOptional = product.getProductImages().stream().findFirst();
        if (firstImageOptional.isPresent()) {
            String imageUrl = firstImageOptional.get().getImageUrl();
            try {
                return imageService.getProfileImage(imageUrl);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
