package com.example.rewardservice.user.application.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;

}
