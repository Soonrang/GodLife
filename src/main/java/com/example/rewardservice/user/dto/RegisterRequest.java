package com.example.rewardservice.user.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String userId;
    private String userPassword;
    private String userName;
    private String email;
}
