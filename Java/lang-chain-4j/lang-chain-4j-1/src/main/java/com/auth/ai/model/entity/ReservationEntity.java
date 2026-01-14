package com.auth.ai.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@TableName("reservation")
@Schema(name = "Reservation", description = "预约实体")
@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ReservationEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @Schema(description = "主键Id")
    private Long id;

    @Schema(description = "考生姓名")
    private String name;

    @Schema(description = "考生性别")
    private String gender;

    @Schema(description = "考生手机号")
    private String phone;

    @Schema(description = "沟通时间")
    private LocalDateTime communicationTime;

    @Schema(description = "考生所处的省份")
    private String province;

    @Schema(description = "考生预估分数")
    private Integer estimatedScore;

}