package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.ChallengeHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface ChallengeHistoryRepository extends JpaRepository<ChallengeHistory, UUID> {

    @Query("SELECT ch FROM ChallengeHistory ch WHERE ch.user.email = :email ORDER BY ch.createAt DESC")
    Page<ChallengeHistory> findChallengeHistoriesByUserEmail(@Param("email") String email, Pageable pageable);


    @Query("SELECT SUM(ch.point) FROM ChallengeHistory ch WHERE ch.user.email = :email and ch.status = '성공' and ch.challenge.isDeleted = false")
    long findTotalPrizeByUser(@Param("email") String email);
}
