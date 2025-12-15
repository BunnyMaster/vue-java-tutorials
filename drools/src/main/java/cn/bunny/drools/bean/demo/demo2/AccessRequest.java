package cn.bunny.drools.bean.demo.demo2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessRequest {

    /* 用户 */
    private User user;

    /* 访问的内容 */
    private Content content;

    /* 有权访问 */
    private boolean granted;

    /* 拒绝的理由 */
    private String denialReason;

}