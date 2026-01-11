package com.design.patterns.adapter.client;

import java.util.List;

/**
 * 适配器客户端类
 *
 * @author bunny
 */
public interface AdapterClient<T> {

    /**
     * 获取数据
     *
     * @return 数据列表
     */
    List<T> getData();
}
