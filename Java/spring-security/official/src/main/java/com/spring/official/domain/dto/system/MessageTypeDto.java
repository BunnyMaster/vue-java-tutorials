package com.spring.official.domain.dto.system;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "MessageTypeDTO对象", title = "系统消息类型", description = "系统消息类型的DTO对象")
public class MessageTypeDto {

    @Schema(name = "id", title = "ID")
    private Long id;

    @Schema(name = "messageName", title = "消息名称")
    private String messageName;

    @Schema(name = "messageType", title = "消息类型")
    private String messageType;

    @Schema(name = "summary", title = "消息备注")
    private String summary;

    @Schema(name = "status", title = "0:启用 1:禁用")
    private Integer status;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "修改时间")
    private LocalDateTime updateTime;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "isDeleted", title = "是否被删除")
    private Boolean isDeleted;

}