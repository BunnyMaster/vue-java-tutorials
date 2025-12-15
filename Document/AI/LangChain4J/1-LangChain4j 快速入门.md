## LangChain4j 简介

LangChain4j 是一个基于 Java 的 AI 应用开发框架，提供统一接口访问多种大语言模型，简化 AI 功能的集成过程。

## 环境准备

- **Java**: 17 或更高版本
- **Spring Boot**: 3.x（如使用 Spring Boot 集成）
- **LangChain4j**: 1.0.1-beta6（建议版本）

### Maven配置

#### 原生集成方式

可以不用依赖Spring环境使用

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

> [!NOTE]
>
> 官网：https://docs.langchain4j.dev/get-started

集成Spring环境使用

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

## 第一个 AI 应用

### 单模型基础配置

- 环境变量在 IDE 启动时读取，配置后需重启 IDE 生效
- 支持在配置文件中引用环境变量

**application.yml 配置示例**：

如果不涉及更多的自定义内容，直接地下面进行配置即可

- `base-url`：请求地址
- `api-key`：使用`${}`从环境变量中读取
- 输出日志用这两个：`log-requests`、`log-responses`

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

### 编程式调用示例

在main的启动函数中进行测试，也可以在构建中添加`returnThinking`可以返回DeepkSeek思考内容

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

### Spring Boot 集成示例

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

## 多模型配置

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

> [!IMPORTANT] 
>
> 官方推荐使用 `ChatModel` 接口而非 `LanguageModel`，以获得更好的对话体验和功能支持。

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

## 基础概念总结

### ChatModel 接口

`ChatModel`属于彽阶API，用于手动构建，通常会使用后面的高阶方法——`@AiService`

```java
ChatModel model = OpenAiChatModel.builder()
    .apiKey("YOUR_API_KEY")
    // more configuration parameters here ...
    .build()
```

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
