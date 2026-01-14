package com.design.patterns.adapter.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;


/**
 * 公司实体类
 *
 * @author bunny
 */
@Data
@AllArgsConstructor
public class CompanyEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 公司ID
     */
    private String id;

    /**
     * 公司名称
     */
    private String name;

    /**
     * 公司地址
     */
    private String address;

    /**
     * 联系电话
     */
    private String phone;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 网站地址
     */
    private String website;

    /**
     * 所属行业
     */
    private String industry;

    /**
     * 员工数量
     */
    private Integer employeeCount;

    /**
     * 公司描述
     */
    private String description;

}
