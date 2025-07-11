package com.spring.official.controller.system;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.official.domain.dto.system.MessageTypeDto;
import com.spring.official.domain.entity.MessageTypeEntity;
import com.spring.official.domain.vo.MessageTypeVo;
import com.spring.official.domain.vo.result.PageResult;
import com.spring.official.domain.vo.result.Result;
import com.spring.official.domain.vo.result.ResultCodeEnum;
import com.spring.official.service.system.MessageTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统消息类型 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统消息类型", description = "系统消息类型相关接口")
@RestController
@RequestMapping("/api/system/message-type")
@RequiredArgsConstructor
public class MessageTypeController {

    private final MessageTypeService messageTypeService;

    @Operation(summary = "分页查询系统消息类型", description = "分页系统消息类型")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<MessageTypeVo>> getMessageTypePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            MessageTypeDto dto) {
        Page<MessageTypeEntity> pageParams = new Page<>(page, limit);
        PageResult<MessageTypeVo> pageResult = messageTypeService.getMessageTypePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加系统消息类型", description = "添加系统消息类型")
    @PostMapping()
    public Result<String> addMessageType(@Valid @RequestBody MessageTypeDto dto) {
        messageTypeService.addMessageType(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统消息类型", description = "更新系统消息类型")
    @PutMapping()
    public Result<String> updateMessageType(@Valid @RequestBody MessageTypeDto dto) {
        messageTypeService.updateMessageType(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统消息类型", description = "删除系统消息类型")
    @DeleteMapping()
    public Result<String> deleteMessageType(@RequestBody List<Long> ids) {
        messageTypeService.deleteMessageType(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}