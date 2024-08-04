package com.example.rewardservice.user.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.point.application.PointService;
import com.example.rewardservice.user.application.request.GiftPointRequest;
import com.example.rewardservice.user.domain.GiftRecord;
import com.example.rewardservice.user.domain.repository.GiftRecordRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class GiftPointService {

    private final GiftRecordRepository giftRecordRepository;
    private final ValidateService validateService;
    private final UserRepository userRepository;
    private final PointService pointService;

    @Transactional
    public void giftPoints(GiftPointRequest giftPointRequest) {
        User sender = validateService.findByUserEmail(giftPointRequest.getSenderEmail());
        User receiver = validateService.findByUserEmail(giftPointRequest.getReceiverEmail());

        if (sender.getTotalPoint() < giftPointRequest.getPoints()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        // 선물 기록 저장
        GiftRecord giftRecord = new GiftRecord(sender, receiver, giftPointRequest.getPoints());
        giftRecordRepository.save(giftRecord);

        // 포인트 선물 기록 저장
        pointService.giftPoints(giftPointRequest, giftRecord.getId());
    }
}
