package com.auth.ai.config;

import com.auth.ai.respository.RedisChatMemoryStore;
import dev.langchain4j.community.store.embedding.redis.RedisEmbeddingStore;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.loader.ClassPathDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class LangChain4jConfig {

    // private final OpenAiChatModel openAiChatModel;
    //
    // @Bean
    // public ConsultantService consultantService() {
    //     return AiServices.builder(ConsultantService.class)
    //             .chatModel(openAiChatModel)
    //             .build();
    // }

    private final RedisChatMemoryStore redisChatMemoryStore;

    private final EmbeddingModel embeddingModel;

    private final RedisEmbeddingStore redisEmbeddingStore;

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
        // List<Document> documents = FileSystemDocumentLoader.loadDocuments(Paths.get("F:\\文档&学习\\阅读读书\\日记"));

        // 构建向量数据库操作对象
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

}
