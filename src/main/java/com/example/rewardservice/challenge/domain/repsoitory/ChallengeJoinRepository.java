package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChallengeJoinRepository extends JpaRepository<UserChallenge, UUID> {

    List<UserChallenge> findALLByUserEmail(String email);

    UserChallenge findAllByChallengeIdAndUserEmail(UUID challengeId, String email);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.email = :email AND uc.challenge.id = :challengeId ORDER BY uc.createdAt DESC")
    Optional<UserChallenge> findLatestByUserEmailAndChallengeId(String email, UUID challengeId);

    @Query("SELECT uc FROM UserChallenge uc WHERE uc.user.email = :email AND uc.status = 'JOINED'")
    List<UserChallenge> findJoinedChallengesByUserEmail(@Param("email") String email);
}

