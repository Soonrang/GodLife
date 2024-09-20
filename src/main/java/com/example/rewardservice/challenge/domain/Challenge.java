package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.challenge.domain.vo.ChallengeImages;
import com.example.rewardservice.challenge.domain.vo.ChallengePeriod;
import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "participants_limit", nullable = false)
    private long participantsLimit;

    @Column(name = "participants_amount", nullable = false)
    private long participants;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "auth_method", nullable = false)
    private String authMethod;

    @Column(name = "state")
    private String state;

    private long prize;

    private boolean isDeleted;

    @Embedded
    private ChallengePeriod challengePeriod;

    @Embedded
    private ChallengeImages challengeImages;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<UserChallenge> userChallenges;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengePost> challengePosts;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL)
    private List<ChallengeHistory> challengeHistories;

    public void updateChallenge(String title, String category,
                                long participantsLimit,
                                String description,
                                String authMethod,
                                ChallengePeriod challengePeriod,
                                ChallengeImages challengeImages) {
        this.title = title;
        this.category = category;
        this.participantsLimit = participantsLimit;
        this.description = description;
        this.authMethod = authMethod;
        this.challengePeriod = challengePeriod;
        this.challengeImages = challengeImages;
    }

    public void checkStatus(LocalDate now) {
        if (now.isBefore(this.challengePeriod.getStartDate())) {
            this.state = "진행전";
        } else if (now.isAfter(this.challengePeriod.getEndDate())) {
            this.state = "종료";
        } else {
            this.state = "진행중";
        }
    }

    public void participantsAmountPlus() {
        this.participants++;
    }

    public void participantsAmountMinus() {
        this.participants--;
    }

    public void plusPrize(long point){
        this.prize += point;
    }

    public void minusPrize(long point) {
        this.prize -= point;
    }

    public void changeState() {
        this.state = "모집마감";
    }

    public void changeStateByCancel(){
        this.state = "모집중";
    }

    public void updateStatus(String state){
        this.state = state;
    }

    public void changeIsDelete(boolean isDelete) {
        this.isDeleted = isDelete;
    }

}
