package com.auth.ai.ai.service;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        // 配置会话记忆功能
        // chatMemory = "chatMemory"
        // 配置会话记忆提供对象
        chatMemoryProvider = "chatMemoryProvider",
        // 配置向量数据库检索对象
        contentRetriever = "contentRetriever",
        tools = "reservationTool"
)
public interface ChatMemoryAiService {

    /**
     * Ai聊天内存示例
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> sampleChat(String message);

    /**
     * Ai聊天记忆功能示例
     *
     * @param memoryId 会话记忆ID
     * @param message  聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> memoryChat(@MemoryId String memoryId, @UserMessage String message);

}
