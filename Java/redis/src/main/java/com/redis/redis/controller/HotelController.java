package com.redis.redis.controller;

import com.redis.redis.model.vo.HotelVO;
import com.redis.redis.model.vo.result.Result;
import com.redis.redis.service.HotelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "HotelController", description = "酒店常规接口")
@RequiredArgsConstructor
@RequestMapping("/api/hotel")
@RestController
public class HotelController {

    private final HotelService hotelService;

    @Operation(summary = "根据Id查询酒店信息")
    @GetMapping("/{id}")
    public Result<HotelVO> queryWithPassThrough(@PathVariable("id") Long id) {
        HotelVO hotel = hotelService.queryWithPassThrough(id);
        return Result.success(hotel);
    }

    @Operation(summary = "根据Id查询酒店信息-缓存击穿")
    @GetMapping("pass-through/{id}")
    public Result<HotelVO> queryWithMutex(@PathVariable("id") Long id) {
        HotelVO hotel = hotelService.queryWithMutex(id);
        return Result.success(hotel);
    }

}
