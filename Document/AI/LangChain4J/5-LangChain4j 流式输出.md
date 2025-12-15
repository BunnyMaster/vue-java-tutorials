## 参考文档

1. [LangChain4j官方文档](https://docs.langchain4j.dev/)
2. [Spring WebFlux响应式编程](https://docs.spring.io/spring-framework/reference/web/webflux.html)
3. [Server-Sent Events (SSE) 规范](https://html.spec.whatwg.org/multipage/server-sent-events.html)

## 快速开始

### 1. 引入依赖

**必需依赖：**

```xml
<!-- Spring WebFlux（支持响应式流） -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>

<!-- LangChain4j 核心 -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
    <version>1.8.0-beta15</version>
</dependency>

<!-- LangChain4j Reactor 集成（支持Flux） -->
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-reactor</artifactId>
    <version>1.8.0-beta15</version>
</dependency>
```

**注意：** `langchain4j-spring-boot-starter` 已包含在 `langchain4j-open-ai-spring-boot-starter` 中，无需重复引入。

### 2. 配置文件（application.yml）

>[!TIP] 
>
>为了防止乱码，需要在配置中添加`servlet`相关配置

```yaml
server:
  port: 8080
  
  # 防止响应乱码（Servlet应用需要）
  servlet:
    encoding:
      charset: UTF-8
      enabled: true
      force: true

spring:
  application:
    name: langchain4j-stream-demo

langchain4j:
  open-ai:
    chat-model:
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      api-key: ${ALIYUN_AI_API_KEY:sk-xxx}  # 建议使用环境变量
      model-name: qwen3-max
      log-requests: true
      log-responses: true
    # 流式模型配置（可与chat-model共用配置）
    streaming-chat-model:
      base-url: ${langchain4j.open-ai.chat-model.base-url}
      api-key: ${langchain4j.open-ai.chat-model.api-key}
      model-name: ${langchain4j.open-ai.chat-model.model-name}
      log-requests: true
      log-responses: true

# 可选：启用LangChain4j调试日志
logging:
  level:
    dev.langchain4j: DEBUG
```

### 🔧 3. 配置Bean（多模型场景）

当需要配置多个模型时，需显式声明Bean：

```java
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
```

### 🛠️ 4. 定义AI服务接口

```java
import dev.langchain4j.service.AiService;
import dev.langchain4j.service.AiServiceWiringMode;
import reactor.core.publisher.Flux;

@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "openAiChatModel",           // 对应配置中的chat-model
    streamingChatModel = "stream-qwe" // 对应Bean名称
)
public interface ChatAssistantService {
    
    /**
     * 普通聊天（同步）
     */
    String chat(String prompt);
    
    /**
     * 流式聊天（异步）
     * 返回Flux<String>，每个元素为模型返回的片段
     */
    Flux<String> chatStream(String prompt);
}
```

### 🎯 5. 控制器实现

#### 方案A：使用AiService（推荐）

```java
private final ChatAssistantService chatAssistantService;

private final ChatAssistantExampleService chatAssistantExampleService;

public AdvanceApiController(ChatAssistantService chatAssistantService, ChatAssistantExampleService chatAssistantExampleService) {
    this.chatAssistantService = chatAssistantService;
    this.chatAssistantExampleService = chatAssistantExampleService;
}

@Operation(summary = "使用Qwen流式AI", description = "通过chatAssistantService创建流方式的Qwen")
@GetMapping("stream-chat-3")
public Flux<String> streamChat3(@RequestParam(value = "prompt", defaultValue = "上海有什么好吃") String prompt) {
    return chatAssistantService.chatStream(prompt);
}
```

#### 方案B：手动处理流（更灵活）

```java
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
```

| 组件    | 关键配置                                         | 说明            |
| ------- | ------------------------------------------------ | --------------- |
| 依赖    | spring-boot-starter-webflux, langchain4j-reactor | 必须引入        |
| 配置    | server.servlet.encoding, base-url, api-key       | 防乱码+API配置  |
| Bean    | @Bean(name="xxx") StreamingChatModel             | 多模型时需要    |
| Service | @AiService(streamingChatModel="xxx")             | 指定使用的Bean  |
| 控制器  | produces = "text/event-stream"                   | 设置SSE响应类型 |
| 测试    | curl -N 或 EventSource                           | 验证流式输出    |

## 常见问题

1. **乱码** → 检查编码配置和响应Content-Type
2. **连接超时** → 调整timeout，特别是长文本场景
3. **Bean找不到** → 确保@AiService中streamingChatModel值与Bean名称一致
4. **依赖冲突** → 检查LangChain4j版本一致性📚 总结

