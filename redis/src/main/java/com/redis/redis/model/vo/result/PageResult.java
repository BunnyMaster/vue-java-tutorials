package com.redis.redis.model.vo.result;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * 封装分页查询结果
 */
@Schema(name = "分页返回结果", title = "分页返回结果", description = "分页返回结果")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> implements Serializable {

    @Schema(name = "pageNo", title = "当前页")
    private Long pageNo;

    @Schema(name = "pageSize", title = "每页记录数")
    private Long pageSize;

    @Schema(name = "pages", title = "总分页数")
    private Long pages;

    @Schema(name = "total", title = "总记录数")
    private Long total;

    @Schema(name = "list", title = "当前页数据集合")
    private List<T> list;

    /**
     * 根据分页数据和记录列表构建PageResult对象
     *
     * @param records 记录列表数据
     * @param page    分页信息对象
     * @return 返回填充了分页信息和数据的PageResult对象
     */
    public static <T> PageResult<T> of(List<T> records, IPage<T> page) {
        // 参数空值检查
        Objects.requireNonNull(records, "records cannot be null");
        Objects.requireNonNull(page, "page cannot be null");

        // 从分页对象中提取分页参数
        return PageResult.<T>builder()
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .list(records)
                .build();
    }

    /**
     * 根据分页数据和记录列表构建PageResult对象
     *
     * @param page 分页信息对象
     * @return 返回填充了分页信息和数据的PageResult对象
     */
    public static <T> PageResult<T> of(IPage<T> page) {
        // 参数空值检查
        Objects.requireNonNull(page, "page cannot be null");

        // 从分页对象中提取分页参数
        return PageResult.<T>builder()
                .pageNo(page.getCurrent())
                .pageSize(page.getSize())
                .pages(page.getPages())
                .total(page.getTotal())
                .list(page.getRecords())
                .build();
    }

}