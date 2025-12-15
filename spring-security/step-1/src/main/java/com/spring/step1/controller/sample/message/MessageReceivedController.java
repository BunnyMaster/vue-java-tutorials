package com.spring.step1.controller.sample.message;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.MessageReceivedDto;
import com.spring.step1.bean.entity.MessageReceivedEntity;
import com.spring.step1.bean.vo.MessageReceivedVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.MessageReceivedService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "消息接受接口", description = "消息接受相关接口")
@RestController
@RequestMapping("/api/message/message-received")
@RequiredArgsConstructor
public class MessageReceivedController {

    private final MessageReceivedService messageReceivedService;

    @Operation(summary = "消息接受分页查询", description = "消息接受分页")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<MessageReceivedVo>> getMessageReceivedPage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            MessageReceivedDto dto) {
        Page<MessageReceivedEntity> pageParams = new Page<>(page, limit);
        PageResult<MessageReceivedVo> pageResult = messageReceivedService.getMessageReceivedPage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加", description = "添加消息接受")
    @PostMapping()
    public Result<String> addMessageReceived(@Valid @RequestBody MessageReceivedDto dto) {
        messageReceivedService.addMessageReceived(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新", description = "更新消息接受")
    @PutMapping()
    public Result<String> updateMessageReceived(@Valid @RequestBody MessageReceivedDto dto) {
        messageReceivedService.updateMessageReceived(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除", description = "删除消息接受")
    @DeleteMapping()
    public Result<String> deleteMessageReceived(@RequestBody List<Long> ids) {
        messageReceivedService.deleteMessageReceived(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}