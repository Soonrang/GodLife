package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class PurchaseDetailResponse {
    private UUID purchaseId;       // 주문 번호
    private String email;          // 유저 이메일
    private long totalPoint;
    private long pointsUsed;       // 사용 포인트
    private int itemQuantity;
    private LocalDateTime purchaseDate;
    private List<ProductInfoResponse> products;  // 구입한 상품 정보

    public static PurchaseDetailResponse from(PurchaseRecord purchaseRecord, List<ProductInfoResponse> productItems, String userEmail, long userCurrentPoints) {
        return new PurchaseDetailResponse(
                purchaseRecord.getId(),
                userEmail,
                userCurrentPoints,
                purchaseRecord.getPoints(),
                purchaseRecord.getProductQuantity(),
                purchaseRecord.getCreatedAt(),
                productItems
        );
    }

}
