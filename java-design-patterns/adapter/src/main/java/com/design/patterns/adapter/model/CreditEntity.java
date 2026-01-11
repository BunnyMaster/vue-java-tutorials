package com.design.patterns.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 信用实体类
 *
 * @author bunny
 */
@Data
@AllArgsConstructor
public class CreditEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 信用代码/编号
     */
    private String creditCode;

    /**
     * 姓名或企业名称
     */
    private String name;

    /**
     * 地址
     */
    private String address;

    /**
     * 电话号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 信用评分
     */
    private Double score;

    /**
     * 信用等级(AAA, AA, A等)
     */
    private String level;

    /**
     * 信用状态(良好、一般、不良等)
     */
    private String status;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 经营范围
     */
    private String businessScope;

    /**
     * 成立日期
     */
    private LocalDateTime establishDate;

    /**
     * 法定代表人
     */
    private String legalRepresentative;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 风险等级
     */
    private String riskLevel;

    /**
     * 黑名单状态
     */
    private String blacklistStatus;

    /**
     * 逾期次数
     */
    private Integer overdueCount;

    /**
     * 债务金额
     */
    private BigDecimal debtAmount;

}
