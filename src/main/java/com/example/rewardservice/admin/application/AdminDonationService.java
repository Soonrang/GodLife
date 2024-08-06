package com.example.rewardservice.admin.application;


import com.example.rewardservice.admin.application.dto.AdminDonationRequest;
import com.example.rewardservice.donation.domain.Donation;
import com.example.rewardservice.donation.domain.DonationRepository;
import com.example.rewardservice.image.application.service.ImageService;
import com.example.rewardservice.image.s3.S3ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminDonationService {

    private final DonationRepository donationRepository;
    private final S3ImageService s3ImageService;

    public void registerDonation(AdminDonationRequest adminDonationRequest) {
        Donation donation = Donation.builder()
                .id(UUID.randomUUID())
                .title(adminDonationRequest.title())
                .currentAmount(adminDonationRequest.currentAmount())
                .targetAmount(adminDonationRequest.targetAmount())
                .eventPeriod(adminDonationRequest.eventPeriod())
                .images(adminDonationRequest.imageUrl())
                .build();

        validateDuplicateDonation(donation.getId());

        donationRepository.save(donation);
    }

    public void updateDonation(UUID id, AdminDonationRequest adminDonationRequest) {
        Donation donation = donationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("현재 찾으려는 기부아이디: " + id + " not found"));

        String originalImageUrl = donation.getImages();

        donation.updateDonation(adminDonationRequest.title(),
                adminDonationRequest.currentAmount(),
                adminDonationRequest.targetAmount(),
                adminDonationRequest.imageUrl(),
                adminDonationRequest.eventPeriod()
        );
        //저장된 이미지 삭제하는 로직
        if(originalImageUrl != adminDonationRequest.imageUrl()) {
            s3ImageService.deleteImageFromS3(originalImageUrl);
        }

        s3ImageService.upload(adminDonationRequest.imageUrl())



        donationRepository.save(donation);
    }

    private void validateDuplicateDonation(UUID donationId) {
        if(donationRepository.findById(donationId).isPresent()) {
            throw new IllegalArgumentException("Donation with id " + donationId + " already exists");
        }
    }
}
