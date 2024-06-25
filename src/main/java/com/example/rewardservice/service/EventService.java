package com.example.rewardservice.service;

import com.example.rewardservice.domain.User.UserEventParticipation;
import com.example.rewardservice.repository.EventRepository;
import com.example.rewardservice.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;
    private final UserEventParticipationRepository participationRepository;
    private final PointRepository pointRepository;
    private final PointService pointService;


}