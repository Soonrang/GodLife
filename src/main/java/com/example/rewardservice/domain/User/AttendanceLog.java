package com.example.rewardservice.domain.User;

import com.example.rewardservice.domain.Event.Event;
import com.example.rewardservice.domain.Point.Point;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog  {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "event_id")
    private UUID eventId;

    @Column(name = "attendance_date")
    private LocalDateTime attendanceDate;

    @PrePersist
    protected void onCreate() {
        attendanceDate = LocalDateTime.now();
    }

}
