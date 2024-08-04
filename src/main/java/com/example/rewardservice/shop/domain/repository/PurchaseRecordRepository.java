package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {
    List<PurchaseRecord> findByUser(User user);

}
