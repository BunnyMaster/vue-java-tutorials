package com.auth.ai.config;

import com.auth.ai.service.ai.ChatAssistantExampleService;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatAssistantExampleServiceConfig {
    @Bean
    public ChatAssistantExampleService chatAssistantExampleService(@Qualifier("qwen") ChatModel chatModel) {
        return AiServices.create(ChatAssistantExampleService.class, chatModel);
    }
}
