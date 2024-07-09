package com.example.rewardservice.user.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

import static jakarta.persistence.EnumType.STRING;

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

    @Column(name = "user_email")
    private String userEmail;

    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @Enumerated(value = STRING)
    private MemberState memberState;

    @Column(name = "user_social")
    private boolean userSocial;



    public User(final String userId, final String userPassword, final String userName, final long totalPoint) {
        this.userId = userId;
        this.userPassword = userPassword;
        this.userName = userName;
        this.totalPoint = totalPoint;
        this.lastUpdateDate = LocalDateTime.now();
    }


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
