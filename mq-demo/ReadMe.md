# RabbitMQ

## 基本示例

**生产者**

```java
@SpringBootTest
class MqDemoApplicationTests {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 1. 测试发送消息，示例代码
     * 2. 测试成功的情况
     */
    @Test
    void publishTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "你好小球球~~~");

        Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, JSON.toJSONString(bunny));
    }

}
```

**消费者**

```java
@Component
@Slf4j
public class MessageListenerOrder {

    @RabbitListener(bindings = @QueueBinding(
            exchange = @Exchange(value = EXCHANGE_DIRECT),
            value = @Queue(value = QUEUE_NAME, durable = "true"),
            key = ROUTING_KEY_DIRECT
    )
    )
    public void processMessage(String dataString, Message message, Channel channel) {
        System.out.println("消费端接受消息：" + dataString);
    }

}
```

## 可靠性

### 1、消息没有发送到消息队列

Q：消息没有发送到消息队列

A：在生产者端进行确认，具体操作中我们会分别针对交换机和队列进行确认，如果没有成功发送到消息队列服务器上，那就可以尝试重新发送。

A：为目标交换机指定备份交换机，当目标交换机投递失败时，把消息投递至备份交换机。

**配置文件**

```yaml
rabbitmq:
  host: ${bunny.rabbitmq.host}
  port: ${bunny.rabbitmq.port}
  username: ${bunny.rabbitmq.username}
  password: ${bunny.rabbitmq.password}
  virtual-host: ${bunny.rabbitmq.virtual-host}
  publisher-confirm-type: correlated # 交换机确认
  publisher-returns: true # 队列确认
```

#### 生产者确认

> [!NOTE]  
> **@PostConstruct 注解**  
> **作用**：在Bean依赖注入完成后执行初始化方法（构造器之后，`afterPropertiesSet()`之前）。  
>
> **特点**：  
> - 方法需**无参**、返回**void**，名称任意  
> - 执行顺序：构造器 → 依赖注入 → `@PostConstruct`  
> - 若抛出异常，Bean创建会失败  
>
> **注意**：  
> - 代理类（如`@Transactional`）中，会在**原始对象**初始化时调用  
> - `prototype`作用域的Bean每次创建均会执行  
> - 避免耗时操作，推荐轻量级初始化  
>
> **替代方案**：  
> `InitializingBean`接口 或 `@Bean(initMethod="xxx")`  

RabbitMQ配置

```java
@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitConfiguration implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnsCallback {

    private final RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        System.out.println("============correlationData <回调函数打印> = " + correlationData);
        System.out.println("============ack <输出> = " + ack);
        System.out.println("============cause <输出> = " + cause);
    }

    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        // 发送到队列失败才会走这个
        log.error("消息主体:{}", returnedMessage.getMessage().getBody());
        log.error("应答码：{}", returnedMessage.getReplyCode());
        log.error("消息使用的父交换机：{}", returnedMessage.getExchange());
        log.error("消息使用的路由键：{}", returnedMessage.getRoutingKey());
    }
}
```

**测试失败的情况**

交换机找不到

```java
/* 测试失败交换机的情况 */
@Test
void publishExchangeErrorTest() {
    String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
    String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;
    rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "----失败的消息发送----");

    Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
    rabbitTemplate.convertAndSend(exchangeDirect + "~", routingKeyDirect, JSON.toJSONString(bunny));
}
```

队列找不到

```java
/* 测试失败队列的情况 */
@Test
void publishQueueErrorTest() {
    String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
    String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;
    rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "----失败的队列发送----");

    Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
    rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect + "~", JSON.toJSONString(bunny));
}
```

#### 备份交换机

> [!NOTE]
>
> 创建好的交换机是无法修改的，只能删除重新建立。

创建广播类型的交换机，因为没有路由键，只能通过广播的方式去找。

![image-20250519104914787](./images/image-20250519104914787.png)

创建与备份交换机的队列，交换机是广播的模式，不指定路由键。

![image-20250519105042156](./images/image-20250519105042156.png)

通过指定`Alternate exchange`的交换机进行绑定。第一个填写的不是备份交换机，是投递交换机，之后通过`Alternate exchange`绑定备份的交换机。

![image-20250519105250365](./images/image-20250519105250365.png)

### 2、服务器宕机

Q：服务器宕机导致内存的消息丢失

A：消息持久化到硬盘上，哪怕服务器重启也不会导致消息丢失。

### 3、消费端宕机或抛异常

Q：消费端宕机或者抛异常，导致消息丢失。

A：消费端消费消息成功，给服务器返回ACK信息，通知把消息恢复成待消费状态。

A：消费端消费消息失败，给服务器返回NACK信息，同时把消息恢复为待消费状态，这样就可以再次取回消息，重试一次（需要消费端接口支持幂等性）。

> [!NOTE]
>
> 需要引入一个内容：deliverTag，交付标签。
>
> 每个消息进入队列 时，broker都会自动生成一个唯一标识（64位整数），消息投递时会携带交付标签。
>
> **作用：**消费端把消息处理结果ACK、NACK、Reject等返回给Broker之后，Broker需要对对应的消息执行后续操作，例如删除消息、重新排队或标记为死信等等那么Broker就必须知道它现在要操作的消息具体是哪一条。而deliveryTag作为消息的唯一标识就很好的满足了这个需求。
>
> **问题：**
>
> Q：如果交换机是Fanout模式，同一个消息广播到了不同队列，deliveryTag会重复吗?
>
> A：不会，deliveryTag在Broker范围内唯一，消息复制到各个队列，deliverTag各不相同。
>
> **multiple说明**
>
> 1. 为false时单独处理这一条消息；正常都是false。
> 2. true批量处理消息。

**配置文件**

```yaml
rabbitmq:
  host: ${bunny.rabbitmq.host}
  port: ${bunny.rabbitmq.port}
  username: ${bunny.rabbitmq.username}
  password: ${bunny.rabbitmq.password}
  virtual-host: ${bunny.rabbitmq.virtual-host}
  # 需要注释下面这两个，不需要这两个，因为要手动确认
  # publisher-confirm-type: correlated # 交换机确认
  # publisher-returns: true # 队列确认
  listener:
    simple:
      acknowledge-mode: manual # 手动处理消息
```

#### 消费端流程

> [!NOTE]
>
> - `deliverTag`
>   - 消费端把消息处理结果ACK、NACK、Reject等返回给Broker之后，Broker需要对对应的消息执行后续操作。
>   - 例如删除消息、重新排队或标记为死信等等那么Broker就必须知道它现在要操作的消息具体是哪一条。
>   - 而deliveryTag作为消息的唯一标识就很好的满足了这个需求。
> - `basicReject`和`basicNack`区别：
>   - `basicNack`可以设置是否批量操作，如果需要批量操作，第二个参数传入`true`为批量，反之。
>   - `basicReject`只能做到批量操作。

```java
@RabbitListener(queues = {QUEUE_NAME})
public void processQueue(String dataString, Message message, Channel channel) throws IOException {
    // 设置 deliverTag
    // 消费端把消息处理结果ACK、NACK、Reject等返回给Broker之后，Broker需要对对应的消息执行后续操作。
    // 例如删除消息、重新排队或标记为死信等等那么Broker就必须知道它现在要操作的消息具体是哪一条。
    // 而deliveryTag作为消息的唯一标识就很好的满足了这个需求。
    long deliveryTag = message.getMessageProperties().getDeliveryTag();

    try {
        // 核心操作
        System.out.println("消费端 消息内容：" + dataString);
        channel.basicAck(deliveryTag, false);

        // 核心操作完成，返回ACK信息
    } catch (Exception e) {
        // 当前参数是否是重新投递的，为true时重复投递过了，为法拉瑟是第一次投递
        Boolean redelivered = message.getMessageProperties().getRedelivered();

        // 第三个参数：
        // true：重新放回队列，broker会重新投递这个消息
        // false：不重新放回队列，broker会丢弃这个消息
        channel.basicNack(deliveryTag, false, !redelivered);

        // 除了 basicNack 外还有 basicReject，其中 basicReject 不能控制是否批量操作
        channel.basicReject(deliveryTag, true);

        // 核心操作失败，返回NACK信息
        throw new RuntimeException(e);
    }
}
```

## 消费端限流

### 设置方式

在配置文件中设置`prefetch`值。如果不设置，当生产者将消息放置到RabbitMQ中时，是一次性取回的，无论有多少。

设置了`prefetch`之后，每次取回数量就是`prefetch`的数量。

> [!NOTE]
>
> 并且在UI界面中`Unacked`值和我们设置的值一致。
>
> ![image-20250519135056806](./images/image-20250519135056806.png)
>
> *图中表示表示当前有5条消息已被消费者获取但未确认（正在处理中）*
>
> 当`prefetch=5`且消费速度为1条/秒时：
>
> - 初始会立即获取5条消息（Unacked=5）
> - 每ACK 1条后，Broker会立即推送1条新消息（动态保持Unacked≈5）

```yaml
  rabbitmq:
    host: ${bunny.rabbitmq.host}
    port: ${bunny.rabbitmq.port}
    username: ${bunny.rabbitmq.username}
    password: ${bunny.rabbitmq.password}
    virtual-host: ${bunny.rabbitmq.virtual-host}
    # publisher-confirm-type: correlated # 交换机确认
    # publisher-returns: true # 队列确认
    listener:
      simple:
        acknowledge-mode: manual # 手动处理消息
        prefetch: 5 # 设置每次取回数量，消息条数（非字节或KB）
```

> [!IMPORTANT]  
> **RabbitMQ Prefetch 机制（prefetch=5）**  
>
> 在 RabbitMQ 的 **prefetch（QoS，服务质量设置）** 机制下，当 `prefetch=5` 时，**消费端的行为** 取决于 **消息确认模式（Ack/Nack）** 和 **消费速度**
>
> **核心规则**：  
> - 保持 `unacked` 消息数 **≤ prefetch (5)**  
> - **不会** 等5条全部ACK完才发下一批，而是 **动态补充**（每ACK 1条，补发1条）  
>
> **不同模式对比**：  
> | 模式                                   | 行为                                          |
> | -------------------------------------- | --------------------------------------------- |
> | **手动ACK** (`AcknowledgeMode.MANUAL`) | ✔️ 推荐！保持 `unacked ≤ 5`，ACK后立即补新消息 |
> | **自动ACK** (`AcknowledgeMode.AUTO`)   | ⚠️ 无效！消息投递后立即ACK，prefetch无法限流   |
>
> >*自动ACK模式下**prefetch仍然有效**（限制未处理的消息数），但消息会在投递后立即被ACK，实际可能失去限流意义。*
>
> **消费慢时的表现**：  
>
> - 若消费速度=1条/秒，RabbitMQ会 **持续补消息**，始终维持 `unacked ≈ 5`  
>

### 测试方式

生产者生产一定数量的消息。

```java
/* 发送消息，发送多条消息，测试使用 */
@Test
void buildMessageTest() {
    String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
    String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;

    for (int i = 0; i < 100; i++) {
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "测试消息发送【" + i + "】");
    }
}
```

消费者进行消费消息，在消费的时候为了方便观察，每秒去读一个。

```java
@RabbitListener(queues = {QUEUE_NAME})
public void processMessagePrefetch(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("消费者 消息内容：{}", dataString);

    TimeUnit.SECONDS.sleep(1);

    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
}
```

## 消息超时

- 给消息设定一个过期时间，超过这个时间没有被取走的消息就会被删除，从两个层面来给消息设定过期时间。
  - **队列层面：**在队列层面设定消息过期时间，并不是队列的过期时间。意思是这个队列中的消息全部使用同一个过期时间。
  - **消息本身：**给具体的某个消息设定过期时间。
- 可通过两种方式设置消息TTL（Time-To-Live），那么哪个时间短，哪个生效。

### 测试环境

测试时候不要用消费端消费（监听），监听取走了就不是超时了。

**创建交换机**

![image-20250519141236957](./images/image-20250519141236957.png)

**创建队列**

> [!NOTE]
> 过期时间单位为毫秒，如`5000`表示5秒。

创建交换机。直接点击下面的加粗字体可以直接设置。

**队列TTL设置步骤**：

创建队列时在`Arguments`中添加：`x-message-ttl`: 设置值（如5000）

![image-20250519135919549](./images/image-20250519135919549.png)

**绑定交换机**

![image-20250519141220594](./images/image-20250519141220594.png)

### 测试队列层面

当在队列中设置了过期时间，超时后自动删除。

**测试Code**

```java
/* 测试过期时间 */
@Test
void buildExchangeTimeoutTest() {
    String EXCHANGE = "exchange.test.timeout";
    String QUEUE = "queue.test.timeout";
    String ROUTING_KEY = "routing.key.test.timeout";

    for (int i = 0; i < 100; i++) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "测试消息超时时间【" + i + "】");
    }
}
```

**测试效果**

![image-20250519141706027](./images/image-20250519141706027.png)

### 测试消息层面

> [!TIP]
>
> 和上面代码对比，区别在于，第四个参数是否设置了`MessagePostProcessor`。
>
> **队列TTL vs 消息TTL**：
>
> - 队列TTL：统一管理，适合批量消息
> - 消息TTL：灵活控制，适合特殊消息

> [!IMPORTANT]
>
> **TTL过期后消息直接删除**（非进入死信队列，除非配置了`x-dead-letter-exchange`）

```java
/* 测试过期时间---消息层面实现消息超时自动删除 */
@Test
void buildExchangeTimeoutTest2() {
    String EXCHANGE = "exchange.test.timeout";
    String QUEUE = "queue.test.timeout";
    String ROUTING_KEY = "routing.key.test.timeout";

    MessagePostProcessor postProcessor = message -> {
        // 设置过期时间
        message.getMessageProperties().setExpiration("7000");
        return message;
    };

    for (int i = 0; i < 100; i++) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "消息层面超时自动删除【" + i + "】", postProcessor);
    }
}
```

## 死信队列

> [!NOTE]
> **死信（Dead Letter）**：满足以下任一条件的消息：
> 1. 被消费者拒绝且不重新入队（`requeue=false`）
> 2. 消息在队列中超过TTL时间
> 3. 队列达到最大长度限制（溢出）

**概念：**当消息因上述原因无法被正常消费时，会被标记为死信（Dead Letter），并可通过死信交换机（DLX）路由到死信队列。

**产生原因：**

1. **拒绝：**消费者拒绝消息`basicNack`/`basicReject`，并且不把消息重新放入源目标队列，`requeue=false`。
2. **溢出：**队列中消息数量达到限制。比如队列最大只能存储10条消息，且现在已经存储了10条消息，此时如果再发送一条消息进来，根据先进先出原则，队列中最早的消息会变成死信。
3. **超时：**消息达到超时时间未被消息。

**死信处理方式：**

- **丢弃**
  适用于对业务无影响的非关键消息（需明确丢弃的监控手段）。
- **入库**
  将死信持久化到数据库，便于后续分析或人工干预。
- **监听（推荐）**
  - 配置独立的死信交换机和队列，与业务逻辑解耦。
  - 消费者专注处理异常消息（如补偿、告警等）。

### 测试环境

#### 搭建死信环境

**创建死信交换机**

exchange.dead.letter.video

![image-20250519151418248](./images/image-20250519151418248.png)

**创建死信队列**

queue.dead.letter.video

![image-20250519151430393](./images/image-20250519151430393.png)

**绑定死信队列**

routing.key.dead.letter.video

![image-20250519151451777](./images/image-20250519151451777.png)

#### 搭建正常环境

> [!WARNING]
> 必须为正常队列配置以下参数才能生效：
> - `x-dead-letter-exchange`：指定死信交换机
> - `x-dead-letter-routing-key`：指定死信路由键

**正常交换机**

exchange.normal.video

**正常队列**

queue.normal.video

这时设置最大长度为10，一次接受10条消息，超出后进入死信。

![image-20250519152200270](./images/image-20250519152200270.png)

**正常路由键**

routing.key.normal.video

到正常的队列中指定正常的交换机和正常路由键。

![image-20250519152552486](./images/image-20250519152552486.png)

### 测试Code

#### 因拒绝产生

**含义：**

- 本来时监听正常队列的，处理一些逻辑，但是因为某些原因消息成死信了，消息转到死信队列中。
- 这时候，在下面又监听了死信队列，死信队列可以监听到成死信的消息。

```java
/* 测试死信---监听正常队列 */
@RabbitListener(queues = {"queue.normal.video"})
public void processMessageNormal(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("监听正常队列----接受到：{}", dataString);
    channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
}

/* 测试死信---监听死信队列 */
@RabbitListener(queues = {"queue.dead.letter.video"})
public void processMessageDeadLetter(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("监听死信队列----接收到：{}", dataString);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
}
```

向正常队列发送消息

![image-20250519153747525](./images/image-20250519153747525.png)

#### 因超时或溢出

在上设置了最大长度为10，如果发送40条那么有30条是溢出的，这时会进入死信队列中。

测试时注释掉监听死信队列和正常队列的代码，之后看UI界面折线图。

```java
/* 因超时或移除产生死信 */
@Test
void buildExchangeOverflowTest() {
    String EXCHANGE = "exchange.normal.video";
    String ROUTING_KEY = "routing.key.normal.video";

    for (int i = 0; i < 40; i++) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "因超时或移除产生死信【" + i + "】");
    }
}
```

从图中可以看出，是分多批进行的。

在之前设置中，消息最大接受是10，最多只能接收到10条消息，之后溢出消息进入死信，其中有10条消息是因为延迟，进入了死信。

![image-20250519154653646](./images/image-20250519154653646.png)