package com.redis.redis.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Schema(title = "Hotel对象", description = "酒店返回对象")
@Data
public class HotelVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(title = "酒店id")
    private Long id;

    @Schema(title = "酒店名称")
    private String name;

    @Schema(title = "酒店地址")
    private String address;

    @Schema(title = "酒店价格")
    private Integer price;

    @Schema(title = "酒店评分")
    private Integer score;

    @Schema(title = "酒店品牌")
    private String brand;

    @Schema(title = "所在城市")
    private String city;

    @Schema(title = "酒店星级，1星到5星，1钻到5钻")
    private String starName;

    @Schema(title = "商圈")
    private String business;

    @Schema(title = "纬度")
    private String latitude;

    @Schema(title = "经度")
    private String longitude;

    @Schema(title = "酒店图片")
    private String pic;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(title = "系统时间")
    private LocalDateTime systemTime;

}