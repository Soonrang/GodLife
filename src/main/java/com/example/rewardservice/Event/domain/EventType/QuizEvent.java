package com.example.rewardservice.Event.domain.EventType;

import com.example.rewardservice.Event.domain.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("QUIZ")
public class QuizEvent extends Event {



}
