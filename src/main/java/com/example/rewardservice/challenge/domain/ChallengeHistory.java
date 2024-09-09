package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ChallengeHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private UUID challengeId;

    private String status;

    private String reason;

    public ChallengeHistory(User user, UUID challengeId, String status, String reason) {
        this.user = user;
        this.challengeId = challengeId;
        this.status = status;
        this.reason = reason;
    }

}
