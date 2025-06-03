package cn.bunny.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * * 移出、暂停、恢复任务
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuartzOperationDto {
    private String jobName;// 任务名称
    private String jobGroup;// 任务分组
}
