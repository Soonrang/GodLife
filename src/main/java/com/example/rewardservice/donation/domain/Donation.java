package com.example.rewardservice.donation.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.event.domain.EventPeriod;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
public class Donation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "donation_id")
    private UUID id;

    @Column(name = "donation_title")
    private String title;

    @Column(name = "donation_current_Amount")
    private long currentAmount;

    @Column(name = "donation_target_amount")
    private long targetAmount;

    @Embedded
    private EventPeriod eventPeriod;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
    private List<DonationRecord> donationRecords;

    public void addDonation(long amount) {
        this.currentAmount += amount;
    }


}
