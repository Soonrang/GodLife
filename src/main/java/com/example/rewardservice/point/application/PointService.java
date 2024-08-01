package com.example.rewardservice.point.application;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.donation.application.DonationService;
import com.example.rewardservice.donation.application.dto.DonationRequest;
import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRecord;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.event.domain.EventParticipation;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.point.application.dto.ViewPointRequest;
import com.example.rewardservice.point.domain.GiftRecord;
import com.example.rewardservice.point.domain.GiftRecordRepository;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.point.application.dto.AddPointRequest;
import com.example.rewardservice.point.application.dto.GiftPointRequest;
import com.example.rewardservice.point.application.dto.UsePointRequest;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.shop.domain.repository.PurchaseRecordRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ProductRepository productRepository;
    private final EventParticipationRepository eventParticipationRepository;
    private final PurchaseRecordRepository purchaseRecordRepository;
    private final DonationRecordRepository donationRecordRepository;
    private final GiftRecordRepository giftRecordRepository;
    private final ValidateService validateService;
    private final DonationService donationService;

    private static final String POINT_TYPE_EARNED = "적립";
    private static final String POINT_TYPE_USED = "사용";
    private static final String POINT_TYPE_GIFT = "선물";
    private static final String POINT_TYPE_VIEW = "뷰어";
    private static final String POINT_TYPE_DONATION = "기부";


    @Transactional
    public void addEarnedPoint(AddPointRequest addPointRequest) {
        User user = findByUserEmail(addPointRequest.getUserEmail());

        Point point = Point.builder()
                .user(user)
                .amount(addPointRequest.getPoint())
                .description(addPointRequest.getDescription())
                .type(POINT_TYPE_EARNED)
                .activityId(addPointRequest.getActivityId())
                .build();

        pointRepository.save(point);
        user.earnPoints(addPointRequest.getPoint());
        userRepository.save(user);
    }

    @Transactional
    public void usedPoints(UsePointRequest usePointRequest) {
        User user = findByUserEmail(usePointRequest.getUserEmail());
        Product product = findByProductId(usePointRequest.getProductId());

        PurchaseRecord purchaseRecord = new PurchaseRecord(user, product, usePointRequest.getPoint());
        purchaseRecordRepository.save(purchaseRecord);

        Point usedPoint = new Point(
                product,
                user,
                usePointRequest.getPoint(),
                usePointRequest.getDescription(),
                POINT_TYPE_USED
        );

        pointRepository.save(usedPoint);
        user.validateUsePoints(usePointRequest.getPoint());
        userRepository.save(user);
    }

    @Transactional
    public void giftPoints(GiftPointRequest giftPointRequest) {
        User sender = findByUserEmail(giftPointRequest.getSenderEmail());
        User receiver = findByUserEmail(giftPointRequest.getReceiverEmail());

        if (sender.getTotalPoint() < giftPointRequest.getPoints()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        GiftRecord giftRecord = new GiftRecord(sender, receiver, giftPointRequest.getPoints());
        giftRecordRepository.save(giftRecord);

        Point senderPointRecord = Point.builder()
                .user(sender)
                .amount(-giftPointRequest.getPoints())
                .description(giftPointRequest.getReceiverEmail() + "에게 포인트를 선물하였습니다.")
                .type(POINT_TYPE_GIFT)
                .activityId(giftRecord.getId())
                .build();

        Point receiverPointRecord = Point.builder()
                .user(receiver)
                .amount(giftPointRequest.getPoints())
                .description(giftPointRequest.getSenderEmail() + "이 보낸 포인트 선물입니다.")
                .type(POINT_TYPE_GIFT)
                .activityId(giftRecord.getId())
                .build();

        pointRepository.save(senderPointRecord);
        pointRepository.save(receiverPointRecord);

        sender.validateUsePoints(giftPointRequest.getPoints());
        receiver.earnPoints(giftPointRequest.getPoints());

        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @Transactional
    public void donatePoints(DonationRequest donationRequest) {
        User user = findByUserEmail(donationRequest.getEmail());
        Donation donation = validateService.findByDonationId(donationRequest.getId());

        if (user.getTotalPoint() < donationRequest.getPoints()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        DonationRecord donationRecord = new DonationRecord(user, donation, donationRequest.getPoints());
        donationRecordRepository.save(donationRecord);

        Point point = Point.builder()
                .user(user)
                .amount(donationRequest.getPoints())
                .description(donationRequest.getTitle() + " 기부")
                .type(POINT_TYPE_DONATION)
                .activityId(donationRecord.getId())
                .build();

        pointRepository.save(point);
        user.validateUsePoints(donationRequest.getPoints());
        userRepository.save(user);
    }


    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    private Event findByEventId(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    private Product findByProductId(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 없습니다: " + productId));
    }

    // 포인트 선물하기
    private void deductPointsByGift(User user, long points) {
        user.validateUsePoints(points);
        userRepository.save(user);
    }

    private void addPointsByGift(User user, long points) {
        user.earnPoints(points);
        userRepository.save(user);
    }

    // 포인트 선물하기 기록
    private void recordPointTransaction(User sender, User receiver, GiftPointRequest giftPointRequest) {
        Point senderPointRecord = Point.builder()
                .user(sender)
                .amount(-giftPointRequest.getPoints())
                .description(giftPointRequest.getReceiverEmail() + "에게 포인트를 선물하였습니다.")
                .type(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(senderPointRecord);


        Point recipientPointRecord = Point.builder()
                .user(receiver)
                .amount(giftPointRequest.getPoints())
                .description(giftPointRequest.getSenderEmail() + "이 보낸 포인트 선물입니다.")
                .type(POINT_TYPE_GIFT)
                .build();
        pointRepository.save(recipientPointRecord);
    }

}
