package com.spring.step1.bean.entity;

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
@TableName("sys_files")
@Schema(name = "Files对象", title = "文件记录", description = "文件记录的实体类对象")
public class FilesEntity {

    @Schema(name = "id", title = "文件id")
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(name = "url", title = "文件访问地址")
    private String url;

    @Schema(name = "size", title = "文件大小，单位字节")
    private Long size;

    @Schema(name = "fileSizeStr", title = "文件大小（字符串）")
    private String fileSizeStr;

    @Schema(name = "filename", title = "文件名称")
    private String filename;

    @Schema(name = "originalFilename", title = "原始文件名")
    private String originalFilename;

    @Schema(name = "basePath", title = "基础存储路径")
    private String basePath;

    @Schema(name = "filepath", title = "存储路径")
    private String filepath;

    @Schema(name = "ext", title = "文件扩展名")
    private String ext;

    @Schema(name = "contentType", title = "MIME类型")
    private String contentType;

    @Schema(name = "platform", title = "存储平台")
    private String platform;

    @Schema(name = "thUrl", title = "缩略图访问路径")
    private String thUrl;

    @Schema(name = "thFilename", title = "缩略图名称")
    private String thFilename;

    @Schema(name = "thSize", title = "缩略图大小，单位字节")
    private Long thSize;

    @Schema(name = "thContentType", title = "缩略图MIME类型")
    private String thContentType;

    @Schema(name = "objectId", title = "文件所属对象id")
    private String objectId;

    @Schema(name = "objectType", title = "文件所属对象类型，例如用户头像，评价图片")
    private String objectType;

    @Schema(name = "metadata", title = "文件元数据")
    private String metadata;

    @Schema(name = "userMetadata", title = "文件用户元数据")
    private String userMetadata;

    @Schema(name = "thMetadata", title = "缩略图元数据")
    private String thMetadata;

    @Schema(name = "thUserMetadata", title = "缩略图用户元数据")
    private String thUserMetadata;

    @Schema(name = "attr", title = "附加属性")
    private String attr;

    @Schema(name = "fileAcl", title = "文件ACL")
    private String fileAcl;

    @Schema(name = "thFileAcl", title = "缩略图文件ACL")
    private String thFileAcl;

    @Schema(name = "hashInfo", title = "哈希信息")
    private String hashInfo;

    @Schema(name = "uploadId", title = "上传ID，仅在手动分片上传时使用")
    private String uploadId;

    @Schema(name = "uploadStatus", title = "上传状态，仅在手动分片上传时使用，1：初始化完成，2：上传完成")
    private Integer uploadStatus;

    @Schema(name = "downloadCount", title = "下载数量")
    private Integer downloadCount;

    @Schema(name = "createUser", title = "创建用户")
    private Long createUser;

    @Schema(name = "updateUser", title = "操作用户")
    private Long updateUser;

    @Schema(name = "createTime", title = "创建时间")
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "记录文件最后修改的时间戳")
    private LocalDateTime updateTime;

    @Schema(name = "isDeleted", title = "文件是否被删除")
    private Boolean isDeleted;

}