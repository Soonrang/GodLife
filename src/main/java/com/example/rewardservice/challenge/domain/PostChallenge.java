package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
public class PostChallenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenge challenge;

    private String image;

    private String description;

    private String status;

    public void changeStatus(String status) {
        this.status = status;
    }

    public void updatePost(String image, String description) {
        this.image = image;
        this.description = description;
    }
}
