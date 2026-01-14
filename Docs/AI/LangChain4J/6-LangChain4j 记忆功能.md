## 大模型记忆功能

LangChain4j 的**记忆功能（Memory）** 允许大模型在多轮对话中保持上下文记忆，基于用户ID（或其他标识）区分不同会话的记忆存储。  
⚠️ **注意**：默认配置下记忆仅保存在内存中，重启应用后记忆会丢失。如需持久化，需配置外部存储（如Redis、数据库等）。

### 注入Bean

```java
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
            // .chatMemoryStore(redisChatMemoryStore)
            .build();
}
```

### 定义带记忆的 AI 服务接口

使用 `@MemoryId` 标注用户标识参数，系统会自动关联该用户的对话记忆。

```java
@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "openAiChatModel",
    streamingChatModel = "stream-qwen",
    chatMemoryProvider = "chatMemoryProvider"  // 指定记忆提供者
)
public interface ChatAssistantService {
    
    /**
     * 支持记忆的对话接口
     * @param userId 用户ID，作为记忆标识
     * @param prompt 用户输入
     * @return 模型输出（流式）
     */
    Flux<String> chatWithChatMemory(@MemoryId Long userId, @UserMessage String prompt);
}
```

###  在 Controller 中调用

```java
@Tag(name = "高级AI服务")
@RequestMapping("/api/ai/advance")
@RestController
public class AdvanceApiController {

    private final ChatAssistantService chatAssistantService;

    public AdvanceApiController(ChatAssistantService chatAssistantService) {
        this.chatAssistantService = chatAssistantService;
    }

    @Operation(summary = "使用记忆功能进行对话")
    @PostMapping("/chat-with-memory")
    public Flux<String> chatWithMemory(@RequestParam Long userId, @RequestParam String message) {
        return chatAssistantService.chatWithChatMemory(userId, message);
    }
}
```

## 基础会话记忆

> [!NOTE]
> 注意：这种方式所有用户共享同一个会话记忆，可能导致不同用户间的对话混淆。

### 配置说明

```java
@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }
}
```

在 `AiService` 中指定会话记忆：

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        chatMemory = "chatMemory"
)
public interface ChatMemoryAiService {
    /**
     * AI聊天记忆示例
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> sampleChat(String message);
}
```

### 接口调用

```java
@Tag(name = "ChatMemoryController", description = "AI聊天记忆接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-memory")
@RestController
public class ChatMemoryController {

    private final ChatMemoryAiService chatMemoryAiService;

    @Operation(summary = "AI聊天记忆示例", description = "AI聊天记忆示例")
    @GetMapping("sample-chat")
    public Flux<String> sampleChat(String message) {
        return chatMemoryAiService.sampleChat(message);
    }
}
```

## 记忆隔离

### 配置说明

> [!NOTE]
> 这种方式在应用重启后会话记忆会丢失。

#### **Bean 配置**

```java
@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .build();
    }
}
```

#### **Service 配置**

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface ChatMemoryAiService {
    /**
     * AI聊天记忆功能示例
     * @param memoryId 会话记忆ID
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> memoryChat(@MemoryId String memoryId, @UserMessage String message);
}
```

### 接口调用

```java
@Tag(name = "ChatMemoryController", description = "AI聊天记忆接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-memory")
@RestController
public class ChatMemoryController {

    private final ChatMemoryAiService chatMemoryAiService;
    
    @Operation(summary = "AI记忆会话功能", description = "AI记忆会话功能，基于memoryId实现")
    @GetMapping("memory-chat")
    public Flux<String> memoryChat(String memoryId, String message) {
        return chatMemoryAiService.memoryChat(memoryId, message);
    }
}
```

## 持久化会话记忆

为了解决应用重启后会话记忆丢失的问题，可以使用 Redis 进行持久化存储。

### 配置说明

#### **引入依赖**

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

#### **Redis 配置**

```yaml
spring:
  data:
    redis:
      host: 192.168.2.19
      port: 6379
      password: "Dev1234!"
```

### 实现 ChatMemoryStore 接口

```java
@RequiredArgsConstructor
@Slf4j
@Repository
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = redisTemplate.opsForValue().get(memoryId.toString());
        log.info("获取会话消息: {}, 消息内容：{}", memoryId, json);
        if (json == null) {
            return new ArrayList<>();
        }
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(@NotNull Object memoryId, List<ChatMessage> messages) {
        String messagesToJson = ChatMessageSerializer.messagesToJson(messages);
        redisTemplate.opsForValue().set(memoryId.toString(), messagesToJson, 1, TimeUnit.DAYS);
        log.info("更新会话消息: {}, 消息内容：{}", memoryId, messagesToJson);
    }

    @Override
    public void deleteMessages(@NotNull Object memoryId) {
        redisTemplate.delete(memoryId.toString());
        log.info("删除会话消息: {}", memoryId);
    }
}
```

### 配置使用

```java
@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    private final RedisChatMemoryStore redisChatMemoryStore;

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(redisChatMemoryStore)
                .build();
    }
}
```
