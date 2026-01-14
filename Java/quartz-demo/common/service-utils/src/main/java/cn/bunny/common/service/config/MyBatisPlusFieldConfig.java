package cn.bunny.common.service.config;

import cn.bunny.common.service.context.BaseContext;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 配置MP在修改和新增时的操作
 */
@Component
public class MyBatisPlusFieldConfig implements MetaObjectHandler {

    /**
     * 使用mp做添加操作时候，这个方法执行
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        // 设置属性值
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("deleteStatus", 1, metaObject);
        if (BaseContext.getUsername() != null) {
            this.setFieldValByName("createBy", BaseContext.getUsername(), metaObject);
            this.setFieldValByName("updateBy", BaseContext.getUsername(), metaObject);
        }
    }

    /**
     * 使用mp做修改操作时候，这个方法执行
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", BaseContext.getUsername(), metaObject);
    }
}
