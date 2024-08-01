package com.example.rewardservice.donation.domain;

import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DonationRecordRepository extends JpaRepository<DonationRecord, UUID> {
    List<DonationRecord> findByUser(User user);

}
