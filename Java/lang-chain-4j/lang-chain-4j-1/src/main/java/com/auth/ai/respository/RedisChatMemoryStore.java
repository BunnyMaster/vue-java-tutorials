package com.auth.ai.respository;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.ChatMessageDeserializer;
import dev.langchain4j.data.message.ChatMessageSerializer;
import dev.langchain4j.store.memory.chat.ChatMemoryStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Slf4j
@Repository
public class RedisChatMemoryStore implements ChatMemoryStore {

    private final StringRedisTemplate redisTemplate;

    @Override
    public List<ChatMessage> getMessages(Object memoryId) {
        String json = redisTemplate.opsForValue().get(memoryId);
        // log.info("获取会话消息: {},消息内容：{}", memoryId, json);
        return ChatMessageDeserializer.messagesFromJson(json);
    }

    @Override
    public void updateMessages(@NotNull Object memoryId, List<ChatMessage> list) {
        // 更新会话消息
        String messagesToJson = ChatMessageSerializer.messagesToJson(list);
        redisTemplate.opsForValue().set(memoryId.toString(), messagesToJson, 1, TimeUnit.DAYS);

        // log.info("更新会话消息: {},消息内容：{}", memoryId, messagesToJson);
    }

    @Override
    public void deleteMessages(@NotNull Object memoryId) {
        redisTemplate.delete(memoryId.toString());
        // log.info("删除会话消息: {}", memoryId);
    }
}
