package com.example.rewardservice.Point;

import com.example.rewardservice.domain.User.User;
import com.example.rewardservice.dto.User.UserDTO;
import com.example.rewardservice.service.PointService;
import com.example.rewardservice.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("유저가 추가되면 성공")
    public void addUserTest() throws Exception{
        //Given
        String userId = "테스트유저1";
        long initialPoints = 1000L;

        //When
        UserDTO createdUser = userService.createUser(userId, initialPoints);
        //Then
        assertThat(createdUser).isNotNull();
        assertThat(createdUser.getUserId()).isEqualTo(userId);
        assertThat(createdUser.getTotalPoint()).isEqualTo(initialPoints);

    }

    @Test
    @DisplayName("유저정보가 조회되면 성공")
    public void viewUserTest() throws Exception{
        //Given
        String userId = "testUser";
        long initialPoints = 1000L;
        UserDTO newUser = userService.createUser(userId, initialPoints);
        UUID userUUID = newUser.getId();

        //When
        UserDTO foundUser = userService.viewUser(userUUID);

        //Then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUserId()).isEqualTo(userId);
        assertThat(foundUser.getTotalPoint()).isEqualTo(initialPoints);
    }

}
