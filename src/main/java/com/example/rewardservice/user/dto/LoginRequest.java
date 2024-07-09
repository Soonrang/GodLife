package com.example.rewardservice.user.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String userPassword;
    private String email;
}
