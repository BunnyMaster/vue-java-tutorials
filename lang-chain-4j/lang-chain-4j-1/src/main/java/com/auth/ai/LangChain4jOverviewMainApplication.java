package com.auth.ai;

import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LangChain4jOverviewMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(LangChain4jOverviewMainApplication.class, args);
    }

    private static void introduction() {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-max")
                .logRequests(true)
                .logResponses(true)
                .build();

        String chat = model.chat("我叫Bunny");
        System.out.println(chat);
    }
}
