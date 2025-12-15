package com.auth.ai.ai.tools;

import com.auth.ai.model.entity.ReservationEntity;
import com.auth.ai.service.ReservationService;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class ReservationTool {

    private final ReservationService reservationService;

    @Tool("添加志愿指导服务预约")
    public void addReservation(@P("考生姓名") String name,
                               @P("考生性别") String gender,
                               @P("考生手机号") String phone,
                               @P("预约沟通时间：格式为：yyyy-MM-dd'T'HH:mm") String communicationTime,
                               @P("考生所处的省份") String province,
                               @P("考生预估分数") Integer estimatedScore) {
        ReservationEntity reservation = ReservationEntity.builder()
                .name(name)
                .gender(gender)
                .phone(phone)
                .communicationTime(LocalDateTime.parse(communicationTime))
                .province(province)
                .estimatedScore(estimatedScore)
                .build();

        reservationService.save(reservation);
    }

    @Tool("根据考生电话查询考生预约详情")
    public ReservationEntity findReservationById(@P("考生手机号") String phone) {
        return reservationService.getOne(Wrappers.<ReservationEntity>lambdaQuery().eq(ReservationEntity::getPhone, phone));
    }

}
