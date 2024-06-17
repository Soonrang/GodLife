package com.example.rewardservice.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PointLogDTO {
    private String userId;
    private String transactionType;
    private long mount;
    private String reason;
}
