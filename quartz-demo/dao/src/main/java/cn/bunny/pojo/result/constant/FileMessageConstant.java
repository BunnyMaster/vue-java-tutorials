package cn.bunny.pojo.result.constant;

import lombok.Data;

@Data
public class FileMessageConstant {
    public static final String DOWNLOAD_BUCKET_EXCEPTION = "下载文件失败";
    public static final String FILE_UPLOAD_EXCEPTION = "文件上传失败";
    public static final String BUCKET_EXISTS_EXCEPTION = "查询文件对象失败";
    public static final String DELETE_BUCKET_EXCEPTION = "删除文件对象失败";
    public static final String FILE_IS_EMPTY = "文件信息为空";
    public static final String FILE_IS_NOT_EXITS = "文件信息为空";
    public static final String GET_BUCKET_EXCEPTION = "获取文件信息失败";
    public static final String QUERY_BUCKET_EXCEPTION = "查询文件信息失败";
    public static final String CREATE_BUCKET_EXCEPTION = "创建文件对象失败";
    public static final String UPDATE_BUCKET_EXCEPTION = "更新文件对象失败";
    public static final String COMPOSE_OBJECT_EXCEPTION = "对象错误";
    public static final String COPY_BUCKET_EXCEPTION = "复制文件内容失败";
    public static final String DISABLE_BUCKET_EXCEPTION = "禁用文件失败";
    public static final String ENABLE_BUCKET_EXCEPTION = "启用文件失败";
}
