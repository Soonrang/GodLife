package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.shop.application.request.PurchaseRequest;
import com.example.rewardservice.shop.application.request.UsePointRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.response.ProductPagingResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.shop.domain.repository.PurchaseRecordRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    private final static String CATEGORY_ALL = "전체";
    private final static String PRICE_LOW = "priceAsc";
    private final static String PRICE_HIGH = "priceDesc";


    public ProductInfoResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return ProductInfoResponse.from(product);
    }

    public List<ProductInfoResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(ProductInfoResponse::from)
                .collect(Collectors.toList());
    }

    public Page<ProductInfoResponse> getByProductByCategory(String category, int page, int size, String sort) {
        //기본값은 가격 낮은순, 높은경우에만 정렬을 바꿔줌
        Sort sortOption = Sort.by("price").ascending();

        if (PRICE_HIGH.equals(sort)) {
            sortOption = Sort.by("price").descending();
        }

        Pageable pageable = PageRequest.of(page, size, sortOption);
        Page<Product> products;

        if (CATEGORY_ALL.equals(category)) {
            products = productRepository.findAll(pageable);
        } else {
            products = productRepository.findByCategory(category, pageable);
        }

        List<ProductInfoResponse> productInfo = products.stream()
                .map(ProductInfoResponse::from)
                .collect(Collectors.toList());

        return new PageImpl<>(productInfo, pageable, products.getTotalElements());
    }



//    private byte[] getProductImageData(Product product) {
//        Optional<ProductImage> firstImageOptional = product.getProductImages().stream().findFirst();
//        if (firstImageOptional.isPresent()) {
//            String imageUrl = firstImageOptional.get().getImageUrl();
//            try {
//                return imageService.getProfileImage(imageUrl);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return null;
//    }



}
