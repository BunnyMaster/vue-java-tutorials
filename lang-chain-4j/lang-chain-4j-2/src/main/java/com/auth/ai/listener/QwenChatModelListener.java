package com.auth.ai.listener;

import cn.hutool.core.util.IdUtil;
import dev.langchain4j.model.chat.listener.ChatModelErrorContext;
import dev.langchain4j.model.chat.listener.ChatModelListener;
import dev.langchain4j.model.chat.listener.ChatModelRequestContext;
import dev.langchain4j.model.chat.listener.ChatModelResponseContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class QwenChatModelListener implements ChatModelListener {
    private final String TRACE_ID = "TraceID";

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        String uuidValue = IdUtil.simpleUUID();
        requestContext.attributes().put(TRACE_ID, uuidValue);
        log.info("请求参数RequestContext：{}", requestContext + "\t" + uuidValue);
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        Object object = responseContext.attributes().get(TRACE_ID);
        log.info("响应参数ResponseContext：{}", responseContext + "\t" + object);
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        log.info("请求异常：{}", errorContext);
    }
}
