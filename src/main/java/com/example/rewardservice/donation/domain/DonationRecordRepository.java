package com.example.rewardservice.donation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DonationRecordRepository extends JpaRepository<DonationRecord, UUID> {
}
