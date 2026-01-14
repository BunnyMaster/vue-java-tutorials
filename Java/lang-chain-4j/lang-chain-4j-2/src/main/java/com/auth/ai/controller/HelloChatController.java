package com.auth.ai.controller;

import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.StreamingChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.chat.response.StreamingChatResponseHandler;
import dev.langchain4j.model.output.TokenUsage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Tag(name = "初始化示例")
@RequestMapping("/api/hello")
@RestController
public class HelloChatController {

    // 官网推荐 ChatModel 而不是 LanguageModel
    private final ChatModel chatModelQwen;

    private final ChatModel chatModelDeepSeek;

    private final StreamingChatModel streamingChatModel;

    public HelloChatController(@Qualifier("qwen") ChatModel chatModelQwen,
                               @Qualifier("deepseek") ChatModel chatModelDeepSeek,
                               @Qualifier("stream-qwen") StreamingChatModel streamingChatModel) {
        this.chatModelQwen = chatModelQwen;
        this.chatModelDeepSeek = chatModelDeepSeek;
        this.streamingChatModel = streamingChatModel;
    }

    @Operation(summary = "使用Qwen")
    @GetMapping("qwen")
    public String qwen(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatModelQwen.chat(question);
    }

    @Operation(summary = "使用DeepSeek")
    @GetMapping("deepseek")
    public String deepseek(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatModelDeepSeek.chat(question);
    }

    @Operation(summary = "用量计算Api演示", description = "测量AI用量Token算服务")
    @GetMapping("calculation")
    public String chatMeasurementCalculation(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt) {
        ChatResponse chatResponse = chatModelQwen.chat(UserMessage.from(prompt));
        String result = chatResponse.aiMessage().text();

        // Token用量计算
        TokenUsage tokenUsage = chatResponse.tokenUsage();
        return result + "\t\n" + tokenUsage;
    }

    @Operation(summary = "流式聊天", description = "通过streamingChatModel创建流方式的聊天")
    @GetMapping("stream-chat-1")
    public Flux<String> streamChat1(@RequestParam(value = "prompt", defaultValue = "江阴有什么好吃的") String prompt) {
        return Flux.create(emitter -> streamingChatModel.chat(prompt, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                emitter.next(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                emitter.complete();
            }

            @Override
            public void onError(Throwable throwable) {
                emitter.error(throwable);
            }
        }));
    }

    @Operation(summary = "流式聊天", description = "通过streamingChatModel创建流方式的聊天")
    @GetMapping("stream-chat-2")
    public void streamChat2(@RequestParam(value = "prompt", defaultValue = "镇江有什么好吃的") String prompt) {
        streamingChatModel.chat(prompt, new StreamingChatResponseHandler() {
            @Override
            public void onPartialResponse(String partialResponse) {
                System.out.println(partialResponse);
            }

            @Override
            public void onCompleteResponse(ChatResponse chatResponse) {
                System.out.println("---response over" + chatResponse);
            }

            @Override
            public void onError(Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        });
    }
}
