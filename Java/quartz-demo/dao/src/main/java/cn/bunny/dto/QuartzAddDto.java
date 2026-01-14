package cn.bunny.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuartzAddDto {
    private String jobName;// 任务名称
    private String jobGroup;// 任务分组
    private String cronExpression;// 执行时间
    private String description;// 任务描述
    private String jobMethodName;// 执行方法
    private String jobClassName;// 执行类
}
