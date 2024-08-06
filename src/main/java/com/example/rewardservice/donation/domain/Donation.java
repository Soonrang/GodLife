package com.example.rewardservice.donation.domain;

import com.example.rewardservice.common.BaseEntity;
import com.example.rewardservice.event.domain.EventPeriod;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.List;
import java.util.UUID;

import static jakarta.persistence.CascadeType.REMOVE;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE image SET status = 'DELETED' WHERE id = ?")
@Where(clause = "status = 'USABLE'")
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

    @OneToMany(mappedBy = "donation", cascade = REMOVE)
    private List<DonationImage> images;

    @Embedded
    private EventPeriod eventPeriod;

    @OneToMany(mappedBy = "donation", cascade = CascadeType.ALL)
    private List<DonationRecord> donationRecords;

    public void addDonation(long amount) {
        this.currentAmount += amount;
    }

    public void updateDonation(String title, long currentAmount, long targetAmount, List<DonationImage> images, EventPeriod eventPeriod) {
        this.title = title;
        this.currentAmount = currentAmount;
        this.targetAmount = targetAmount;
        this.images = images;
        this.eventPeriod = eventPeriod;
    }
}
