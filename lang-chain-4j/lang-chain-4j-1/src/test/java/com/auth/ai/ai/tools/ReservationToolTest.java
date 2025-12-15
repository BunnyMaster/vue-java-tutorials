package com.auth.ai.ai.tools;

import com.auth.ai.model.entity.ReservationEntity;
import com.auth.ai.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class ReservationToolTest {

    @Autowired
    private ReservationService reservationService;

    @Test
    void addReservation() {
        ReservationEntity build = ReservationEntity.builder()
                .name("好人")
                .gender("女")
                .phone("19988885555")
                .communicationTime(LocalDateTime.parse("2025-12-30T14:30"))
                .estimatedScore(730)
                .province("江苏省淮安市")
                .build();
        reservationService.save(build);
    }
}