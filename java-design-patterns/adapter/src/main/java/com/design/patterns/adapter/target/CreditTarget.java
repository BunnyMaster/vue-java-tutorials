package com.design.patterns.adapter.target;

import com.design.patterns.adapter.client.CreditClient;
import com.design.patterns.adapter.model.AdapterInfoVO;
import com.design.patterns.adapter.model.CreditEntity;

import java.util.List;

/**
 * 适配器目标类
 *
 * @author bunny
 */
public class CreditTarget implements TargetClient {
    private final CreditClient creditClient;

    public CreditTarget(CreditClient creditClient) {
        this.creditClient = creditClient;
    }

    /**
     * 适配接口
     *
     * @return 适配后的数据
     */
    @Override
    public List<AdapterInfoVO> response() {
        List<CreditEntity> data = creditClient.getData();
        return data.stream().map(creditEntity -> {
                    AdapterInfoVO adapterInfoVO = new AdapterInfoVO();
                    Long id = creditEntity.getId();
                    adapterInfoVO.setId(String.valueOf(id));
                    adapterInfoVO.setName(creditEntity.getName());
                    adapterInfoVO.setCode(creditEntity.getCreditCode());
                    adapterInfoVO.setAddress(creditEntity.getAddress());
                    adapterInfoVO.setPhone(creditEntity.getPhone());
                    adapterInfoVO.setEmail(creditEntity.getEmail());
                    adapterInfoVO.setClientType(creditEntity.getIndustry());
                    adapterInfoVO.setUpdateTime(creditEntity.getUpdateTime());
                    adapterInfoVO.setCreateTime(creditEntity.getCreateTime());

                    return adapterInfoVO;
                })
                .toList();
    }
}
