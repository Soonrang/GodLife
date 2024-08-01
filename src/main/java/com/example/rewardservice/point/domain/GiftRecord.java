package com.example.rewardservice.point.domain;

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
public class GiftRecord {
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
    private long pointsGifted;

    public GiftRecord(User sender, User receiver, long points) {
        this.sender = sender;
        this.receiver = receiver;
        this.pointsGifted = points;
    }
}
