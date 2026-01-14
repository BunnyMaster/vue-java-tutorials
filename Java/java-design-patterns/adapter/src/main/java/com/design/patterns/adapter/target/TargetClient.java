package com.design.patterns.adapter.target;

import com.design.patterns.adapter.model.AdapterInfoVO;

import java.util.List;

/**
 * 客户端期望的接口
 *
 * @author bunny
 */
public interface TargetClient {
    /**
     * 适配接口
     *
     * @return 适配后的数据
     */
    List<AdapterInfoVO> response();
}