package com.auth.ai.controller;

import com.auth.ai.ai.service.ChatMemoryAiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "ChatMemoryController", description = "Ai聊天内存接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-memory")
@RestController
public class ChatMemoryController {

    private final ChatMemoryAiService chatMemoryAiService;

    @Operation(summary = "Ai聊天内存示例", description = "Ai聊天内存示例")
    @GetMapping("sample-chat")
    public Flux<String> sampleChat(String message) {
        return chatMemoryAiService.sampleChat(message);
    }

    @Operation(summary = "Ai记忆会话功能", description = "Ai记忆会话功能，基于memoryId实现")
    @GetMapping("memory-chat")
    public Flux<String> memoryChat(String memoryId, String message) {
        return chatMemoryAiService.memoryChat(memoryId, message);
    }

}
