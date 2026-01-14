package com.auth.ai.controller;

import com.auth.ai.model.value.LawPromptValue;
import com.auth.ai.service.ai.ChatAssistantExampleService;
import com.auth.ai.service.ai.ChatAssistantService;
import com.auth.ai.service.ai.LawAssistantService;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.chat.response.ChatResponse;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Map;

@Tag(name = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {

    private final ChatAssistantService chatAssistantService;

    private final ChatAssistantExampleService chatAssistantExampleService;

    private final LawAssistantService lawAssistantService;

    private final ChatModel chatModel;

    public AdvanceApiController(ChatAssistantService chatAssistantService,
                                ChatAssistantExampleService chatAssistantExampleService,
                                LawAssistantService lawAssistantService,
                                @Qualifier("qwen") ChatModel chatModel) {
        this.chatAssistantService = chatAssistantService;
        this.chatAssistantExampleService = chatAssistantExampleService;
        this.lawAssistantService = lawAssistantService;
        this.chatModel = chatModel;
    }

    @Operation(summary = "使用AiServices演示")
    @GetMapping("chat")
    public String chat(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatAssistantExampleService.chat(question);
    }

    @Operation(summary = "使用Qwen声明式AI")
    @GetMapping("chat-qwen")
    public String qwen(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatAssistantService.chat(question);
    }

    @Operation(summary = "使用Qwen流式AI", description = "通过chatAssistantService创建流方式的Qwen")
    @GetMapping("stream-chat-3")
    public Flux<String> streamChat3(@RequestParam(value = "prompt", defaultValue = "上海有什么好吃") String prompt) {
        return chatAssistantService.chatStream(prompt);
    }

    @Operation(summary = "使用记忆功能", description = "1、询问上海有什么好吃；2、我刚刚问了什么")
    @GetMapping("chat-memory")
    public Flux<String> chatMemory(@RequestParam(value = "userId", defaultValue = "1") Long userId,
                                   @RequestParam(value = "prompt", defaultValue = "上海有什么好吃") String prompt) {
        return chatAssistantService.chatWithChatMemory(userId, prompt);
    }

    @Operation(summary = "使用LawAssistantService演示")
    @GetMapping("law-chat-annotation")
    public Flux<String> lawChatAnnotation() {
        Flux<String> json1 = lawAssistantService.chat("知识产权是什么", 100, "json");
        Flux<String> json2 = lawAssistantService.chat("上海有什么好吃", 100, "json");
        Flux<String> json3 = lawAssistantService.chat("什么是Java", 100, "json");

        return Flux.merge(json1, json2, json3);
    }

    @Operation(summary = "使用提示词实体类方式回答")
    @GetMapping("law-chat-value")
    public String lawChatValue() {
        LawPromptValue lawPromptValue = new LawPromptValue();
        lawPromptValue.setLegal("知识产权是什么");
        lawPromptValue.setQuestion("上海有什么好吃");
        return lawAssistantService.chat(lawPromptValue);
    }

    @Operation(summary = "单个参数使用{{it}}进行表示")
    @GetMapping("law-chat-it")
    public String lawChatIt() {
        // 创建包含角色和问题信息的上下文映射
        Map<String, Object> it = Map.of("it", "外科医生", "question", "累了");

        // 构建提示词模板，使用占位符来动态替换内容
        PromptTemplate promptTemplate = PromptTemplate.from("你是一个{{it}}助手，{{question}}怎么办");
        // 应用上下文数据到模板，生成具体的提示词
        Prompt prompt = promptTemplate.apply(it);

        // 将提示词转换为用户消息格式
        UserMessage userMessage = prompt.toUserMessage();
        // 调用聊天模型进行对话，获取AI响应
        ChatResponse chatResponse = chatModel.chat(userMessage);

        // 返回AI响应中的文本内容
        return chatResponse.aiMessage().text();
    }

    @Operation(summary = "使用Service占位符")
    @GetMapping("law-chat-service-it")
    public Flux<String> lawChatServiceIt() {
        return lawAssistantService.chatByIt("外科医生", "累了");
    }
}

