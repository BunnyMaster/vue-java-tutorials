package com.design.patterns.adapter.target;

import com.design.patterns.adapter.client.TaxClient;
import com.design.patterns.adapter.model.AdapterInfoVO;
import com.design.patterns.adapter.model.TaxEntity;

import java.util.List;

/**
 * 税务信息适配器
 *
 * @author bunny
 */
public class TaxTarget implements TargetClient {
    private final TaxClient taxClient;

    public TaxTarget(TaxClient taxClient) {
        this.taxClient = taxClient;
    }

    /**
     * 适配接口
     *
     * @return 适配后的数据
     */
    @Override
    public List<AdapterInfoVO> response() {
        List<TaxEntity> data = taxClient.getData();

        return data.stream().map(taxEntity -> {
                    AdapterInfoVO adapterInfoVO = new AdapterInfoVO();
                    Long id = taxEntity.getId();
                    adapterInfoVO.setId(String.valueOf(id));
                    adapterInfoVO.setName(taxEntity.getTaxpayerName());
                    adapterInfoVO.setCode(taxEntity.getTaxpayerId());
                    adapterInfoVO.setAddress(taxEntity.getTaxpayerName());
                    adapterInfoVO.setPhone(null);
                    adapterInfoVO.setEmail(null);
                    adapterInfoVO.setClientType(taxEntity.getTaxType());
                    adapterInfoVO.setUpdateTime(null);
                    adapterInfoVO.setCreateTime(taxEntity.getCreateTime());

                    return adapterInfoVO;
                })
                .toList();
    }
}
