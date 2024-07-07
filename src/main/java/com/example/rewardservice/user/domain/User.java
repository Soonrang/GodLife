package com.example.rewardservice.user.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_password")
    private String userPassword;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "user_total_point")
    private long totalPoint;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    public void earnPoints(long points) {
        this.totalPoint += points;
        this.lastUpdateDate = LocalDateTime.now();
    }

    public void usePoints(long points) {
        if(this.totalPoint < points) {
            throw new IllegalArgumentException("insufficient points");
        }
        this.totalPoint -= points;
        this.lastUpdateDate = LocalDateTime.now();
    }

    public void changePassword(String upw) {
        this.userPassword = upw;
    }



}
