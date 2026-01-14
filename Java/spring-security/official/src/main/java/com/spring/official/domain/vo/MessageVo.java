package com.spring.official.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "MessageVO对象", title = "系统消息", description = "系统消息的VO对象")
public class MessageVo {

    @Schema(name = "id", title = "ID")
    private Long id;

    @Schema(name = "title", title = "消息标题")
    private String title;

    @Schema(name = "sendUserId", title = "发送人用户ID")
    private Long sendUserId;

    @Schema(name = "messageType", title = "sys:系统消息,user用户消息")
    private Long messageType;

    @Schema(name = "cover", title = "封面")
    private String cover;

    @Schema(name = "summary", title = "简介")
    private String summary;

    @Schema(name = "content", title = "消息内容")
    private String content;

    @Schema(name = "editorType", title = "编辑器类型")
    private String editorType;

    @Schema(name = "level", title = "消息等级")
    private String level;

    @Schema(name = "extra", title = "消息等级详情")
    private String extra;

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

