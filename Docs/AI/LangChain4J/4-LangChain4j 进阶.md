## AiServices

### 概述

AiServices 是 LangChain4j 提供的核心功能之一，它采用声明式编程模式，通过接口定义和动态代理技术，简化 AI 功能的集成和使用。

口的 `Class` 对象和底层组件作为参数，创建一个实现该接口的代理对象。当前实现基于反射机制，未来可能考虑其他替代方案。

**处理流程**：
1. **输入转换**：将方法参数自动转换为合适的消息类型
2. **模型调用**：调用配置的 ChatLanguageModel 进行处理
3. **输出转换**：将模型返回结果转换为方法定义的返回类型

### Bean注入方式

#### 1. 服务接口定义

```java
public interface ChatAssistantExampleService {

    /**
     * 普通聊天，使用AiServices
     *
     * @param prompt 提示词
     * @return 输出
     */
    String chat(String prompt);
}
```

#### 2. 服务配置类

```java
@Configuration
public class ChatAssistantExampleServiceConfig {
    @Bean
    public ChatAssistantExampleService chatAssistantExampleService(@Qualifier("qwen") ChatModel chatModel) {
        return AiServices.create(ChatAssistantExampleService.class, chatModel);
    }
}

```

#### 3. 控制器实现

```java
@Tag(name = " advance-api-controller", description = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {

    private final ChatAssistantExampleService chatAssistantExampleService;

    public AdvanceApiController(ChatAssistantExampleService chatAssistantExampleService) {
        this.chatAssistantExampleService = chatAssistantExampleService;
    }

    @Operation(summary = "使用AiServices演示")
    @GetMapping("chat")
    public String chat(@RequestParam(value = "question", defaultValue = "你是谁") String question) {
        return chatAssistantExampleService.chat(question);
    }

}
```

### 注解方式

> [!IMPORTANT]
> 使用注解方式时，需要注释掉上面的 `LangChain4jConfig` 配置类

#### 显式指定模型

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel"
)
public interface ConsultantService {
    /**
     * 聊天
     * @param message 聊天内容
     * @return 聊天结果
     */
    String chat(String message);
}
```

#### 自动装配

```java
@AiService
public interface ConsultantService {
    /**
     * 聊天
     * @param message 聊天内容
     * @return 聊天结果
     */
    String chat(String message);
}
```

#### 使用示例

```java
@Tag(name = "ChatController", description = "AI聊天接口")
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class ChatController {

    private final ConsultantService consultantService;

    @Operation(summary = "Chat with AI bean", description = "AI聊天bean")
    @GetMapping("chat-bean")
    public String chatBean(String message) {
        return consultantService.chat(message);
    }
}
```

## 常用配置

### 输出日志配置

只有日志级别为debug级别，同时配置`log-requests`和`log-responses: true`才有效

#### 配置文件示例

```yaml
langchain4j:
  open-ai:
    chat-model:
      base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
      api-key: ${ALIYUN-AI-API-KEY}
      model-name: "qwen3-max"
      log-requests: true
      log-responses: true

logging:
  level:
    root: info
    dev.langchain4j: debug
```

#### Bean使用示例

开启`logResponses(true)`和`logResponses(true)`这两个

```java
@Configuration
public class LLMConfig {

    @Bean(name = "qwen")
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
                .apiKey(System.getenv("Deepseek-AI-API-KEY"))
                .modelName("deepseek-chat")
                .baseUrl("https://api.deepseek.com/v1")
                .logResponses(true)
                .logResponses(true)
                .build();
    }
}
```

### 监听器

> [!NOTE]
>
> 参考官网：https://docs.langchain4j.dev/tutorials/observability#chat-model-observability

如果需要可以自己实现`ChatModelListener`，在下面示例中包含了所有的可以使用的参数。

这些事件包括各种属性，例如：

- 请求：消息、模型、温度、Top P、最大令牌数、工具、响应格式、等等
- 响应：助手消息、ID、模型、令牌使用情况、完成原因、等等

```java
ChatModelListener listener = new ChatModelListener() {

    @Override
    public void onRequest(ChatModelRequestContext requestContext) {
        ChatRequest chatRequest = requestContext.chatRequest();

        List<ChatMessage> messages = chatRequest.messages();
        System.out.println(messages);

        ChatRequestParameters parameters = chatRequest.parameters();
        System.out.println(parameters.modelName());
        System.out.println(parameters.temperature());
        System.out.println(parameters.topP());
        System.out.println(parameters.topK());
        System.out.println(parameters.frequencyPenalty());
        System.out.println(parameters.presencePenalty());
        System.out.println(parameters.maxOutputTokens());
        System.out.println(parameters.stopSequences());
        System.out.println(parameters.toolSpecifications());
        System.out.println(parameters.toolChoice());
        System.out.println(parameters.responseFormat());

        if (parameters instanceof OpenAiChatRequestParameters openAiParameters) {
            System.out.println(openAiParameters.maxCompletionTokens());
            System.out.println(openAiParameters.logitBias());
            System.out.println(openAiParameters.parallelToolCalls());
            System.out.println(openAiParameters.seed());
            System.out.println(openAiParameters.user());
            System.out.println(openAiParameters.store());
            System.out.println(openAiParameters.metadata());
            System.out.println(openAiParameters.serviceTier());
            System.out.println(openAiParameters.reasoningEffort());
        }

        System.out.println(requestContext.modelProvider());

        Map<Object, Object> attributes = requestContext.attributes();
        attributes.put("my-attribute", "my-value");
    }

    @Override
    public void onResponse(ChatModelResponseContext responseContext) {
        ChatResponse chatResponse = responseContext.chatResponse();

        AiMessage aiMessage = chatResponse.aiMessage();
        System.out.println(aiMessage);

        ChatResponseMetadata metadata = chatResponse.metadata();
        System.out.println(metadata.id());
        System.out.println(metadata.modelName());
        System.out.println(metadata.finishReason());

        if (metadata instanceof OpenAiChatResponseMetadata openAiMetadata) {
            System.out.println(openAiMetadata.created());
            System.out.println(openAiMetadata.serviceTier());
            System.out.println(openAiMetadata.systemFingerprint());
        }

        TokenUsage tokenUsage = metadata.tokenUsage();
        System.out.println(tokenUsage.inputTokenCount());
        System.out.println(tokenUsage.outputTokenCount());
        System.out.println(tokenUsage.totalTokenCount());
        if (tokenUsage instanceof OpenAiTokenUsage openAiTokenUsage) {
            System.out.println(openAiTokenUsage.inputTokensDetails().cachedTokens());
            System.out.println(openAiTokenUsage.outputTokensDetails().reasoningTokens());
        }

        ChatRequest chatRequest = responseContext.chatRequest();
        System.out.println(chatRequest);

        System.out.println(responseContext.modelProvider());

        Map<Object, Object> attributes = responseContext.attributes();
        System.out.println(attributes.get("my-attribute"));
    }

    @Override
    public void onError(ChatModelErrorContext errorContext) {
        Throwable error = errorContext.error();
        error.printStackTrace();

        ChatRequest chatRequest = errorContext.chatRequest();
        System.out.println(chatRequest);

        System.out.println(errorContext.modelProvider());

        Map<Object, Object> attributes = errorContext.attributes();
        System.out.println(attributes.get("my-attribute"));
    }
};

ChatModel model = OpenAiChatModel.builder()
        .apiKey(System.getenv("OPENAI_API_KEY"))
        .modelName(GPT_4_O_MINI)
        .listeners(List.of(listener))
        .build();

model.chat("Tell me a joke about Java");
```

#### 实现方式

```java
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
```

在`listeners`中可以传递多个监听器

```java
@Bean(name = "qwen")
public OpenAiChatModel chatModelQwen() {
    return OpenAiChatModel.builder()
            .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
            .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
            .modelName("qwen3-max")
            .logRequests(true)
            .logResponses(true)
            .listeners(List.of(new QwenChatModelListener()))
            .build();
}
```

### 其他配置

#### 重试机制

在配置中加上`maxRetries`

#### 超时时间

在配置上加上`timeout`超时时间

#### 整体配置

```java
@Bean(name = "qwen")
public OpenAiChatModel chatModelQwen() {
    return OpenAiChatModel.builder()
            .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
            .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
            .modelName("qwen3-max")
            .logRequests(true)
            .logResponses(true)
            .listeners(List.of(new QwenChatModelListener()))
            .maxRetries(3)
            .timeout(Duration.ofSeconds(3000L))
            .build();
}
```

## 视觉理解

### 图片识别

本模块基于阿里云的通义千问视觉语言模型 `qwen3-vl-flash`，实现了图片内容识别和分析功能。支持对上传的图片进行智能识别，包括物体识别、场景理解、文字提取等视觉任务。

```java
@Bean(name = "qwen3-vl-flash")
public OpenAiChatModel openAiChatModelQWenVLFlash() {
    return OpenAiChatModel.builder()
            .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
            .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
            .modelName("qwen3-vl-flash")
            .logRequests(true)
            .logResponses(true)
            .listeners(List.of(new QwenChatModelListener()))
            .maxRetries(3)
            .timeout(Duration.ofSeconds(3000L))
            .build();
}
```

控制器

```java
@Tag(name = " image-chat-controller", description = "图片聊天服务")
@RequestMapping("api/ai/image")
@RestController
public class ImageChatController {

    private final OpenAiChatModel openAiChatModelQWenVLFlash;

    @Value("classpath:static/image-test-1.jpg")
    private Resource resource;

    public ImageChatController(@Qualifier("qwen3-vl-flash") OpenAiChatModel openAiChatModelQWenVLFlash) {
        this.openAiChatModelQWenVLFlash = openAiChatModelQWenVLFlash;
    }

    @RequestMapping("chat")
    public String chat() throws IOException {
        byte[] byteArray = resource.getContentAsByteArray();
        String base64Data = Base64.getEncoder().encodeToString(byteArray);

        UserMessage userMessage = UserMessage.from(
                TextContent.from("图片中是谁？来自哪里？角色是什么？"),
                ImageContent.from(base64Data, "image/jpg")
        );

        ChatResponse chatResponse = openAiChatModelQWenVLFlash.chat(userMessage);
        return chatResponse.aiMessage().text();
    }
}
```

### 阿里万象-文生图

- **容错策略**
  - **处理限流**：当 API 返回 `Throttling` 错误码或 HTTP 429 状态码时，表明已触发限流，限流处理请参见[限流](https://help.aliyun.com/zh/model-studio/rate-limit)。
  - **异步任务轮询**：轮询查询异步任务结果时，建议采用合理的轮询策略（如前30秒每3秒一次，之后拉长间隔），避免因过于频繁的请求而触发限流。为任务设置一个最终超时时间（如 2 分钟），超时后标记为失败。
- **风险防范**
  - **结果持久化**：API 返回的图片 URL 有 24 小时有效期。生产系统必须在获取 URL 后立即下载图片，并转存至您自己的持久化存储服务中（如阿里云对象存储 OSS）。
  - **内容安全审核**：所有 `prompt` 和 `negative_prompt` 都会经过内容安全审核。若输入内容不合规，请求将被拦截并返回 `DataInspectionFailed` 错误。
  - **生成内容的版权与合规风险**：请确保您的提示词内容符合相关法律法规。生成包含品牌商标、名人肖像、受版权保护的 IP 形象等内容可能涉及侵权风险，请您自行评估并承担相应责任。

> [!NOTE]
>
> LangChain4j官网参考：https://docs.langchain4j.dev/integrations/embedding-models/dashscope
>
> 阿里官网：https://bailian.console.aliyun.com/?spm=5176.29597918.J_SEsSjsNv72yRuRFS2VknO.2.2e3e7b08DjS7IL&tab=model#/model-market/detail/wan2.5-t2i-preview
>
> 提示词参考文档：https://help.aliyun.com/zh/model-studio/text-to-image-prompt

#### 图像分辨率说明

参数: parameters.size (string)，格式为 `**"宽\*高"**`。

**通义千问 Qwen-Image**：仅支持以下 5 种固定的分辨率：

- 1328*1328（默认值）：1:1。
- 1664*928: 16:9。
- 928*1664: 9:16。
- 1472*1140: 4:3。
- 1140*1472: 3:4。

**通义万相 V2 版模型 (2.0 及以上版本)**：支持在 `[512, 1440]` 像素范围内任意组合宽高，总像素不超过 1440*1440。常用分辨率：

- 1024*1024（默认值）：1:1。
- 1440*810: 16:9。
- 810*1440: 9:16。
- 1440*1080: 4:3。
- 1080*1440: 3:4。

#### 环境配置

在包管理器中引入`langchain4j-community-bom`

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

        <dependency>
            <groupId>dev.langchain4j</groupId>
            <artifactId>langchain4j-community-bom</artifactId>
            <version>1.8.0-beta15</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

在`dependencies`引入，这里的版本号是上面配置的`dependencyManagement`

```xml
<dependency>
    <groupId>dev.langchain4j</groupId>
    <artifactId>langchain4j-community-dashscope-spring-boot-starter</artifactId>
</dependency>
```

#### 简单生成

配置`WanX`模型

```java
@Bean
public WanxImageModel wanxImageModel() {
    return WanxImageModel.builder()
            .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
            .modelName("wan2.5-t2i-preview")
            .build();
}
```

根据用户的关键词生成

```java
@Operation(summary = "WanX图片生成Api演示")
@GetMapping("image/wan-create")
public String createWanXImage(@RequestParam(value = "message", defaultValue = "不要生成") String message) {
    Response<Image> imageResponse = wanxImageModel.generate(message);
    return imageResponse.content().url().toASCIIString();
}
```

#### 高级使用

1. 其中`size`只能是`['1024*1024', '720*1280', '1280*720', '768*1152']`

```java
@Operation(summary = "WanX图片生成Api演示")
@GetMapping("image/wan-create-advance")
public String createWanXImageAdvance() {
    String prompt = "近景镜头，18岁的中国女孩，古代服饰，圆脸，正面看着镜头，" +
            "民族优雅的服装，商业摄影，室外，电影级光照，半身特写，精致的淡妆，锐利的边缘。";

    ImageSynthesisParam imageSynthesisParam = ImageSynthesisParam.builder()
            .apiKey(System.getenv("ALIYUN-AI-API-KEY"))
            .model(ImageSynthesis.Models.WANX_V1)
            .prompt(prompt)
            .style("<watercolor>")
            .n(1)
            // ['1024*1024', '720*1280', '1280*720', '768*1152']
            .size("1280*720")
            .build();

    ImageSynthesis imageSynthesis = new ImageSynthesis();
    try {
        ImageSynthesisResult imageSynthesisResult = imageSynthesis.call(imageSynthesisParam);
        return JSONUtil.toJsonStr(imageSynthesisResult);
    } catch (NoApiKeyException e) {
        throw new RuntimeException(e);
    }
}
```

输出结果

```json
{"requestId":"94d2f475-d8f5-4e47-a329-d4913ea7a4b5","output":{"taskId":"e11f0e04-f4d8-4330-92ff-847e8e5f1dc5","taskStatus":"SUCCEEDED","results":[{"url":"https://dashscope-result-bj.oss-cn-beijing.aliyuncs.com/1d/22/20251123/c70535fc/b69fa89a-2ef9-4812-ac8d-723bb48092eb-1.png?Expires=1763995524&OSSAccessKeyId=LTAI5tQZd8AEcZX6KZV4G8qL&Signature=HkCY9vmXetj09U1Ma5exXS2cqO0%3D"}],"taskMetrics":{"total":1,"succeeded":1,"failed":0}},"usage":{"imageCount":1}}
```

