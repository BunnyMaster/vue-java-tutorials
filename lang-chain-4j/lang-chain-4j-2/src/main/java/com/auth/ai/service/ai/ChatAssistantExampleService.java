package com.auth.ai.service.ai;

public interface ChatAssistantExampleService {

    /**
     * 普通聊天，使用AiServices
     *
     * @param prompt 提示词
     * @return 输出
     */
    String chat(String prompt);
}
