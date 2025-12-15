package com.auth.ai.service.ai;

import com.auth.ai.model.value.LawPromptValue;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwen3-vl-flash",
        streamingChatModel = "stream-qwen",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LawAssistantService {

    /**
     * 使用注解方式进行回答
     *
     * @param question 问题
     * @param length   回答长度
     * @param format   输出格式
     * @return 回答
     */
    @UserMessage("请回答以下法律问题:{{question}},字数控制在{{length}}以内，以{{format}}格式输出")
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    Flux<String> chat(@V("question") String question, @V("length") int length, @V("format") String format);

    /**
     * 使用参数对象进行回答
     *
     * @param lawPrompt 参数对象
     * @return 回答
     */
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    String chat(LawPromptValue lawPrompt);

    /**
     * 使用占位符进行回答
     *
     * @param it       角色
     * @param question 问题
     * @return 回答
     */
    @SystemMessage("你是一位专业的{{it}}，只回答与{{it}}相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答{{it}}相关的问题。'")
    Flux<String> chatByIt(@V("it") String it, String question);
}
