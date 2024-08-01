package com.example.rewardservice.user.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "gift_record", indexes = {
        @Index(name = "idx_sender_id", columnList = "sender_id"),
        @Index(name = "idx_receiver_id", columnList = "receiver_id")
})
public class GiftRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receiver;

    @Column(name = "points_gifted")
    private long points;

    @Column(name = "gift_message")
    private String message;

    public GiftRecord(User sender, User receiver, long points, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.points = points;
        this.message = message;
    }

    public GiftRecord(User sender, User receiver, long points) {
        this.sender = sender;
        this.receiver = receiver;
        this.points = points;
    }
}
