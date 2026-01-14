package com.design.patterns.adapter.client;

import com.design.patterns.adapter.model.TaxEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 税务客户端类
 *
 * @author bunny
 */
public class TaxClient implements AdapterClient<TaxEntity> {
    private final List<TaxEntity> taxList;

    public TaxClient() {
        this.taxList = List.of(
                new TaxEntity(1L, "91330000MA27K5JG7R", "杭州科技有限公司", "增值税",
                        new BigDecimal("100000.00"), new BigDecimal("0.13"), new BigDecimal("13000.00"),
                        new BigDecimal("13000.00"), LocalDate.now().minusMonths(1), LocalDate.now().minusDays(5),
                        "已缴纳", LocalDateTime.now()),
                new TaxEntity(2L, "91440000MA5DQBXN7X", "深圳贸易有限公司", "企业所得税",
                        new BigDecimal("500000.00"), new BigDecimal("0.25"), new BigDecimal("125000.00"),
                        new BigDecimal("100000.00"), LocalDate.now().minusMonths(2), LocalDate.now().minusWeeks(1),
                        "部分缴纳", LocalDateTime.now()),
                new TaxEntity(3L, "91110000MA009TCA1Y", "北京服务有限公司", "增值税",
                        new BigDecimal("200000.00"), new BigDecimal("0.06"), new BigDecimal("12000.00"),
                        new BigDecimal("0.00"), LocalDate.now().minusWeeks(2), null,
                        "未缴纳", LocalDateTime.now()),
                new TaxEntity(4L, "91320000MA1NL0G73R", "南京制造有限公司", "印花税",
                        new BigDecimal("800000.00"), new BigDecimal("0.0005"), new BigDecimal("400.00"),
                        new BigDecimal("400.00"), LocalDate.now().minusMonths(3), LocalDate.now().minusMonths(3),
                        "已缴纳", LocalDateTime.now()),
                new TaxEntity(5L, "91310000MA1234567X", "上海咨询有限公司", "个人所得税",
                        new BigDecimal("300000.00"), new BigDecimal("0.20"), new BigDecimal("60000.00"),
                        new BigDecimal("60000.00"), LocalDate.now().minusMonths(1), LocalDate.now().minusDays(10),
                        "已缴纳", LocalDateTime.now())
        );
    }

    /**
     * 获取数据
     *
     * @return 数据列表
     */
    @Override
    public List<TaxEntity> getData() {
        return this.taxList;
    }
}
