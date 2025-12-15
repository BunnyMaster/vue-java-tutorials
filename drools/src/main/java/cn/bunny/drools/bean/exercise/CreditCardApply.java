package cn.bunny.drools.bean.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardApply {

    public static final String EDUCATION_1 = "专科以下";
    public static final String EDUCATION_2 = "专科";
    public static final String EDUCATION_3 = "本科";
    public static final String EDUCATION_4 = "本科以上";

    private String name;

    private String sex;

    private int age;

    private String education;

    private String telephone;

    /* 月收入 */
    private double monthlyIncome = 0;

    private String address;

    /* 是否有房 */
    private boolean hasHouse = false;

    /* 是否有车 */
    private boolean hasCar = false;

    /* 现持有信用卡数量 */
    private int hasCreditCardCount = 0;

    /* 审核是否通过 */
    private boolean checkResult = true;

    /* 额度 */
    private double quota = 0;

}
