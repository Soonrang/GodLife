package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "participants_limit", nullable = false)
    private long participantsLimit;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "auth_method", nullable = false)
    private String authMethod;

    @Column(name = "main_image", nullable = true)
    private String mainImage;

    @Column(name = "success_image", nullable = true)
    private String successImage;

    @Column(name = "fail_image", nullable = true)
    private String failImage;

    @Column(name = "status")
    private String status;

    public void updateChallenge(String title, String category, LocalDate startDate, LocalDate endDate,
                                long participantsLimit, String description, String authMethod,
                                String mainImageUrl, String successImageUrl, String failImageUrl, String status) {
        this.title = title;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.participantsLimit = participantsLimit;
        this.description = description;
        this.authMethod = authMethod;
        this.mainImage = mainImageUrl;
        this.successImage = successImageUrl;
        this.failImage = failImageUrl;
        this.status = status;
    }

    public void checkStatus(LocalDateTime now) {
        LocalDateTime startDateTime = LocalDateTime.of(this.startDate, this.startTime);
        LocalDateTime endDateTime = LocalDateTime.of(this.endDate, this.endTime);

        if (now.isBefore(startDateTime)) {
            this.status = "PENDING";
        } else if (now.isAfter(endDateTime)) {
            this.status = "COMPLETED";
        } else {
            this.status = "ONGOING";
        }
    }
}
