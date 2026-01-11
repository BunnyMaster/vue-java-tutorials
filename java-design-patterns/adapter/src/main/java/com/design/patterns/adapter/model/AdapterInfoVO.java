package com.design.patterns.adapter.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 要融合成客户端使用的数据
 *
 * @author bunny
 */
@Data
public class AdapterInfoVO implements Serializable {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 客户ID
     */
    private String id;

    /**
     * 客户名称
     */
    private String name;

    /**
     * 客户编码
     */
    private String code;

    /**
     * 客户地址
     */
    private String address;

    /**
     * 客户电话
     */
    private String phone;

    /**
     * 客户邮箱
     */
    private String email;

    /**
     * 客户类型
     */
    private String clientType;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
