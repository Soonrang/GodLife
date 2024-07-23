package com.example.rewardservice.admin.domain;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final String id;

    @Column(name = "admin_email")
    private final String adminEmail;

    @Column(name = "admin_password")
    private final String adminPassword;

}
