package com.example.rewardservice.challenge.domain;

import com.example.rewardservice.challenge.domain.vo.*;
import com.example.rewardservice.user.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "challenge")
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    @Embedded
    private Title title;

    @Embedded
    private Category category;

    @Embedded
    private Span span;

    @Embedded
    private ParticipantsLimit participantsLimit;

    @Embedded
    private Images images;

    @Embedded
    private Description description;

    @Embedded
    private AuthMethod authMethod;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition = "BINARY(16)", insertable = true, updatable = true)
    private User user;

    public Challenge(final User user,
                     final Title title,
                     final Category category,
                     final Span span,
                     final ParticipantsLimit participantsLimit,
                     final Images images,
                     final Description description,
                     final AuthMethod authMethod) {
        this.user = user;
        this.title = title;
        this.category = category;
        this.span = span;
        this.participantsLimit = participantsLimit;
        this.images = images;
        this.description = description;
        this.authMethod = authMethod;
    }

    public void update(final Title title,
                           final Category category,
                           final Span span,
                           final ParticipantsLimit participantsLimit,
                           final Images images,
                           final Description description,
                           final AuthMethod authMethod) {
        this.title = title;
        this.category = category;
        this.span = span;
        this.participantsLimit = participantsLimit;
        this.images = images;
        this.description = description;
        this.authMethod = authMethod;
    }
}
