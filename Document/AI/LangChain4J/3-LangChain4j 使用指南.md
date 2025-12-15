## 概述

LangChain4j 是一个基于 Java 的 AI 应用开发框架，提供统一接口访问多种大语言模型，简化 AI 功能的集成过程。

## 环境要求

- **Java**: 17 或更高版本
- **Spring Boot**: 3.x（如使用 Spring Boot 集成）
- **LangChain4j**: 1.0.1-beta6（建议版本）

## 快速开始

### 依赖配置

#### 原生集成方式

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j</artifactId>
    <version>1.0.1-beta6</version>
</dependency>
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-open-ai</artifactId>
    <version>1.0.1-beta6</version>
</dependency>
```

#### Spring Boot 集成方式

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-bom</artifactId>
            <version>1.8.0</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>

<dependencies>
    <!-- 基础功能使用 -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-open-ai-spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-community-dashscope-spring-boot-starter</artifactId>
    </dependency>
    <!-- 高阶服务使用 AI Service -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-spring-boot-starter</artifactId>
    </dependency>
    <!-- Flux -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-reactor</artifactId>
    </dependency>
    <!-- 引入 向量存储 依赖 -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-community-redis-spring-boot-starter</artifactId>
        <version>1.8.0-beta15</version>
    </dependency>
    <!-- 向量数据库 -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-easy-rag</artifactId>
    </dependency>
    <!-- 解析pdf -->
    <dependency>
        <groupId>dev.langchain4j</groupId>
        <artifactId>langchain4j-document-parser-apache-pdfbox</artifactId>
    </dependency>
</dependencies>
```

### 环境变量配置

**重要提示**：
- 环境变量在 IDE 启动时读取，配置后需重启 IDE 生效
- 支持在配置文件中引用环境变量

**application.yml 配置示例**：

```yaml
langchain4j:
  open-ai:
    chat-model:
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      api-key: ${ALIYUN-AI-API-KEY}
      model-name: "qwen3-max"
      log-requests: true
      log-responses: true
```

### 基础用法

#### 编程式调用

```java
public class LangChain4jMainApplication {
    public static void main(String[] args) {
        OpenAiChatModel model = OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-max")
                .logRequests(true)
                .logResponses(true)
                .build();

        String response = model.chat("我叫Bunny");
        System.out.println(response);
    }
}
```

#### Spring Boot 集成调用

```java
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatController {
    
    private final OpenAiChatModel openAiChatModel;
    
    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return openAiChatModel.chat(message);
    }
}
```

## 多模型配置与使用

### 环境变量配置

- `ALIYUN-AI-API-KEY`：通义千问 API 密钥
- `DEEPSEEK-AI-API-KEY`：DeepSeek API 密钥

### 模型特性对比

- **通义千问**：支持长文本理解，适合复杂对话场景
- **DeepSeek**：推理能力强，适合逻辑分析和代码生成

### 多模型配置

```java
@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
    @Primary
    public OpenAiChatModel chatModelQwen() {
        return OpenAiChatModel.builder()
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
                .modelName("qwen3-max")
                .logRequests(true)
                .logResponses(true)
                .build();
    }

    @Bean(name = "deepseek")
    public OpenAiChatModel chatModelDeepSeek() {
        return OpenAiChatModel.builder()
                .apiKey(System.getenv("DEEPSEEK-AI-API-KEY"))
                .modelName("deepseek-chat")
                .baseUrl("https://api.deepseek.com/v1")
                .logRequests(true)
                .logResponses(true)
                .build();
    }
}
```

### 多模型调用

**重要提示**：官方推荐使用 `ChatModel` 接口而非 `LanguageModel`，以获得更好的对话体验和功能支持。

```java
@Tag(name = "HelloChatController", description = "多模型调用示例")
@RequestMapping("/api/hello")
@RestController
public class HelloChatController {

    private final ChatModel chatModelQwen;
    private final ChatModel chatModelDeepSeek;

    public HelloChatController(@Qualifier("qwen") ChatModel chatModelQwen,
                               @Qualifier("deepseek") ChatModel chatModelDeepSeek) {
        this.chatModelQwen = chatModelQwen;
        this.chatModelDeepSeek = chatModelDeepSeek;
    }

    @Operation(summary = "调用通义千问模型")
    @GetMapping("/qwen")
    public String qwen(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatModelQwen.chat(question);
    }

    @Operation(summary = "调用DeepSeek模型")
    @GetMapping("/deepseek")
    public String deepseek(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatModelDeepSeek.chat(question);
    }
}
```

## 声明式 AI 服务

### 优势特点

1. **声明式编程**：通过接口注解即可实现 AI 功能
2. **类型安全**：编译时检查方法签名
3. **易于测试**：可以轻松创建 Mock 实现
4. **代码简洁**：无需编写具体的实现类

### 配置说明

**@AiService 注解参数详解**：

- **wiringMode**:
  - `EXPLICIT`: 显式指定使用的 ChatModel Bean
  - `AUTOMATIC`: 自动装配默认的 ChatModel

- **chatModel**:
  - 指定要使用的 ChatModel Bean 名称
  - 需要与 Spring 容器中的 Bean 名称一致

### AI 服务接口定义

```java
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(
    wiringMode = AiServiceWiringMode.EXPLICIT,
    chatModel = "openAiChatModel"
)
public interface ChatAssistantService {

    /**
     * 普通对话
     *
     * @param prompt 用户输入
     * @return AI 回复
     */
    String chat(String prompt);
}
```

### 控制器实现

```java
@Tag(name = "declarative-ai-service-controller", description = "声明式AI服务")
@RequestMapping("/api/ai/declarative")
@RestController
public class DeclarativeAiServiceController {

    private final ChatAssistantService declarativeAiService;

    public DeclarativeAiServiceController(ChatAssistantService declarativeAiService) {
        this.declarativeAiService = declarativeAiService;
    }

    @Operation(summary = "使用通义千问声明式AI服务")
    @GetMapping("/chat-qwen")
    public String chatWithQwen(
        @RequestParam(value = "question", defaultValue = "你是谁") String question
    ) {
        return declarativeAiService.chat(question);
    }
}
```

## Token 计算与用量统计

该功能适用于基于使用量向客户收费的业务场景。通过 `ChatModel` 可以获取当前会话的 Token 使用量，返回类型为 `TokenUsage`。

### 使用示例

```java
@Operation(summary = "用量计算Api演示")
@GetMapping("calculation")
public String calculation(@RequestParam(value = "prompt", defaultValue = "你是谁") String prompt) {
    ChatResponse chatResponse = chatModel.chat(UserMessage.from(prompt));
    String result = chatResponse.aiMessage().text();

    // Token用量计算
    TokenUsage tokenUsage = chatResponse.tokenUsage();
    return result + "\t\n" + tokenUsage;
}
```

### 返回结果示例

```
我是通义千问，阿里巴巴集团旗下的超大规模语言模型。我能够回答问题、创作文字，比如写故事、写公文、写邮件、写剧本、逻辑推理、编程等等，还能表达观点，玩游戏等。如果你有任何问题或需要帮助，欢迎随时告诉我！    
OpenAiTokenUsage { inputTokenCount = 10, inputTokensDetails = OpenAiTokenUsage.InputTokensDetails { cachedTokens = 0 }, outputTokenCount = 60, outputTokensDetails = null, totalTokenCount = 70 }
```

## 完整配置示例

### 应用配置文件

```yaml
server:
  port: 8081

spring:
  data:
    redis:
      host: 192.168.2.19
      port: 6379
      password: "Dev1234!"
      database: 0
  datasource:
    username: root
    password: "Dev1234!"
    url: jdbc:mysql://192.168.2.19:3306/volunteer?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    driver-class-name: com.mysql.cj.jdbc.Driver

langchain4j:
  open-ai:
    chat-model:
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      api-key: ${ALIYUN-AI-API-KEY}
      model-name: "qwen3-max"
      log-requests: true
      log-responses: true
    streaming-chat-model:
      base-url: ${langchain4j.open-ai.chat-model.base-url}
      api-key: ${langchain4j.open-ai.chat-model.api-key}
      model-name: "qwen3-max"
      log-requests: true
      log-responses: true
    embedding-model:
      base-url: ${langchain4j.open-ai.chat-model.base-url}
      api-key: ${langchain4j.open-ai.chat-model.api-key}
      model-name: "text-embedding-v4"
      log-requests: true
      log-responses: true
      max-segments-per-batch: 10

logging:
  level:
    root: info
    com.baomidou.mybatisplus: debug
```

### 完整配置类

```java
@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    private final RedisChatMemoryStore redisChatMemoryStore;
    private final EmbeddingModel embeddingModel;

    @Bean
    public ChatMemory chatMemory() {
        return MessageWindowChatMemory.builder()
                .maxMessages(20)
                .build();
    }

    @Bean
    public ChatMemoryProvider chatMemoryProvider() {
        return memoryId -> MessageWindowChatMemory.builder()
                .id(memoryId)
                .maxMessages(20)
                .chatMemoryStore(redisChatMemoryStore)
                .build();
    }

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        // 加载文档内存
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("static/content", new ApachePdfBoxDocumentParser());

        // 构建向量数据库操作对象
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

        // 构建文档分割器对象
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 100);

        // 构建 EmbeddingStoreIngestor 对象完成文本数据切割、向量化、存储
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .build();
        ingestor.ingest(documents);

        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> store) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .minScore(0.5)
                .maxResults(3)
                .embeddingModel(embeddingModel)
                .build();
    }
}
```

