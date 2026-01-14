package cn.bunny.mongodb.dao.enity;

import lombok.Data;

@Data
public class Response<T> {
    private Integer code;
    private String message;
    private ResponseData<T> data;
}
