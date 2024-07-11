package com.example.rewardservice.Image.repository;

import com.example.rewardservice.Image.domain.ProfileImage;

import java.util.Optional;

public interface ProfileImageRepository {

    Optional<ProfileImage> findById(final Long id);
    //Optional<ProfileImage> findByUserName(final String userName);

}
