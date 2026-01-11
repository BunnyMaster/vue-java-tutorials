package com.design.patterns.adapter;


import cn.hutool.json.JSONUtil;
import com.design.patterns.adapter.client.CompanyClient;
import com.design.patterns.adapter.model.AdapterInfoVO;
import com.design.patterns.adapter.target.CompanyTarget;
import com.design.patterns.adapter.target.TargetClient;

import java.util.List;

/**
 * 适配器模式
 *
 * @author bunny
 */
public class App {
    public static void main(String[] args) {
        TargetClient companyClient = new CompanyTarget(new CompanyClient());

        List<AdapterInfoVO> response = companyClient.response();
        System.out.println(JSONUtil.toJsonStr(response));
    }
}
