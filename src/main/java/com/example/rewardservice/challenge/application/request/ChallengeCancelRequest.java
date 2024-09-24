package com.example.rewardservice.challenge.application.request;

import lombok.Getter;

import java.util.UUID;

public record ChallengeCancelRequest (UUID challengeId){
}
