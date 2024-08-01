package com.example.rewardservice.point.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;
@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "point_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "point_type")
    private String type;

    @Column(name = "amount")
    private long amount;

    @Column(name = "description")
    private String description;

    @Column(name = "deleted")
    private char deleted = NOT_DELETED;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)")
    private User user;

    @Column(name = "activity_id", columnDefinition = "BINARY(16)")
    private UUID activityId;

    public static final char NOT_DELETED = 'N';
    public static final char DELETED = 'Y';

    public void delete() {
        this.deleted = DELETED;
    }
}
