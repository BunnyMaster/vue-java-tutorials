package com.spring.official.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Getter
@Setter
@Accessors(chain = true)
@TableName("sys_message_received")
@Schema(name = "MessageReceived对象", title = "接受消息Vo", description = "的实体类对象")
public class MessageReceivedEntity {

    @Schema(name = "id", title = "主键")
    @TableId(type = IdType.ASSIGN_ID)
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