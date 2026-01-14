## 工具调用

### 工具函数定义

工具函数允许 AI 模型调用外部功能，如数据库操作、API 调用等。

#### 工具类定义

```java
@RequiredArgsConstructor
@Component
public class ReservationTool {

    private final ReservationService reservationService;

    @Tool("添加志愿指导服务预约")
    public void addReservation(@P("考生姓名") String name,
                               @P("考生性别") String gender,
                               @P("考生手机号") String phone,
                               @P("预约沟通时间：格式为：yyyy-MM-dd'T'HH:mm") String communicationTime,
                               @P("考生所处的省份") String province,
                               @P("考生预估分数") Integer estimatedScore) {
        ReservationEntity reservation = ReservationEntity.builder()
                .name(name)
                .gender(gender)
                .phone(phone)
                .communicationTime(LocalDateTime.parse(communicationTime))
                .province(province)
                .estimatedScore(estimatedScore)
                .build();

        reservationService.save(reservation);
    }

    @Tool("根据考生电话查询考生预约详情")
    public ReservationEntity findReservationById(@P("考生手机号") String phone) {
        return reservationService.getOne(Wrappers.<ReservationEntity>lambdaQuery().eq(ReservationEntity::getPhone, phone));
    }
}
```

#### 配置 AI Service 使用工具

```java
@AiService(
        wiringMode = AiServiceWiringMode.EXPLICIT,
        chatModel = "openAiChatModel",
        streamingChatModel = "openAiStreamingChatModel",
        chatMemoryProvider = "chatMemoryProvider",
        contentRetriever = "contentRetriever",
        tools = "reservationTool"  // 配置工具
)
public interface ChatMemoryAiService {

    /**
     * AI聊天记忆示例（带工具调用）
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> sampleChat(String message);

    /**
     * AI聊天记忆功能示例（带工具调用）
     * @param memoryId 会话记忆ID
     * @param message 聊天内容
     * @return 聊天结果
     */
    @SystemMessage(fromResource = "static/ai/system.txt")
    Flux<String> memoryChat(@MemoryId String memoryId, @UserMessage String message);
}
```

### 工具调用说明

- `@Tool` 注解用于标记工具方法，描述信息会帮助 AI 理解工具的功能
- `@P` 注解用于描述参数，帮助 AI 理解参数的含义和格式
- 工具方法可以是任何业务逻辑，如数据库操作、API 调用等
- AI 会根据对话内容自动判断是否需要调用工具