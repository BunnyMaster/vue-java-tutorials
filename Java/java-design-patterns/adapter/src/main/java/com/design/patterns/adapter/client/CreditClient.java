package com.design.patterns.adapter.client;

import com.design.patterns.adapter.model.CreditEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 信用信息客户端
 *
 * @author bunny
 */
public class CreditClient implements AdapterClient<CreditEntity> {
    private final List<CreditEntity> creditList;

    public CreditClient() {
        this.creditList = List.of(
                createCreditEntity(1L, "CRED001", "张三有限公司", "北京市朝阳区建国路88号",
                        "13800138001", "zhangsan@example.com", 85.5, "AA", "良好",
                        "科技行业", "软件开发", LocalDateTime.now().minusYears(2), "张三",
                        LocalDateTime.now(), LocalDateTime.now(), "低风险", "正常", 0, new BigDecimal("10000.00")),
                createCreditEntity(2L, "CRED002", "李四贸易公司", "上海市浦东新区陆家嘴金融中心",
                        "13800138002", "lisi@example.com", 92.0, "AAA", "优秀",
                        "贸易行业", "进出口贸易", LocalDateTime.now().minusYears(3), "李四",
                        LocalDateTime.now(), LocalDateTime.now(), "低风险", "正常", 1, new BigDecimal("50000.00")),
                createCreditEntity(3L, "CRED003", "王五制造厂", "广州市天河区高新技术开发区",
                        "13800138003", "wangwu@example.com", 78.0, "A", "一般",
                        "制造业", "机械制造", LocalDateTime.now().minusYears(5), "王五",
                        LocalDateTime.now(), LocalDateTime.now(), "中风险", "正常", 2, new BigDecimal("150000.00")),
                createCreditEntity(4L, "CRED004", "赵六服务公司", "深圳市南山区科技园",
                        "13800138004", "zhaoliu@example.com", 95.5, "AAA", "优秀",
                        "服务业", "咨询服务", LocalDateTime.now().minusYears(1), "赵六",
                        LocalDateTime.now(), LocalDateTime.now(), "低风险", "正常", 0, new BigDecimal("8000.00")),
                createCreditEntity(5L, "CRED005", "钱七投资公司", "杭州市西湖区文三路",
                        "13800138005", "qianqi@example.com", 72.0, "B", "较差",
                        "金融行业", "投资管理", LocalDateTime.now().minusYears(4), "钱七",
                        LocalDateTime.now(), LocalDateTime.now(), "高风险", "关注", 5, new BigDecimal("500000.00"))
        );
    }

    private CreditEntity createCreditEntity(Long id, String creditCode, String name, String address,
                                            String phone, String email, Double score, String level, String status,
                                            String industry, String businessScope, LocalDateTime establishDate,
                                            String legalRepresentative, LocalDateTime createTime, LocalDateTime updateTime,
                                            String riskLevel, String blacklistStatus, Integer overdueCount,
                                            BigDecimal debtAmount) {
        return new CreditEntity(id, creditCode, name, address, phone, email, score, level, status,
                industry, businessScope, establishDate, legalRepresentative, createTime, updateTime,
                riskLevel, blacklistStatus, overdueCount, debtAmount);
    }

    /**
     * 获取数据
     *
     * @return 数据列表
     */
    @Override
    public List<CreditEntity> getData() {
        return this.creditList;
    }
}
