package com.auth.ai.service.ai;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "stream-qwen",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface ChatAssistantService {

    /**
     * 普通聊天
     *
     * @param prompt 输入
     * @return 输出
     */
    String chat(String prompt);

    /**
     * 流式聊天
     *
     * @param prompt 输入
     * @return 输出
     */
    Flux<String> chatStream(String prompt);

    /**
     * 聊天，使用MemoryId，使用大模型记忆功能
     *
     * @param userId 用户id
     * @param prompt 输入
     * @return 输出
     */
    Flux<String> chatWithChatMemory(@MemoryId Long userId, @UserMessage String prompt);
}
