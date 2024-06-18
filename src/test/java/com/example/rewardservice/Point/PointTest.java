package com.example.rewardservice.Point;

import com.example.rewardservice.domain.Point;

import com.example.rewardservice.domain.PointLog;
import com.example.rewardservice.service.PointService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class PointTest {

    @Autowired
    private PointService pointService;

    @Test
    public void testCreatePoint() {
        Point point = pointService.createPoint("testuser", 1000);

        assertThat(point).isNotNull();
        assertThat(point.getUserId()).isEqualTo("testuser");
        assertThat(point.getTotalPoint()).isEqualTo(1000);
    }

    @Test
    public void testEarnPoints() {
        Point point = pointService.createPoint("testuser", 1000);
        PointLog pointLog = pointService.earnPoints(point.getId(), 500, "룰렛");

        assertThat(pointLog).isNotNull();
        assertThat(pointLog.getTransactionType()).isEqualTo("적립");
        assertThat(pointLog.getAmount()).isEqualTo(500);
        assertThat(pointLog.getReason()).isEqualTo("룰렛");

        Point updatedPoint = pointService.getPointById(point.getId());
        assertThat(updatedPoint.getTotalPoint()).isEqualTo(1500);
    }

    @Test
    public void testUsePoints() {
        Point point = pointService.createPoint("testuser", 1000);
        PointLog pointLog = pointService.usePoints(point.getId(), 500, "구입");

        assertThat(pointLog).isNotNull();
        assertThat(pointLog.getTransactionType()).isEqualTo("사용");
        assertThat(pointLog.getAmount()).isEqualTo(500);
        assertThat(pointLog.getReason()).isEqualTo("구입");

        Point updatedPoint = pointService.getPointById(point.getId());
        assertThat(updatedPoint.getTotalPoint()).isEqualTo(500);
    }
}