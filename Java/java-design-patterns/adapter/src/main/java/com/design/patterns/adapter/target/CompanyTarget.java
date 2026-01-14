package com.design.patterns.adapter.target;

import com.design.patterns.adapter.client.CompanyClient;
import com.design.patterns.adapter.model.AdapterInfoVO;
import com.design.patterns.adapter.model.CompanyEntity;

import java.util.List;

/**
 * 适配公司返回结果
 *
 * @author liujingman
 */
public class CompanyTarget implements TargetClient {
    private final CompanyClient companyClient;

    public CompanyTarget(CompanyClient companyClient) {
        this.companyClient = companyClient;
    }

    /**
     * 适配接口
     *
     * @return 适配后的数据
     */
    @Override
    public List<AdapterInfoVO> response() {
        List<CompanyEntity> data = companyClient.getData();
        return data.stream().map(companyEntity -> {
                    AdapterInfoVO adapterInfoVO = new AdapterInfoVO();
                    adapterInfoVO.setId(companyEntity.getId());
                    adapterInfoVO.setName(companyEntity.getName());
                    adapterInfoVO.setCode(companyEntity.getId());
                    adapterInfoVO.setAddress(companyEntity.getAddress());
                    adapterInfoVO.setPhone(companyEntity.getPhone());
                    adapterInfoVO.setEmail(companyEntity.getEmail());
                    adapterInfoVO.setClientType(companyEntity.getIndustry());
                    adapterInfoVO.setUpdateTime(null);
                    adapterInfoVO.setCreateTime(null);

                    return adapterInfoVO;
                })
                .toList();
    }
}
