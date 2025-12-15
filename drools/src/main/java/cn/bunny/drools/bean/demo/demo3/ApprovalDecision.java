package cn.bunny.drools.bean.demo.demo3;

public interface ApprovalDecision {

    /* 最高额度 */
    String APPROVED_HIGH = "APPROVED_HIGH";

    /* 中等额度 */
    String APPROVED_MEDIUM = "APPROVED_MEDIUM";

    /* 最低额度 */
    String APPROVED_LOW = "APPROVED_LOW";

    /* 拒绝 */
    String REJECTED = "REJECTED";
}