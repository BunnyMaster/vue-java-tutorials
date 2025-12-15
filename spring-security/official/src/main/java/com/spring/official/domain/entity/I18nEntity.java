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
@TableName("sys_i18n")
@Schema(name = "I18n对象", title = "多语言表", description = "多语言表的实体类对象")
public class I18nEntity {

    @Schema(name = "id", title = "主键id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "keyName", title = "多语言key")
    private String keyName;

    @Schema(name = "translation", title = "多语言翻译名称")
    private String translation;

    @Schema(name = "typeName", title = "多语言类型")
    private String typeName;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "updateTime", title = "记录文件最后修改的时间戳")
    private LocalDateTime updateTime;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "isDeleted", title = "文件是否被删除")
    private Boolean isDeleted;

}