package cn.bunny.drools.bean.demo.demo3;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardApplication {

    private String applicantId;

    private String applicantName;

    /* 信用评分(300-850) */
    private int creditScore;

    /* 年收入(美元) */
    private double annualIncome;

    /* 现有负债(美元) */
    private double existingDebt;

    /* 审批结果(APPROVED_HIGH, APPROVED_MEDIUM, APPROVED_LOW, REJECTED) */
    private String decision;

    /* 批准额度 */
    private double approvedLimit;

    public boolean percentage() {
        return existingDebt / annualIncome > 0.5;
    }

}