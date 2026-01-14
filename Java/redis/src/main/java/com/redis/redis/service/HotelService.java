package com.redis.redis.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.redis.redis.model.entity.HotelEntity;
import com.redis.redis.model.vo.HotelVO;

public interface HotelService extends IService<HotelEntity> {

    /**
     * 根据Id查询酒店信息
     *
     * @param id 酒店 Id
     * @return 酒店信息
     */
    HotelVO queryWithMutex(Long id);

    /**
     * 根据Id查询酒店信息-防止缓存穿透
     *
     * @param id 酒店 Id
     * @return 酒店信息
     */
    HotelVO queryWithPassThrough(Long id);
}
