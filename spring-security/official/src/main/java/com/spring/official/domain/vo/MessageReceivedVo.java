package com.spring.official.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MessageReceivedVO对象", title = "消息接受返回对象", description = "的VO对象")
public class MessageReceivedVo {

    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "receivedUserId", title = "接受者用户ID")
    private Long receivedUserId;

    @Schema(name = "messageId", title = "消息ID")
    private Long messageId;

    @Schema(name = "status", title = "0:未读 1:已读")
    private Integer status;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间")
    private LocalDateTime updateTime;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "isDeleted", title = "是否删除")
    private Integer isDeleted;

}

