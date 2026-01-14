> [!NOTE]
>
> 如果只有一个参数时可以使用`{{it}}`表示，`it` 指代唯一的方法参数。

## 提示词-注解方式

使用`SystemMessage`明确定义助手的角色和能力范围，将其限定在法律咨询领域。在LangChain4j中，我们主要利用`SystemMessage`来实现这一点，`SystemMessage`具有高优先级，能有效地指导模型的整体行为。

利用提示词模板(`@UserMessage`,`@V`)精确控制输入和期望的输出格式，确保问题被正确理解和回答。

### 创建Service

`{{question}}`是模板，内容是传参中设置的内容`@V("question") String question`

1. `@SystemMessage`是系统提示词
2. `@UserMessage`是用户设置的提示词
3. 使用 `@V` 注解为模板变量分配自定义名称

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwen3-vl-flash",
        streamingChatModel = "stream-qwen",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LawAssistantService {

    @UserMessage("请回答以下法律问题:{{question}},字数控制在{{length}}以内，以{{format}}格式输出")
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    Flux<String> chat(@V("question") String question, @V("length") int length, @V("format") String format);
}
```

### 在Controller中使用

```java
@Tag(name = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {

    private final ChatAssistantService chatAssistantService;

    private final ChatAssistantExampleService chatAssistantExampleService;

    private final LawAssistantService lawAssistantService;

    public AdvanceApiController(ChatAssistantService chatAssistantService, ChatAssistantExampleService chatAssistantExampleService, LawAssistantService lawAssistantService) {
        this.chatAssistantService = chatAssistantService;
        this.chatAssistantExampleService = chatAssistantExampleService;
        this.lawAssistantService = lawAssistantService;
    }
    
    @Operation(summary = "使用LawAssistantService演示")
    @GetMapping("law-chat")
    public Flux<String> lawChat() {
        Flux<String> json1 = lawAssistantService.chat("知识产权是什么", 100, "json");
        Flux<String> json2 = lawAssistantService.chat("上海有什么好吃", 100, "json");
        Flux<String> json3 = lawAssistantService.chat("什么是Java", 100, "json");

        return Flux.merge(json1, json2, json3);
    }
}
```

## 提示词-实体类方式

### 创建实体类

使用这个就不用在Service上加注解了，直接在实体类上添加这些内容即可

```java
@StructuredPrompt("法律内容：{{legal}}\n解答用户以下问题：{{question}}")
@Schema(description = "大模型法律提示词")
@Data
public class LawPromptValue {

    @Schema(description = "法律内容")
    private String legal;

    @Schema(description = "用户问题")
    private String question;

}
```

### Service添加

在Service中直接使用实体类即可

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwen3-vl-flash",
        streamingChatModel = "stream-qwen",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LawAssistantService {
    @SystemMessage("你是一位专业的中国法律顾问，只回答与中国法律相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答中国法律相关的问题。'")
    String chat(LawPromptValue lawPrompt);
}
```

### Controller使用

```java
@Tag(name = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {

    private final ChatAssistantService chatAssistantService;

    private final ChatAssistantExampleService chatAssistantExampleService;

    private final LawAssistantService lawAssistantService;

    public AdvanceApiController(ChatAssistantService chatAssistantService, ChatAssistantExampleService chatAssistantExampleService, LawAssistantService lawAssistantService) {
        this.chatAssistantService = chatAssistantService;
        this.chatAssistantExampleService = chatAssistantExampleService;
        this.lawAssistantService = lawAssistantService;
    }
    
    @Operation(summary = "使用提示词实体类方式回答")
    @GetMapping("law-chat-value")
    public String lawChatValue() {
        LawPromptValue lawPromptValue = new LawPromptValue();
        lawPromptValue.setLegal("知识产权是什么");
        lawPromptValue.setQuestion("上海有什么好吃");
        return lawAssistantService.chat(lawPromptValue);
    }
}
```

## 提示词

### 单个的提示词

对于单个的占位符可以使用`{{it}}`进行表示

#### Controller中使用

```java
@Tag(name = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {
    private final ChatModel chatModel;

    public AdvanceApiController(@Qualifier("qwen") ChatModel chatModel) {
        this.chatModel = chatModel;
    }
    
    @Operation(summary = "单个参数使用{{it}}进行表示")
    @GetMapping("law-chat-it")
    public String lawChatIt() {
        // 创建包含角色和问题信息的上下文映射
        Map<String, Object> it = Map.of("it", "外科医生", "question", "累了");

        // 构建提示词模板，使用占位符来动态替换内容
        PromptTemplate promptTemplate = PromptTemplate.from("你是一个{{it}}助手，{{question}}怎么办");
        // 应用上下文数据到模板，生成具体的提示词
        Prompt prompt = promptTemplate.apply(it);

        // 将提示词转换为用户消息格式
        UserMessage userMessage = prompt.toUserMessage();
        // 调用聊天模型进行对话，获取AI响应
        ChatResponse chatResponse = chatModel.chat(userMessage);

        // 返回AI响应中的文本内容
        return chatResponse.aiMessage().text();
    }
}
```

#### Service中使用

**Service定义**

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "qwen3-vl-flash",
        streamingChatModel = "stream-qwen",
        chatMemoryProvider = "chatMemoryProvider"
)
public interface LawAssistantService {
    /**
     * 使用占位符进行回答
     *
     * @param it       角色
     * @param question 问题
     * @return 回答
     */
    @SystemMessage("你是一位专业的{{it}}，只回答与{{it}}相关的问题。" +
            "输出限制:对于其他领域的问题禁止回答，直接返回'抱歉，我只能回答{{it}}相关的问题。'")
    Flux<String> chatByIt(@V("it") String it, String question);
}
```

**Controller中**

```java
@Tag(name = "高级Api服务")
@RequestMapping("api/ai/advance")
@RestController
public class AdvanceApiController {

    private final LawAssistantService lawAssistantService;

    public AdvanceApiController(@Qualifier("qwen") ChatModel chatModel) {
        this.lawAssistantService = lawAssistantService;
    }

    @Operation(summary = "使用Service占位符")
    @GetMapping("law-chat-service-it")
    public Flux<String> lawChatServiceIt() {
        return lawAssistantService.chatByIt("外科医生", "累了");
    }
}
```

### 从资源文件读取

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel"
)
public interface ConsultantService {
    /**
     * 聊天（带系统消息）
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/system.txt")
    Flux<String> chatByMessage(String message);
}
```

