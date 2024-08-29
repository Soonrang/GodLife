package com.example.rewardservice.shop.application.response;

import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class PurchaseRecordResponse {
    private UUID purchaseId;       // 주문 번호
    private String email;          // 유저 이메일
    private List<String> productNames; // 구입한 상품 이름 리스트
    private long pointsUsed;       // 사용 포인트(총 금액)
    private int quantity;          // 주문 개수
    private long totalPoint;       // 유저 현재 포인트

    public static PurchaseRecordResponse from(PurchaseRecord purchaseRecord, List<ProductInfoResponse> productInfoResponses, String userEmail, long userCurrentPoints) {
        List<String> productNames = productInfoResponses.stream()
                .map(ProductInfoResponse::getProductName)
                .collect(Collectors.toList());

        return new PurchaseRecordResponse(
                purchaseRecord.getId(),
                userEmail,
                productNames,
                purchaseRecord.getPoints(),
                purchaseRecord.getProductQuantity(),
                userCurrentPoints
        );
    }

    // Method to handle a list of PurchaseRecords
    public static List<PurchaseRecordResponse> from(List<PurchaseRecord> purchaseRecords, String userEmail, long userCurrentPoints) {
        return purchaseRecords.stream()
                .map(purchaseRecord -> from(
                        purchaseRecord,
                        List.of(ProductInfoResponse.from(purchaseRecord.getProduct())),
                        userEmail,
                        userCurrentPoints
                ))
                .collect(Collectors.toList());
    }
}