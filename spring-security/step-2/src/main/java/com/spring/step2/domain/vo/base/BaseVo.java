package com.spring.step2.domain.vo.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BaseVo {

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户ID")
    private Long createUser;

    @Schema(name = "updateUser", title = "更新用户ID")
    private Long updateUser;
    
}
