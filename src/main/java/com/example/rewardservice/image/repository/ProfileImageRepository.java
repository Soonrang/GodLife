package com.example.rewardservice.image.repository;

import com.example.rewardservice.image.domain.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findById(final Long id);
    //Optional<ProfileImage> findByUserName(final String userName);

}
