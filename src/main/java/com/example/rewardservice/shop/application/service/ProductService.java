package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.shop.application.request.MultiPurchaseRequest;
import com.example.rewardservice.shop.application.request.PurchaseRequest;
import com.example.rewardservice.shop.application.request.UsePointRequest;
import com.example.rewardservice.shop.application.response.MultiPurchaseResponse;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.response.ProductPagingResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.ProductImage;
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


    //public record PurchaseRequest(String name, long price, long quantity, long totalPrice)

    @Transactional
    public void purchaseProduct(String email, PurchaseRequest purchaseRequest) {
        User user = findByUserEmail(email);

        for (PurchaseRequest.ProductItem item : purchaseRequest.products()) {
            Product product = findByProductId(item.id());

            // 재고 확인 (필요 시 사용)
            validateQuantity(product.getStock(), item.quantity());

            // 상품 총 금액이 프론트에서 던져주는 총 금액과 일치하는지 확인
            long totalPrice = validateTotalPrice(product.getPrice(), item.quantity(), item.price() * item.quantity());

            // 구매 기록 저장
            PurchaseRecord purchaseRecord = new PurchaseRecord(user, product, totalPrice, item.quantity());
            purchaseRecordRepository.save(purchaseRecord);

            // 상품 재고 차감
            product.minusPurchaseQuantity(item.quantity());

            // 포인트 사용 기록 저장
            UsePointRequest usePointRequest = UsePointRequest.builder()
                    .userEmail(email)
                    .points(totalPrice)
                    .description("상품 구매: " + product.getProductName() + "*" + item.quantity() + "개")
                    .activityId(purchaseRecord.getId())
                    .build();

            pointService.usePoints(usePointRequest);
        }
    }

    public ProductInfoResponse getProductById(UUID productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        return ProductInfoResponse.from(product);
    }

    public List<ProductInfoResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(product -> {
                    return ProductInfoResponse.from(product);
                })
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

    private long validateTotalPrice(long price, int quantity, long totalPrice) {
        long calcTotal = price * quantity;

        if(totalPrice != calcTotal) {
            throw new IllegalArgumentException("총 구매금액이 일치하지 않습니다.");
        }

        return calcTotal;
    }

    private void validateQuantity(int remainQuantity, int purchaseQuantity) {
        if(remainQuantity - purchaseQuantity < 0) {
            throw new IllegalArgumentException("상품 재고가 부족합니다.");
        }
    }


    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    public Product findByProductId(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 없습니다: " + productId));
    }

}
