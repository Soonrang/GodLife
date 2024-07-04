package com.example.rewardservice.authentication.domain.dto;

public record UserInformationDto(Long id) {

    public String finaUserId() {
        return String.valueOf(id);
    }
}
