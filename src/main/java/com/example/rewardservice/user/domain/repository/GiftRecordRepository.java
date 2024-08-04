package com.example.rewardservice.user.domain.repository;

import com.example.rewardservice.user.domain.GiftRecord;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GiftRecordRepository extends JpaRepository<GiftRecord, UUID> {
    List<GiftRecord> findBySenderOrReceiver(User sender, User receiver);

}
