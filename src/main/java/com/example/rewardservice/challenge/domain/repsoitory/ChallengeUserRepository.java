package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.domain.Challenge;
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


    // 진행중, 진행전, 종료 상태의 챌린지 모두 조회
    @Query("SELECT uc FROM UserChallenge uc JOIN uc.challenge c WHERE uc.user.email = :email " +
            "AND uc.isJoined = true " +
            "AND uc.challenge.isDeleted = false")
    Page<UserChallenge> findJoinedChallengesByAllStates(@Param("email") String email, Pageable pageable);



//    // 내가 개설한 챌린지
//    @Query("SELECT DISTINCT c FROM Challenge c JOIN c.userChallenges uc WHERE uc.user.email = :email AND c.isDeleted = false AND uc.isJoined = true AND (CASE " +
//            " WHEN CURRENT_DATE < c.challengePeriod.startDate THEN '진행전' " +
//            " WHEN CURRENT_DATE > c.challengePeriod.endDate THEN '종료' " +
//            " ELSE '진행중' " +
//            " END) = :state")
//    Page<Challenge> findAdminChallengesByUserEmail(@Param("email") String email,
//                                                   @Param("state") String state,
//                                                   Pageable pageable);
//    @Query("SELECT DISTINCT uc FROM UserChallenge uc JOIN uc.challenge c WHERE c.user.email = :adminEmail " +
//            "AND uc.isJoined = true " +
//            "AND c.isDeleted = false")
//    Page<UserChallenge> findAdminChallengesByAllStates(@Param("adminEmail") String adminEmail, Pageable pageable);

}

