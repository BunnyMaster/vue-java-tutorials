package cn.bunny.drools.bean.demo.demo2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Content {

    private String id;

    /* 内容类型 */
    private ContentType type;


}