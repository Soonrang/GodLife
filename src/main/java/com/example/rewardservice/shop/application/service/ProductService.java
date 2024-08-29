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
    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PointService pointService;
    private final UserRepository userRepository;


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


    public Page<ProductPagingResponse> paging(Pageable pageable) {
        int page = pageable.getPageNumber();

        if (page < 0) {
            page = 0;
        }

        int pageLimit = 12;

        Page<Product> products = productRepository.findAll(PageRequest.of(page, pageLimit,Sort.by(Sort.Direction.DESC, "id")));

        List<ProductPagingResponse> pagingResponses = products.stream()
                .map(ProductPagingResponse::from).collect(Collectors.toList());

        return new PageImpl<>(pagingResponses, pageable, products.getTotalElements());
    }


    public Product getProductByCategory(String category) {
        return productRepository.findByCategory(category)
                .orElseThrow(()-> new IllegalArgumentException("카테고리가 없습니다."));
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
