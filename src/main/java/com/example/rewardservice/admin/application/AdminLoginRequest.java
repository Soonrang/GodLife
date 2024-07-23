package com.example.rewardservice.admin.application;

import com.example.rewardservice.admin.domain.Admin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminLoginRequest {

    @NotNull
    private String account;

    @NotNull
    private String password;

    @NotNull
    private String secondPassword;

    public static Admin toAdmin(AdminLoginRequest adminLoginRequest) {
        return new Admin(
                adminLoginRequest.getAccount(),
                adminLoginRequest.getPassword(),
                adminLoginRequest.getSecondPassword()
        );
    }
}
