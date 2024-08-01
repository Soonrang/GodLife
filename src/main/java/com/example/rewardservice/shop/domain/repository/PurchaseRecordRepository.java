package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.PurchaseRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {
}
