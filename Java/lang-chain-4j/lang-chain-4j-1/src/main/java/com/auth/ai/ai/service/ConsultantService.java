package com.auth.ai.ai.service;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        // 会自动获取配置中的模型
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel"
)
// @AiService
public interface ConsultantService {

    /**
     * 阻塞式回答
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    String chat(String message);

    /**
     * 流方式回答
     *
     * @param message 消息
     * @return 聊天流
     */
    Flux<String> chatStream(String message);

    /**
     * 使用注解方式获取系统消息
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage("你是我的文档助手小兔子,可以帮我整理很多日常文档和日记.")
    Flux<String> chatStreamByMessage1(String message);

    /**
     * 从资源中获取系统消息
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> chatStreamByMessage2(String message);

    /**
     * 使用占位符获取用户消息
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    @UserMessage("你是我的文档助手小兔子,可以帮我整理很多日常文档和日记{{it}}")
    Flux<String> chatStreamByMessageByPlaceholder(String message);

    /**
     * 使用占位符获取用户消息
     *
     * @param message 聊天内容
     * @return 聊天结果
     */
    @UserMessage("你是我的文档助手小兔子,可以帮我整理很多日常文档和日记{{msg}}")
    Flux<String> chatStreamByAnnotation(@V("msg") String message);

}
