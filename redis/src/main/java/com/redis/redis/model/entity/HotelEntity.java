package com.redis.redis.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@TableName("tb_hotel")
@Schema(title = "Hotel对象", description = "酒店表")
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class HotelEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
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
}