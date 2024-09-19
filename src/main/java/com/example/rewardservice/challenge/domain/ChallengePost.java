package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChallengePost {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    private Challenge challenge;

    @ManyToOne
    private UserChallenge userChallenge;

    private String image;

    private String description;

    private String status;
    private LocalDate createdAt;

    public ChallengePost(User user, Challenge challenge, UserChallenge userChallenge, String image, String description){
        this.user = user;
        this.challenge = challenge;
        this.userChallenge = userChallenge;
        this.image = image;
        this.description = description;
        this.status = "인증";
        this.createdAt = LocalDate.now();
    }


    public void changeStatus(String status) {
        this.status = status;
    }

    public void updatePost(String image, String description) {
        this.image = image;
        this.description = description;
    }
}
