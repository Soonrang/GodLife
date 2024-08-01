package com.example.rewardservice.common;

import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRecordRepository;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.event.domain.Event;
import com.example.rewardservice.event.domain.repository.EventParticipationRepository;
import com.example.rewardservice.event.domain.repository.EventRepository;
import com.example.rewardservice.shop.domain.Product;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.domain.User;
import com.example.rewardservice.user.domain.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Component
public class ValidateService {

    private EventRepository eventRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private DonationRepository donationRepository;
    private DonationRecordRepository donationRecordRepository;
    private EventParticipationRepository eventParticipationRepository;

    public User findByUserEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("해당 이메일의 유저가 없습니다: " + email));
    }

    public Event findByEventId(UUID eventId) {
        if (eventId == null) {
            return null;
        }
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 이벤트가 없습니다: " + eventId));
    }

    public Product findByProductId(UUID productId) {
        if (productId == null) {
            return null;
        }
        return productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 상품이 없습니다: " + productId));
    }

    public Donation findByDonationId(UUID donationId) {
        return donationRepository.findById(donationId)
                .orElseThrow(() -> new RuntimeException("해당 ID의 기부 정보가 없습니다: " + donationId));
    }


}
