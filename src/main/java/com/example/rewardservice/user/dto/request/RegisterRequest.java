package com.example.rewardservice.user.dto.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
    private String name;
    private String profileUrl;
    private String gender;
    private String nickName;

}
