package com.example.rewardservice.event.domain.EventType;

import com.example.rewardservice.event.domain.Event;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("ROULETTE")
public class RouletteEvent extends Event {

}
