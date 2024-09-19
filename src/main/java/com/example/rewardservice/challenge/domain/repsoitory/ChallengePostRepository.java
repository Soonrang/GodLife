package com.example.rewardservice.challenge.domain.repsoitory;

import com.example.rewardservice.challenge.application.response.UserChallengeStateResponse;
import com.example.rewardservice.challenge.domain.ChallengePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface ChallengePostRepository extends JpaRepository<ChallengePost, UUID> {

    @Query("SELECT cp FROM ChallengePost cp WHERE cp.userChallenge.challenge.id = :challengeId AND cp.userChallenge.challenge.user.email = :email")
    Page<ChallengePost> findAllByChallengeId(@Param("challengeId") UUID challengeId,
                                             @Param("email") String email,
                                             Pageable pageable);

//    @Query("SELECT cp FROM ChallengePost cp WHERE cp.userChallenge.challenge.id = :challengeId AND cp.userChallenge.challenge.user.email = :email")
//    Page<ChallengePost> findAllUserChallengeStateResponseByChallengeId(@Param("challengeId") UUID challengeId,
//                                                                                    @Param("email") String email,
//
//
//                                                                                    Pageable pageable);

    @Query("SELECT cp FROM ChallengePost cp WHERE cp.userChallenge.challenge.id = :challengeId")
    Page<ChallengePost> findAllUserChallengeStateResponseByChallengeId(@Param("challengeId") UUID challengeId, Pageable pageable);


    @Query("SELECT cp FROM ChallengePost cp WHERE cp.user.email = :email AND cp.challenge.id = :challengeId AND cp.createdAt = :today")
    Optional<ChallengePost> findTodayPostByUserAndChallenge(@Param("email") String email,
                                                            @Param("challengeId") UUID challengeId,
                                                            @Param("today") LocalDate today);

    @Query("SELECT cp FROM ChallengePost cp WHERE cp.userChallenge.id = :userChallengeId")
    Page<ChallengePost> findByUserChallengeId(@Param("userChallengeId") UUID userChallengeId, Pageable pageable);



}

