package cn.bunny.mongodb.dao.enity;

import lombok.Data;

import java.util.List;

@Data
public class ResponseData<T> {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPage;
    private Integer total;
    private List<T> list;
}
