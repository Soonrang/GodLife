package com.example.rewardservice.user.domain;

import com.example.rewardservice.challenge.domain.Challenge;
import com.example.rewardservice.challenge.domain.ChallengeHistory;
import com.example.rewardservice.challenge.domain.PostChallenge;
import com.example.rewardservice.challenge.domain.UserChallenge;
import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.point.domain.Point;
import com.example.rewardservice.shop.domain.Product;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

import static com.example.rewardservice.user.domain.MemberState.ACTIVE;
import static jakarta.persistence.EnumType.STRING;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name="user_email")
    private String email;

    @Column(name = "user_password")
    private String password;

    @Column(name = "user_name")
    private String name;

    @Column(name = "user_Nick_Name")
    private String nickname;

    @Column(name = "user_total_point")
    private long totalPoint;

    @Column(name = "profile_image_url")
    private String profileImage;

    @Enumerated(value = STRING)
    private MemberState memberState;

    @Column(name = "user_social")
    private boolean userSocial;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Point> points;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Challenge> challenges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserChallenge> userChallenges;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ChallengeHistory> challengeHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<PostChallenge> postChallenges;

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private MemberRole memberRole;

    public User(final String email, final String userPassword,
                final String userName, final long totalPoint, final String profileImage, final String nickname ) {
        this.email = email;
        this.password = userPassword;
        this.name = userName;
        this.totalPoint = totalPoint;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.memberState = ACTIVE;
        this.userSocial = false;
    }

    public void updateUserInfo(String NewNickname, String newFile){
        this.nickname = NewNickname;
        this.profileImage = newFile;
    }

    public void changeMemberState(MemberState memberState){
        this.memberState =memberState;
    }

    public void changePassword(String upw) {
        this.password = upw;
    }

    public void updateProfileImage(String newProfileImage) {
        this.profileImage = newProfileImage;
    }

    public void earnPoints(long points) {
        this.totalPoint += points;
    }
    public void usedPoints(long points) { this.totalPoint -= points; }

    public void validateUsePoints(long points) {
        if(this.totalPoint < points) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        this.totalPoint -= points;
    }

}
