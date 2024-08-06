package com.example.rewardservice.donation.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import static jakarta.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@SQLDelete(sql = "UPDATE image SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status = 'USABLE'")
public class DonationImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Donation donation;

    public DonationImage(final String name, final Donation donation) {
        this.name = name;
        this.donation =donation;
    }
}
