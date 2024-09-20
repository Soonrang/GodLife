package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ChallengeAdminRepository extends JpaRepository<Challenge, UUID> {

    @Query("SELECT DISTINCT c FROM Challenge c WHERE c.user.email = :email AND c.isDeleted = false  AND (CASE " +
            " WHEN CURRENT_DATE < c.challengePeriod.startDate THEN '진행전' " +
            " WHEN CURRENT_DATE > c.challengePeriod.endDate THEN '종료' " +
            " ELSE '진행중' " +
            " END) = :state")
    Page<Challenge> findAdminChallengesByUserEmail(@Param("email") String email,
                                                   @Param("state") String state,
                                                   Pageable pageable);

    @Query("SELECT DISTINCT c FROM Challenge c WHERE c.user.email = :email AND c.isDeleted = false ")
    Page<Challenge> findAdminChallengesByAllStates(@Param("email") String email, Pageable pageable);


}
