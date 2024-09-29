package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ChallengeAdminRepository extends JpaRepository<Challenge, UUID> {

    @Query("SELECT DISTINCT c FROM Challenge c " +
            "WHERE c.user.email = :email " +
            "AND c.isDeleted = false " +
            "AND (:state IS NULL OR c.state = :state) " +
            "ORDER BY " +
            "(CASE " +
            "   WHEN c.state = '진행전' THEN 1 " +
            "   WHEN c.state = '진행중' THEN 2 " +
            "   ELSE 3 " +
            "END), " +
            "c.challengePeriod.startDate ASC")
    Page<Challenge> findAdminChallengesByUserEmail(@Param("email") String email,
                                                   @Param("state") String state,
                                                   Pageable pageable);

    @Query("SELECT DISTINCT c FROM Challenge c WHERE c.user.email = :email AND c.isDeleted = false " +
            "ORDER BY " +
            "(CASE " +
            "   WHEN c.state = '종료' AND c.isClosed = false THEN 1 " +
            "   WHEN c.state = '진행전' THEN 2 " +
            "   WHEN c.state = '진행중' THEN 3 " +
            "   WHEN c.state = '종료' AND c.isClosed = true THEN 4 " +
            "   ELSE 5 " + 
            " END)," +
            "c.challengePeriod.startDate ASC")
    Page<Challenge> findAdminChallengesByAllStates(@Param("email") String email, Pageable pageable);



}

