package com.example.rewardservice.point.application;

import com.example.rewardservice.donation.application.dto.DonatePointRequest;
import com.example.rewardservice.event.application.request.AddPointRequest;
import com.example.rewardservice.point.domain.PointRepository;
import com.example.rewardservice.shop.application.request.UsePointRequest;
import com.example.rewardservice.user.application.request.GiftPointRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointRepository pointRepository;
    private final UserRepository userRepository;


    private static final String POINT_TYPE_EARNED = "적립";
    private static final String POINT_TYPE_USED = "사용";
    private static final String POINT_TYPE_GIFT = "선물";
    private static final String POINT_TYPE_DONATION = "기부";

    @Transactional
    public void addEarnedPoint(AddPointRequest addPointRequest) {
        User user = findByUserEmail(addPointRequest.getUserEmail());

        Point point = Point.builder()
                .user(user)
                .amount(addPointRequest.getPoints())
                .description(addPointRequest.getDescription())
                .type(POINT_TYPE_EARNED)
                .activityId(addPointRequest.getActivityId())
                .build();

        pointRepository.save(point);
        user.earnPoints(addPointRequest.getPoints());
        userRepository.save(user);
    }

    @Transactional
    public void usePoints(UsePointRequest usePointRequest) {
        User user = findByUserEmail(usePointRequest.getUserEmail());

        Point point = Point.builder()
                .user(user)
                .amount(-usePointRequest.getPoints())
                .description(usePointRequest.getDescription())
                .type(POINT_TYPE_USED)
                .activityId(usePointRequest.getActivityId())
                .build();

        pointRepository.save(point);
        user.validateUsePoints(usePointRequest.getPoints());
        userRepository.save(user);
    }

    @Transactional
    public void giftPoints(GiftPointRequest giftPointRequest, UUID giftRecordId) {
        User sender = findByUserEmail(giftPointRequest.getSenderEmail());
        User receiver = findByUserEmail(giftPointRequest.getReceiverEmail());

        Point senderPoint = Point.builder()
                .user(sender)
                .amount(-giftPointRequest.getPoints())
                .description(receiver.getNickname() + "에게 보낸 포인트")
                .type(POINT_TYPE_GIFT)
                .activityId(giftRecordId)
                .build();

        Point receiverPoint = Point.builder()
                .user(receiver)
                .amount(giftPointRequest.getPoints())
                .description(sender.getNickname() + "에게 받은 포인트")
                .type(POINT_TYPE_GIFT)
                .activityId(giftRecordId)
                .build();

        pointRepository.save(senderPoint);
        pointRepository.save(receiverPoint);
        sender.validateUsePoints(giftPointRequest.getPoints());
        receiver.earnPoints(giftPointRequest.getPoints());
        userRepository.save(sender);
        userRepository.save(receiver);
    }

    @Transactional
    public void donatePoints(DonatePointRequest donatePointRequest) {
        User user = findByUserEmail(donatePointRequest.getUserEmail());

        Point point = Point.builder()
                .user(user)
                .amount(-donatePointRequest.getPoints())
                .description(donatePointRequest.getDescription())
                .type(POINT_TYPE_DONATION)
                .activityId(donatePointRequest.getActivityId())
                .build();

        pointRepository.save(point);
        user.validateUsePoints(donatePointRequest.getPoints());
        userRepository.save(user);
    }

    private User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }


}

