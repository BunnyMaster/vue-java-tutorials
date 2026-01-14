package com.design.patterns.adapter.client;


import com.design.patterns.adapter.model.CompanyEntity;

import java.util.List;

/**
 * 公司客户端
 *
 * @author bunny
 */
public class CompanyClient implements AdapterClient<CompanyEntity> {
    private final List<CompanyEntity> companyList;

    public CompanyClient() {
        // 初始化一些示例数据
        this.companyList = List.of(
                new CompanyEntity("COMP001", "阿里巴巴集团", "浙江省杭州市余杭区文一西路969号",
                        "0571-85022088", "info@alibaba.com", "https://www.alibaba.com",
                        "互联网科技", 25000, "全球知名的电子商务和科技公司"),
                new CompanyEntity("COMP002", "腾讯控股", "广东省深圳市南山区科技园科技中一路",
                        "0755-86013388", "contact@tencent.com", "https://www.tencent.com",
                        "互联网科技", 100000, "中国领先的互联网增值服务提供商"),
                new CompanyEntity("COMP003", "百度公司", "北京市海淀区上地十街10号",
                        "010-59928888", "ir@baidu.com", "https://www.baidu.com",
                        "互联网科技", 40000, "全球最大的中文搜索引擎服务商"),
                new CompanyEntity("COMP004", "华为技术", "广东省深圳市龙岗区坂田华为基地",
                        "0755-28780808", "huawei@huawei.com", "https://www.huawei.com",
                        "通信设备", 190000, "全球领先的信息与通信技术解决方案供应商"),
                new CompanyEntity("COMP005", "小米科技", "北京市海淀区清河中街68号",
                        "010-62351111", "contact@mi.com", "https://www.mi.com",
                        "消费电子", 30000, "专注于智能硬件和电子产品研发的互联网公司")
        );
    }

    /**
     * 获取数据
     *
     * @return 数据列表
     */
    @Override
    public List<CompanyEntity> getData() {
        return this.companyList;
    }
}
