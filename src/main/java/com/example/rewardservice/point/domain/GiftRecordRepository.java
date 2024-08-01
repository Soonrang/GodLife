package com.example.rewardservice.point.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface GiftRecordRepository extends JpaRepository<GiftRecord, UUID> {
}
