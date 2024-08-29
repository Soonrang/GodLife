package com.example.rewardservice.user.application.service;

import com.example.rewardservice.common.ValidateService;
import com.example.rewardservice.point.application.GiftRequest;
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
    public void giftPoints(String senderEmail, GiftRequest giftRequest) {
        User sender = findByUserEmail(senderEmail);
        User receiver = findByUserEmail(giftRequest.getRecipientId());

        if (sender.getTotalPoint() < giftRequest.getPoints()) {
            throw new RuntimeException("포인트가 충분하지 않습니다.");
        }

        // 선물 기록 저장
        GiftRecord giftRecord = new GiftRecord(sender, receiver, giftRequest.getPoints());
        giftRecordRepository.save(giftRecord);

        GiftPointRequest giftPointRequest = GiftPointRequest.builder()
                .points(giftRequest.getPoints())
                .description("포인트 선물")
                .receiverEmail(receiver.getEmail())
                .senderEmail(sender.getEmail())
                .build();


        // 포인트 선물 기록 저장
        pointService.giftPoints(giftPointRequest, giftRecord.getId());
    }

    public boolean giftUserCheck(String receiverEmail) {
        boolean status = false;
        if(userRepository.findByEmail(receiverEmail).isPresent()){
            status = true;
        }
        return status;
    }

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

}
