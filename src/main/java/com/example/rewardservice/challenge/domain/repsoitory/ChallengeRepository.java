package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface ChallengeRepository extends JpaRepository<Challenge, UUID> {
    Page<Challenge> findByCategory(String category, Pageable pageable);


    @Query("SELECT c FROM Challenge c " +
            "WHERE c.isDeleted = false " +
            "AND (:category IS NULL OR c.category = :category) " +
            "AND (:state IS NULL OR c.state = :state) " +
            "ORDER BY " +
            "(CASE " +
            "    WHEN c.state = '진행전' THEN 1 " +
            "    WHEN c.state = '진행중' THEN 2 " +
            "    WHEN c.state = '종료' THEN 4 " +
            "    ELSE 3 " +
            "END), " +
            "(CASE WHEN c.isLimited = true THEN 1 ELSE 0 END) ASC, " +
            "c.challengePeriod.startDate ASC")
    Page<Challenge> findByCategoryAndState(
            @Param("category") String category,
            @Param("state") String state,
            Pageable pageable);



    @Query("SELECT c FROM Challenge c " +
            "WHERE c.isDeleted = false " +
            "ORDER BY " +
            "(CASE " +
            "    WHEN c.state = '진행전' THEN 1 " +  // 진행전 챌린지 우선
            "    WHEN c.state = '진행중' THEN 2 " +  // 그다음 진행중 챌린지
            "    WHEN c.state = '종료' THEN 4 " +    // 종료된 챌린지 마지막
            "    ELSE 3 " +                          // 기타 상태 (예: 모집마감)
            "END), " +
            "(CASE WHEN c.isLimited = true THEN 1 ELSE 0 END) ASC, " +  // 모집마감인 챌린지 처리
            "c.challengePeriod.startDate ASC")  // 시작 날짜 순으로 정렬
    Page<Challenge> findChallenge(Pageable pageable);

    @Query("select c from Challenge c where c.challengePeriod.startDate <= :today and c.challengePeriod.endDate >= :today and c.state <> '종료' and c.state <> '모집마감'")
    List<Challenge> findChallengesForOngoing(@Param("today") LocalDate today);

    @Query("SELECT c FROM Challenge c WHERE c.challengePeriod.startDate < :today AND c.state <> '종료' AND c.state <> '모집마감'")
    List<Challenge> findChallengesForUpcoming(@Param("today") LocalDate today);

    @Query("SELECT c FROM Challenge c WHERE c.challengePeriod.endDate < :today AND c.state <> '종료'")
    List<Challenge> findChallengesToExpire(@Param("today") LocalDate today);
}

