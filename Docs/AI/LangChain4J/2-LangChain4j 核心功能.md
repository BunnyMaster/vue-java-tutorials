## 声明式 AI 服务（AiServices）

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

### @AiService 注解详解

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

## 流式输出

### 

## 提示词工程

### 系统提示词 (@SystemMessage)

### 用户提示词 (@UserMessage)

### 模板变量 (@V) 使用

### 结构化提示词

## 工具调用

### @Tool 注解定义工具

### @P 注解参数描述

### 工具调用执行流程

### 数据库/API 集成示例