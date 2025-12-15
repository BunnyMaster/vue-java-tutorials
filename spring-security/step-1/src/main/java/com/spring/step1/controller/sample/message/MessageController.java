package com.spring.step1.controller.sample.message;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spring.step1.bean.dto.system.MessageDto;
import com.spring.step1.bean.entity.MessageEntity;
import com.spring.step1.bean.vo.MessageVo;
import com.spring.step1.bean.vo.result.PageResult;
import com.spring.step1.bean.vo.result.Result;
import com.spring.step1.bean.vo.result.ResultCodeEnum;
import com.spring.step1.service.sample.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 系统消息 前端控制器
 * </p>
 *
 * @author Bunny
 * @since 2025-07-10 14:16:49
 */
@Tag(name = "系统消息", description = "系统消息相关接口")
@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @Operation(summary = "分页查询系统消息", description = "分页系统消息")
    @GetMapping("{page}/{limit}")
    public Result<PageResult<MessageVo>> getMessagePage(
            @Parameter(name = "page", description = "当前页", required = true)
            @PathVariable("page") Integer page,
            @Parameter(name = "limit", description = "每页记录数", required = true)
            @PathVariable("limit") Integer limit,
            MessageDto dto) {
        Page<MessageEntity> pageParams = new Page<>(page, limit);
        PageResult<MessageVo> pageResult = messageService.getMessagePage(pageParams, dto);
        return Result.success(pageResult);
    }

    @Operation(summary = "添加系统消息", description = "添加系统消息")
    @PostMapping()
    public Result<String> addMessage(@Valid @RequestBody MessageDto dto) {
        messageService.addMessage(dto);
        return Result.success(ResultCodeEnum.ADD_SUCCESS);
    }

    @Operation(summary = "更新系统消息", description = "更新系统消息")
    @PutMapping()
    public Result<String> updateMessage(@Valid @RequestBody MessageDto dto) {
        messageService.updateMessage(dto);
        return Result.success(ResultCodeEnum.UPDATE_SUCCESS);
    }

    @Operation(summary = "删除系统消息", description = "删除系统消息")
    @DeleteMapping()
    public Result<String> deleteMessage(@RequestBody List<Long> ids) {
        messageService.deleteMessage(ids);
        return Result.success(ResultCodeEnum.DELETE_SUCCESS);
    }
}