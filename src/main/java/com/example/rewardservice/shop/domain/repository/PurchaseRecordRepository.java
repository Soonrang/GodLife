package com.example.rewardservice.shop.domain.repository;

import com.example.rewardservice.shop.domain.PurchaseRecord;
import com.example.rewardservice.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface PurchaseRecordRepository extends JpaRepository<PurchaseRecord, UUID> {


    //@Query("select pr from PurchaseRecord pr order by pr.createdAt desc")
    List<PurchaseRecord> findByUser(User user);

}
