package cn.bunny.entity.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Data
public class BaseEntity implements Serializable {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @ApiModelProperty("唯一标识")
    private Long id;

    @TableField("create_time")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @TableField("update_time")
    @ApiModelProperty("更新时间")
    private Date updateTime;

    @TableField("update_user")
    @ApiModelProperty("操作用户ID")
    private Long updateUser;

    @TableLogic
    @TableField("is_deleted")
    @ApiModelProperty("是否被删除")
    private Boolean isDeleted;

    @TableField(exist = false)
    private Map<String, Object> param = new HashMap<>();
}
