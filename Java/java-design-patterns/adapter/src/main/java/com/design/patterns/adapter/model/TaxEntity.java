package com.design.patterns.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 税务实体类
 *
 * @author bunny
 */
@Data
@AllArgsConstructor
public class TaxEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;


    /**
     * 税务记录ID
     */
    private Long id;

    /**
     * 纳税人识别号
     */
    private String taxpayerId;

    /**
     * 纳税人姓名
     */
    private String taxpayerName;

    /**
     * 税种类型
     */
    private String taxType;

    /**
     * 应税金额
     */
    private BigDecimal taxableAmount;

    /**
     * 税率
     */
    private BigDecimal taxRate;

    /**
     * 应缴税额
     */
    private BigDecimal taxAmount;

    /**
     * 已缴金额
     */
    private BigDecimal paidAmount;

    /**
     * 申报日期
     */
    private LocalDate filingDate;

    /**
     * 缴款日期
     */
    private LocalDate paymentDate;

    /**
     * 记录状态
     */
    private String status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
