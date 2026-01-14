## RAG 知识库

### 文档处理组件

#### 文档加载器

支持多种文档来源：

- `AmazonS3DocumentLoader` - Amazon S3
- `AzureBlobStorageDocumentLoader` - Azure Blob Storage  
- `GoogleCloudStorageDocumentLoader` - Google Cloud Storage
- `ClassPathDocumentLoader` - 类路径
- `FileSystemDocumentLoader` - 文件系统
- `UrlDocumentLoader` - URL
- `GitHubDocumentLoader` - GitHub
- `SeleniumDocumentLoader` - 网页抓取
- `TencentCosDocumentLoader` - 腾讯云 COS

#### 文档解析器

- `TextDocumentParser` - 文本文件
- `ApacheTikaDocumentParser` - 多种格式（Tika）
- `ApachePoiDocumentParser` - Office 文档
- `ApachePdfBoxDocumentParser` - PDF 文档

#### 文档分割器

- `DocumentByParagraphSplitter` - 按段落分割
- `DocumentByLineSplitter` - 按行分割  
- `DocumentBySentenceSplitter` - 按句子分割
- `DocumentByWordSplitter` - 按词分割
- `DocumentByCharacterSplitter` - 按字符数分割
- `DocumentByRegexSplitter` - 按正则表达式分割
- `DocumentSplitters.recursive()` - 递归分割（默认）

### 基础使用

#### 引入依赖

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-easy-rag</artifactId>
    <version>1.8.0-beta15</version>
</dependency>
```

#### 配置向量数据库

**基础配置**

```java
@Configuration
public class LangChain4jConfig {

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        // 加载文档
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("static/content");
        // 或者使用文件系统路径
        // List<Document> documents = FileSystemDocumentLoader.loadDocuments(
        //     Paths.get("F:\\文档&学习\\阅读读书\\日记"));

        // 构建向量数据库
        InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
        
        // 构建文档处理管道
        EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
                .embeddingStore(embeddingStore)
                .build();
        ingestor.ingest(documents);

        return embeddingStore;
    }

    @Bean
    public ContentRetriever contentRetriever(EmbeddingStore<TextSegment> store) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(store)
                .minScore(0.5)    // 最小相似度分数
                .maxResults(3)    // 最大返回结果数
                .build();
    }
}
```

**使用文档解析器**

```java
@Bean
public EmbeddingStore<TextSegment> embeddingStore() {
    List<Document> documents = ClassPathDocumentLoader.loadDocuments(
        "static/content", new ApachePdfBoxDocumentParser());
    
    InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();
    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .embeddingStore(embeddingStore)
            .build();
    ingestor.ingest(documents);

    return embeddingStore;
}
```

**使用文档分割器**

```java
@Bean
public EmbeddingStore<TextSegment> embeddingStore() {
    List<Document> documents = ClassPathDocumentLoader.loadDocuments(
        "static/content", new ApachePdfBoxDocumentParser());

    InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    // 配置文档分割器（500字符块，100字符重叠）
    DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 100);

    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            .embeddingStore(embeddingStore)
            .documentSplitter(documentSplitter)
            .build();
    ingestor.ingest(documents);

    return embeddingStore;
}
```

#### 配置 AI Service

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        contentRetriever = "contentRetriever"  // 配置向量检索
)
public interface ChatMemoryAiService {

    /**
     * AI聊天记忆示例（带知识库检索）
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> sampleChat(String message);

    /**
     * AI聊天记忆功能示例（带知识库检索）
     * @param memoryId 会话记忆ID
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> memoryChat(@MemoryId String memoryId, @UserMessage String message);
}
```

### 配置向量模型

可以使用第三方的向量模型，比如阿里的`text-embedding-v4`之后在`embedding-model`中进行配置

```yaml
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
```

在配置类中添加`private final EmbeddingModel embeddingModel;`，分别在`embeddingStore`和`contentRetriever`添加`.embeddingModel(embeddingModel)`

```java
@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    private final RedisChatMemoryStore redisChatMemoryStore;

    private final EmbeddingModel embeddingModel;

    @Bean
    public EmbeddingStore<TextSegment> embeddingStore() {
        // 加载文档内存
        List<Document> documents = ClassPathDocumentLoader.loadDocuments("static/content", new ApachePdfBoxDocumentParser());
        // List<Document> documents = FileSystemDocumentLoader.loadDocuments(Paths.get("F:\\文档&学习\\阅读读书\\日记"));

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

### Redis存储向量模型

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-community-redis-spring-boot-starter</artifactId>
    <version>1.8.0-beta15</version>
</dependency>
```

需要更改下面几个地方`community:`中添加redis相关。

> [!IMPORTANT]
>
> 如果使用的是阿里的向量模型需要在最后添加`max-segments-per-batch`，阿里的模型有限制一次只能是10个

```yaml
langchain4j:
  community:
    redis:
      host: 192.168.2.19
      port: 16379
      password: "Dev1234!"
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
      # 批量处理最多一次发送10个
      max-segments-per-batch: 10
```

将配置`EmbeddingStore`修改成`RedisEmbeddingStore`

```java
private final RedisEmbeddingStore redisEmbeddingStore;

@Bean
public EmbeddingStore<TextSegment> embeddingStore() {
    // 加载文档内存
    List<Document> documents = ClassPathDocumentLoader.loadDocuments("static/content", new ApachePdfBoxDocumentParser());
    // List<Document> documents = FileSystemDocumentLoader.loadDocuments(Paths.get("F:\\文档&学习\\阅读读书\\日记"));

    // // 构建向量数据库操作对象
    // InMemoryEmbeddingStore<TextSegment> embeddingStore = new InMemoryEmbeddingStore<>();

    // 构建文档分割器对象
    DocumentSplitter documentSplitter = DocumentSplitters.recursive(500, 100);

    // 构建 EmbeddingStoreIngestor 对象完成文本数据切割、向量化、存储
    EmbeddingStoreIngestor ingestor = EmbeddingStoreIngestor.builder()
            // .embeddingStore(embeddingStore)
            .embeddingStore(redisEmbeddingStore)
            .documentSplitter(documentSplitter)
            .embeddingModel(embeddingModel)
            .build();
    ingestor.ingest(documents);

    return redisEmbeddingStore;
}

@Bean
public ContentRetriever contentRetriever(/* EmbeddingStore<TextSegment> store */) {
    return EmbeddingStoreContentRetriever.builder()
            .embeddingStore(redisEmbeddingStore)
            .minScore(0.5)
            .maxResults(3)
            .embeddingModel(embeddingModel)
            .build();
}
```

