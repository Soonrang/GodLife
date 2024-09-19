package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.UserChallenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeUserRepository extends JpaRepository<UserChallenge, UUID> {

    List<UserChallenge> findALLByUserEmail(String email);

    UserChallenge findAllByChallengeIdAndUserEmail(UUID challengeId, String email);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.email = :email AND uc.challenge.id = :challengeId AND uc.challenge.isDeleted = false ORDER BY uc.createdAt DESC")
    Optional<UserChallenge> findLatestByUserEmailAndChallengeId(String email, UUID challengeId);

    @Query("SELECT uc FROM UserChallenge uc JOIN uc.challenge c WHERE uc.user.email = :email " +
            "AND uc.isJoined = true " +                      // 공백 추가
            "AND uc.challenge.isDeleted = false " +           // 공백 추가
            "AND (CASE " +
            "         WHEN CURRENT_DATE < c.challengePeriod.startDate THEN '진행전' " +
            "         WHEN CURRENT_DATE > c.challengePeriod.endDate THEN '종료' " +
            "         ELSE '진행중' " +
            "     END) = :state")
    Page<UserChallenge> findJoinedChallengesByUserEmail(@Param("email") String email,
                                                        @Param("state") String state,
                                                        Pageable pageable);

    List<UserChallenge> findByChallengeId(UUID challengeId);
}

