package com.auth.ai.controller;

import com.auth.ai.ai.service.ConsultantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "ChatController", description = "Ai聊天接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ChatController {

    private final ConsultantService consultantService;

    @Operation(summary = "Chat with AI example", description = "Ai聊天示例")
    @GetMapping("chat-example")
    public String chat(String message) {
        return consultantService.chat(message);
    }

    @Operation(summary = "Chat with AI bean", description = "Ai聊天bean")
    @GetMapping("chat-bean")
    public String chatBean(String message) {
        return consultantService.chat(message);
    }

    @Operation(summary = "Chat with AI stream", description = "Ai聊天流")
    @GetMapping(value = "chat-stream1", produces = "text/plain;charset=UTF-8")
    public Flux<String> chatStream(String message) {
        return consultantService.chatStream(message);
    }

    @Operation(summary = "Chat with AI stream", description = "Ai聊天流")
    @GetMapping(value = "/chat-stream2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStream2(String message) {
        return consultantService.chatStream(message);
    }

    @Operation(summary = "Chat with AI stream message1", description = "Ai聊天流message1")
    @GetMapping(value = "/chat-stream-message1", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreamMessage1(String message) {
        return consultantService.chatStreamByMessage1(message);
    }

    @Operation(summary = "Chat with AI stream message2", description = "Ai聊天流message2")
    @GetMapping(value = "/chat-stream-message2", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatStreamMessage2(String message) {
        return consultantService.chatStreamByMessage2(message);
    }

    @Operation(summary = "使用占位符获取用户消息", description = "使用占位符获取用户消息")
    @GetMapping("chat-stream/message-placeholder")
    public Flux<String> chatStreamMessagePlaceholder(String message) {
        return consultantService.chatStreamByMessageByPlaceholder(message);
    }

    @Operation(summary = "使用注解获取用户消息", description = "使用注解获取用户消息")
    @GetMapping("chat-stream/message-annotation")
    public Flux<String> chatStreamMessageAnnotation(String message) {
        return consultantService.chatStreamByAnnotation(message);
    }

}
