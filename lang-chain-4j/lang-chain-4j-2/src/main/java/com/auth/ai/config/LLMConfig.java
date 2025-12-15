package com.auth.ai.config;

import com.auth.ai.listener.QwenChatModelListener;
import com.auth.ai.respository.RedisChatMemoryStore;
import dev.langchain4j.community.model.dashscope.WanxImageModel;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class LLMConfig {

    private final RedisChatMemoryStore redisChatMemoryStore;

    @Bean(name = "qwen")
    public OpenAiChatModel chatModelQwen() {
        return OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-max")
                .logRequests(true)
                .logResponses(true)
                .listeners(List.of(new QwenChatModelListener()))
                .maxRetries(3)
                .timeout(Duration.ofSeconds(3000L))
                .build();
    }

    @Bean(name = "qwen3-vl-flash")
    public OpenAiChatModel openAiChatModelQWenVLFlash() {
        return OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-vl-flash")
                .logRequests(true)
                .logResponses(true)
                .listeners(List.of(new QwenChatModelListener()))
                .maxRetries(3)
                .timeout(Duration.ofSeconds(3000L))
                .build();
    }

    @Bean
    public WanxImageModel wanxImageModel() {
        return WanxImageModel.builder()
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("wan2.5-t2i-preview")
                .build();
    }

    @Bean(name = "deepseek")
    public OpenAiChatModel chatModelDeepSeek() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("Deepseek-AI-API-KEY"))
                .modelName("deepseek-chat")
                .baseUrl("https://api.deepseek.com/v1")
                .logResponses(true)
                .logResponses(true)
                .build();
    }

    @Bean(name = "stream-qwen")
    public StreamingChatModel streamingChatModel() {
        return OpenAiStreamingChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-max")
                .logRequests(true)
                .logResponses(true)
                .listeners(List.of(new QwenChatModelListener()))
                .timeout(Duration.ofSeconds(3000L))
                .build();
    }

    /**
     * 设置大模型记忆功能
     *
     * @return ChatMemoryProvider
     */
    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(redisChatMemoryStore)
                .build();
    }
}
