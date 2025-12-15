package cn.bunny.drools.bean.demo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookDisCount {

    /* 订单原始价格，即优惠前价格 */
    private Double originalPrice;

    /* 订单真实价格，即优惠后价格 */
    private Double realPrice;

}
