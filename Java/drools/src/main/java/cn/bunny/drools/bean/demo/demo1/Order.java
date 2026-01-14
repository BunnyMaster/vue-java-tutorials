package cn.bunny.drools.bean.demo.demo1;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    /* 金额 */
    private BigDecimal amount;

    /* 最终金额 */
    private BigDecimal finalAmount;

    /* 是否是vip */
    private Boolean vip;

    /* 积分 */
    private double score;

    /* 折扣 */
    private double discount;

    public void applyDiscount(double additionalDiscount) {
        this.discount += additionalDiscount;
        this.finalAmount = this.amount.multiply(new BigDecimal(1 - this.discount))
                .setScale(2, RoundingMode.HALF_UP);
    }
}
