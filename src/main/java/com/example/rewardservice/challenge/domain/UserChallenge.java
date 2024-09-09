package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.challenge.domain.vo.Progress;
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
public class UserChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private String status;

    @Embedded
    private Progress progress;

    public void changeStatus(String status) {
        this.status = status;
    }

    public UserChallenge(User user, Challenge challenge, String status) {
        this.user = user;
        this.challenge = challenge;
        this.status = status;
    }
}
