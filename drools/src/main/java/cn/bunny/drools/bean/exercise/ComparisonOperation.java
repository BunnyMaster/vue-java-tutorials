package cn.bunny.drools.bean.exercise;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComparisonOperation {

    /* 字段名称 */
    private String field;

    /* 操作列表 */
    private List<String> operatorList;

}
