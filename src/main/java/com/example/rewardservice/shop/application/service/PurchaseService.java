package com.example.rewardservice.shop.application.service;

import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.shop.application.request.PurchaseRequest;
import com.example.rewardservice.shop.application.request.UsePointRequest;
import com.example.rewardservice.shop.application.response.ProductInfoResponse;
import com.example.rewardservice.shop.application.response.PurchaseDetailResponse;
import com.example.rewardservice.shop.application.response.PurchaseRecordResponse;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.shop.domain.repository.PurchaseRecordRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseService {
    private final ProductRepository productRepository;
    private final PurchaseRecordRepository purchaseRecordRepository;
    private final PointService pointService;
    private final UserRepository userRepository;

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

    @Transactional
    public List<PurchaseDetailResponse> viewAllPurchases(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));

        List<PurchaseRecord> purchaseRecords = purchaseRecordRepository.findByUser(user);

        return purchaseRecords.stream()
                .map(purchaseRecord -> {
                    List<ProductInfoResponse> productInfos = List.of(ProductInfoResponse.from(purchaseRecord.getProduct()));
                    return PurchaseDetailResponse.from(purchaseRecord, productInfos, user.getEmail(), user.getTotalPoint());
                })
                .collect(Collectors.toList());
    }



//    public List<PurchaseRecordResponse> viewAllPurchase(String email) {
//        User user = findByUserEmail(email);
//        List<PurchaseRecord> purchaseRecords = purchaseRecordRepository.findByUser(user);
//
//        return purchaseRecords.stream()
//                .map(purchaseRecord -> {
//                    List<ProductInfoResponse> productInfos = List.of(ProductInfoResponse.from(purchaseRecord.getProduct()));
//                    return PurchaseRecordResponse.from(
//                            purchaseRecord,
//                            productInfos,
//                            user.getEmail(),
//                            user.getTotalPoint()
//                    );
//                })
//                .collect(Collectors.toList());
//    }

    public PurchaseDetailResponse viewPurchaseDetails(UUID purchaseId) {
        PurchaseRecord purchaseRecord = purchaseRecordRepository.findById(purchaseId)
                .orElseThrow(() -> new RuntimeException("Purchase record not found: " + purchaseId));

        User user = purchaseRecord.getUser();

        List<ProductInfoResponse> productInfos = List.of(ProductInfoResponse.from(purchaseRecord.getProduct()));

        return PurchaseDetailResponse.from(purchaseRecord, productInfos, user.getEmail(), user.getTotalPoint());
    }


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
