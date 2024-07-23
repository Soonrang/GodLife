package com.example.rewardservice.Admin.application;

import com.example.rewardservice.event.application.repository.EventRepository;
import com.example.rewardservice.point.PointRepository;
import com.example.rewardservice.security.jwt.util.JWTUtil;
import com.example.rewardservice.shop.domain.repository.ProductRepository;
import com.example.rewardservice.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Value;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService {

  private final String id;
  private final String pwd;

  private final JWTUtil jwtUtil;
  private final UserRepository userRepository;
  private final PointRepository pointRepository;
  private final EventRepository eventRepository;
  private final ProductRepository productRepository;

  public AdminService(@Value("${com.example.admin.id}") String id,
                      @Value("${com.example.admin.pwd}") String pwd,
                      JWTUtil jwtUtil,
                      UserRepository userRepository,
                      PointRepository pointRepository,
                      EventRepository eventRepository,
                      ProductRepository productRepository) {
      this.id = id;
      this.pwd = pwd;
      this.jwtUtil = jwtUtil;
      this.userRepository = userRepository;
      this.pointRepository = pointRepository;
      this.eventRepository = eventRepository;
      this.productRepository = productRepository;
  }
}
