package cn.bunny.entity.quartz;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author Bunny
 * @since 2024-07-26
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("job_list")
@ApiModel(value = "JobList对象", description = "VIEW")
public class JobList implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private String jobName;

    private String jobGroup;

    private String description;

    private String jobClassName;

    private String cronExpression;

    private String triggerName;

    private String triggerState;
}
