package cn.bunny.drools.bean.demo.demo2;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    /* 用户名 */
    private String username;

    /* 年龄 */
    private Integer age;

}
