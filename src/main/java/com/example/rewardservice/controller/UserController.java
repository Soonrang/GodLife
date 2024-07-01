package com.example.rewardservice.controller;

import com.example.rewardservice.dto.Point.PointLogDTO;
import com.example.rewardservice.dto.User.UserDTO;
import com.example.rewardservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    //임시 유저 생성
    @PostMapping("/create")
    public ResponseEntity<UserDTO> createPoint(@RequestParam String userId, @RequestParam long initialPoints) {
        UserDTO point = userService.createUser(userId, initialPoints);
        return ResponseEntity.ok(point);
    }

    @GetMapping
    public ResponseEntity<UserDTO> viewUser(@RequestParam UUID id, @RequestParam)

}
