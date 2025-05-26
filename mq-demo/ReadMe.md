# RabbitMQ

**安装RabbitMQ**

```bash
docker run -d --name rabbitmq_master --restart=always \
-p 5672:5672 -p 15672:15672   \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/data:/var/lib/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/conf:/etc/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/log:/var/log/rabbitmq \
-e RABBITMQ_DEFAULT_VHOST=rabbitmq_master \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.13.7-management
```

**后续测试联邦队列**

如果后续需要学习联邦队列可以再复制下面的内容，根据自己需求修改下端口号

```bash
docker run -d --name rabbitmq_2 --restart=always \
-p 5673:5672 -p 15673:15672   \
-v ~/docker/docker_data/rabbitmq/rabbitmq2/data:/var/lib/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq2/conf:/etc/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq2/log:/var/log/rabbitmq \
-e RABBITMQ_DEFAULT_VHOST=rabbitmq2 \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.13.7-management
```

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
>
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
>
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

## 延迟队列

### 应用场景

- 订单超时未支付自动取消
- 预约任务延迟触发
- 重试机制中的延迟重试

#### 实现方案对比

| 方案         | 优点                 | 缺点                          |
| :----------- | :------------------- | :---------------------------- |
| TTL+死信队列 | 无需插件             | 队列级别TTL，灵活性差         |
| 延迟消息插件 | 消息级延迟，精确控制 | 需安装插件，延迟有上限（2天） |

### 安装插件

如果不想下，这个文档代码仓库中有的，在根目录下`rabbitmq_delayed_message_exchange-3.13.0.ez`

> [!WARNING]
>
> 插件限制：
>
> - 最大延迟时间：**2天（48小时）**
> - 必须匹配RabbitMQ版本
>
> **插件版本必须与RabbitMQ严格匹配（如3.13.x需使用v3.13.x插件）。**

#### 1、确认docker数据卷

```bash
docker inspect rabbitmq_master | grep -A 10 Mounts
```

找到`Mounts`，如果挂在了`plugins`下载后放到这个目录中，当前版本无法创建`/plugins`目录，只能手动下载之后移入。参考下面的第二点。

若未挂载`/plugins`目录，需手动拷贝插件文件：

```bash
docker cp rabbitmq_delayed_message_exchange-3.13.0.ez rabbitmq_master:/plugins/
```

#### 2、安装插件

**安装步骤**

需要注意，版本是否对应上，我当前RabbitMQ版本是`rabbitmq:3.13.7-management`

```bash
# 下载插件（版本必须匹配）
wget https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.13.0/rabbitmq_delayed_message_exchange-3.13.0.ez

# 启用插件
docker exec rabbitmq_master rabbitmq-plugins enable rabbitmq_delayed_message_exchange

# 重启容器生效
docker restart rabbitmq_master
```

**验证安装**

```bash
docker exec rabbitmq_master rabbitmq-plugins list | grep delay
```

应输出：`[E*] rabbitmq_delayed_message_exchange`

### 测试环境配置

#### **1. 创建延迟交换机**

- 名称：`exchange.test.delay`
- 类型：`x-delayed-message`
- **必须参数**：

```json
{
  "x-delayed-type": "direct"  // 指定底层交换机类型（direct/topic/fanout）
}
```

> [!IMPORTANT]
>
> 在指定交换机时，因为本身的Type类型已经设置成了`x-delayed-message`，但是又没有指定交换机类型，又必须指定交换机类型，所以指定下面参数选项中设置延迟交换机的类型。
>
> 需要在下面参数中设置：`x-delayed-type：交换机类型`。

![image-20250519164038005](./images/image-20250519164038005.png)

#### **2. 创建队列与绑定**

- 队列：`queue.test.delay`（无需特殊参数）
- 路由键：`routing.key.test.delay`

![image-20250519164337295](./images/image-20250519164337295.png)

### 测试Code

**生产者端**

```java
/* 测试延迟消息 */
@Test
void delayedPublishTest() {

    // 在下面测试中，如果没有安装延迟插件，设置了 x-delay 没有作用
    MessagePostProcessor messagePostProcessor = message -> {
        // 10秒延迟
        message.getMessageProperties().setHeader("x-delay", 10000);
        return message;
    };

    rabbitTemplate.convertAndSend("exchange.test.delay",
            "routing.key.test.delay",
            "延迟消息插件：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()),
            messagePostProcessor
    );
}
```

**消费者端**

```java
/* 测试延迟消息 */
@RabbitListener(queues = "queue.test.delay")
public void processMessageDelay(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("<延迟消息>----消息本身{}", dataString);
    log.info("<延迟消息>----当前时间{}", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
}
```

## 消息事务

在 RabbitMQ 中使用事务（Transaction）可以确保消息的可靠性，但并不推荐在高并发场景下使用，因为它会带来显著的性能开销。以下是详细分析和替代方案建议：

**1. RabbitMQ 事务的机制**

通过 `channel.txSelect()`、`channel.txCommit()`、`channel.txRollback()` 实现：
```java
channel.txSelect(); // 开启事务
try {
    channel.basicPublish(exchange, routingKey, props, message.getBytes());
    // 其他业务操作（如数据库写入）
    channel.txCommit(); // 提交事务
} catch (Exception e) {
    channel.txRollback(); // 回滚事务
}
```

#### **存在的问题**
| 问题             | 说明                                                         |
| ---------------- | ------------------------------------------------------------ |
| **性能差**       | 事务会同步阻塞信道，吞吐量下降约 2~10 倍（实测数据）         |
| **伪原子性**     | 只能保证消息发送到 Broker，无法保证业务操作（如 DB 更新）的原子性 |
| **复杂场景失效** | 分布式系统中，跨服务的事务需依赖 Seata 等方案，MQ 事务无法覆盖 |

---

### 生产环境推荐方案
#### （1）Confirm 模式（轻量级确认）
- **原理**：异步确认消息是否成功到达 Broker。
- **配置方式**：
  ```java
  // 开启 Confirm 模式
  channel.confirmSelect();
  
  // 异步监听确认结果
  channel.addConfirmListener((deliveryTag, multiple) -> {
      // 消息成功投递
  }, (deliveryTag, multiple) -> {
      // 消息投递失败（可重试或记录日志）
  });
  ```
- **优点**：性能接近非事务模式，可靠性高。

#### （2）消息补偿 + 幂等设计
- **步骤**：
  1. 消息表记录发送状态（如 `status: sending/success/fail`）。
  2. 定时任务补偿失败消息。
  3. 消费者端做幂等处理（如唯一 ID + 去重表）。
- **适用场景**：订单支付、库存扣减等关键业务。

#### （3）本地消息表（最终一致性）
```mermaid
sequenceDiagram
    participant App
    participant DB
    participant MQ
    App->>DB: 1. 业务数据+消息记录（同事务）
    DB-->>App: 成功
    App->>MQ: 2. 异步发送消息
    MQ-->>App: ACK
    App->>DB: 3. 更新消息状态
```

仅在以下情况考虑使用：
- **低频操作**：如日均消息量 < 1k。
- **强一致性要求**：且无法接受异步补偿的极端场景。
- **简单业务**：无嵌套调用（如纯消息发送无 DB 操作）。

| 模式         | 吞吐量（条/秒） | 延迟 | 可靠性 |
| ------------ | --------------- | ---- | ------ |
| 事务模式     | 500~1k          | 高   | 高     |
| Confirm 模式 | 10k~50k         | 低   | 高     |
| 无确认       | 50k+            | 极低 | 低     |

## 惰性队列

> [!NOTE]
>
> 1. **性能权衡**：惰性队列的吞吐量可能低于普通队列(尤其是内存队列)
> 2. **磁盘I/O压力**：会增加磁盘I/O操作，需要考虑磁盘性能
> 3. **不适合低延迟场景**：由于涉及磁盘操作，不适合对延迟极其敏感的场景

- 在创建队列时，在`Durability:`可以有两种选择：
  - Durable：持久化队列，消息持久化到硬盘上。
  - Transient：临时队列，不做持久化操作，broker重启后消息会丢失。

### 惰性队列的核心特点

1. **消息直接写入磁盘**：不像普通队列先将消息存入内存再刷盘
2. **按需加载到内存**：只有在消费者需要时才将消息加载到内存
3. **减少内存占用**：特别适合处理大量消息且消费速度较慢的场景

### 主要应用场景

#### 1. 大流量消息积压场景

- 当生产者速度远高于消费者速度时
- 传统队列可能导致内存溢出，而惰性队列能有效控制内存使用

#### 2. 长时间消息堆积

- 需要长时间存储大量消息(如日志、审计数据)
- 消息可能需要在队列中保留数小时甚至数天

#### 3. 高可用性要求场景

- 减少节点故障时的消息丢失风险(因为消息已持久化到磁盘)
- 配合镜像队列使用可提高系统可靠性

#### 4. 内存敏感环境

- 在内存资源有限的服务器上
- 需要处理大量消息但无法提供足够内存的情况

#### 5. 突发流量处理

- 能够吸收突发的大量消息而不影响系统稳定性
- 为消费者处理高峰流量提供缓冲时间

### 配置方式

可以通过以下方式声明惰性队列：

```java
// Java客户端示例
Map<String, Object> args = new HashMap<>();
args.put("x-queue-mode", "lazy");
channel.queueDeclare("myLazyQueue", true, false, false, args);
```

## 优先级队列

**优先级仅在消息堆积时生效（空队列时无意义）**

**优先级范围**

- 通过 `x-max-priority` 参数定义队列支持的最大优先级（默认0=无优先级）
- 有效值：`1-255`（建议不超过10，因[官方文档](https://www.rabbitmq.com/priority.html)指出高值会显著影响性能）

**消息排序规则**

- 高优先级消息**优先被消费**（同优先级则遵循FIFO）
- 仅当队列堆积时生效（空队列时无意义）

### 测试环境

**创建交换机**

exchange.test.priority

![image-20250519171927379](./images/image-20250519171927379.png)

**创建队列**

队列名称：queue.test.priority

| 参数             | 值   | 说明                            |
| :--------------- | :--- | :------------------------------ |
| `x-max-priority` | 10   | 必须手动添加（UI/代码均可设置） |

![image-20250519172029093](./images/image-20250519172029093.png)

**绑定路由**

routing.key.test.priority

![image-20250519172210296](./images/image-20250519172210296.png)

### 测试Code

```mermaid
graph LR
A[生产者设置priority] --> B{消息入队}
B -->|高优先级| C[插入队列头部附近]
B -->|低优先级| D[插入队列尾部附近]
```

> [!IMPORTANT]
>
> 设置优先级，不能超过设置的优先级，例如设置最高优先级 x-max-priority:	10
>
> 最高不能超过10

**生产者**

```java
/* 测试消息优先级 */
@Test
void priorityTest() {
    for (int i = 0; i <= 10; i++) {
        int finalI = i;
        rabbitTemplate.convertAndSend("exchange.test.priority",
                "routing.key.test.priority",
                "优先级消息-" + i,
                message -> {
                    // 设置优先级，不能超过设置的优先级，例如设置最高优先级 x-max-priority:	10
                    // 最高不能超过10
                    message.getMessageProperties().setPriority(finalI);
                    return message;
                });
    }
}
```

**消费者**

```java
/* 测试优先级队列 */
@RabbitListener(queues = "queue.test.priority")
public void processMessagePriority(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("<<优先级队列>>----<priority>{}", dataString);
}
```

## 集群搭建

完成示例参考

![image-20250519224811858](./images/image-20250519224811858.png)

![image-20250519224836326](./images/image-20250519224836326.png)

> [!TIP]
>
> 在操作之前可以拍摄虚拟机快照，方便回滚，要保证这个环境没有RabbitMQ或妨碍搭建的内容。

### 修改Ubuntu网络(可选)

> [!IMPORTANT]
>
> 如果需要修改，记得修改网关和IP地址。
>
> 节点间端口要求（如4369/25672需互通），避免防火墙问题.。

看自己是否有这个需求。

```yaml
network:
  ethernets:
    ens33:
      dhcp4: false
      addresses: [192.168.3.144/24]
      optional: true
      routes:
        - to: default
          via: 192.168.3.1
      nameservers:
        addresses: [8.8.8.8]
  version: 2
```

应用修改

```bash
sudo netplan apply
# or
sudo reboot
```

### Docker方式

集群环境搭建版本是：

#### 1、配置docker镜像源

```bash
# 创建目录
sudo mkdir -p /etc/docker
# 写入配置文件
sudo tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
    	"https://docker-0.unsee.tech",
        "https://docker-cf.registry.cyou",
        "https://docker.1panel.live"
    ]
}
EOF
# 重启docker服务
sudo systemctl daemon-reload && sudo systemctl restart docker
```

#### 2、docker-compose.yaml

> [!IMPORTANT]
>
> 执行前确保有权限！！！
>
> ```bash
> # 1. 停止容器
> docker compose stop
> 
> # 2. 清理旧目录（可选）
> sudo rm -rf ~/docker/docker_data/rabbitmq
> 
> # 3. 重建目录并设置权限
> mkdir -p ~/docker/docker_data/rabbitmq/{rabbit1,rabbit2,rabbit3}/{data,conf,log}
> sudo chown -R 999:999 ~/docker/docker_data/rabbitmq
> 
> # 4. 重新启动
> docker compose up -d
> ```

创建文件：`docker-compose.yaml`。

```yaml
name: 'rabbitmq-cluster'

services:
  rabbit1:
    image: rabbitmq:3.13.7-management
    container_name: rabbit1
    hostname: rabbit1
    ports:
      - "5672:5672"
      - "15672:15672"
    environment:
      - RABBITMQ_ERLANG_COOKIE=SECRETCOOKIE
      - RABBITMQ_NODENAME=rabbit@rabbit1
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
    volumes:
      - ~/docker/docker_data/rabbitmq/rabbit1/data:/var/lib/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit1/conf:/etc/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit1/log:/var/log/rabbitmq
    networks:
      - rabbitmq-cluster

  rabbit2:
    image: rabbitmq:3.13.7-management
    container_name: rabbit2
    hostname: rabbit2
    ports:
      - "5673:5672"
      - "15673:15672"
    environment:
      - RABBITMQ_ERLANG_COOKIE=SECRETCOOKIE
      - RABBITMQ_NODENAME=rabbit@rabbit2
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
    volumes:
      - ~/docker/docker_data/rabbitmq/rabbit2/data:/var/lib/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit2/conf:/etc/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit2/log:/var/log/rabbitmq
    networks:
      - rabbitmq-cluster
    depends_on:
      - rabbit1

  rabbit3:
    image: rabbitmq:3.13.7-management
    container_name: rabbit3
    hostname: rabbit3
    ports:
      - "5674:5672"
      - "15674:15672"
    environment:
      - RABBITMQ_ERLANG_COOKIE=SECRETCOOKIE
      - RABBITMQ_NODENAME=rabbit@rabbit3
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=admin
    volumes:
      - ~/docker/docker_data/rabbitmq/rabbit3/data:/var/lib/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit3/conf:/etc/rabbitmq
      - ~/docker/docker_data/rabbitmq/rabbit3/log:/var/log/rabbitmq
    networks:
      - rabbitmq-cluster
    depends_on:
      - rabbit1
      - rabbit2

networks:
  rabbitmq-cluster:
    external: true
```

#### 3、启动docker-compose

```bash
docker compose up -d
```

#### 4、加入集群

> [!IMPORTANT]
>
> 注意你的名称是否和我的一样，直接复制忽略。

```bash
# 将rabbit2加入集群
docker exec -it rabbit2 rabbitmqctl stop_app
docker exec -it rabbit2 rabbitmqctl reset
docker exec -it rabbit2 rabbitmqctl join_cluster rabbit@rabbit1
docker exec -it rabbit2 rabbitmqctl start_app

# 将rabbit3加入集群
docker exec -it rabbit3 rabbitmqctl stop_app
docker exec -it rabbit3 rabbitmqctl reset
docker exec -it rabbit3 rabbitmqctl join_cluster rabbit@rabbit1
docker exec -it rabbit3 rabbitmqctl start_app
```

> 成功的输出：
>
> ```bash
> bunny@bunny:~$ # 将rabbit2加入集群
> docker exec -it rabbit2 rabbitmqctl stop_app
> docker exec -it rabbit2 rabbitmqctl reset
> docker exec -it rabbit2 rabbitmqctl join_cluster rabbit@rabbit1
> docker exec -it rabbit2 rabbitmqctl start_app
> 
> # 将rabbit3加入集群
> docker exec -it rabbit3 rabbitmqctl stop_app
> docker exec -it rabbit3 rabbitmqctl reset
> docker exec -it rabbit3 rabbitmqctl join_cluster rabbit@rabbit1
> docker exec -it rabbit3 rabbitmqctl start_app
> Stopping rabbit application on node rabbit@rabbit2 ...
> Resetting node rabbit@rabbit2 ...
> Clustering node rabbit@rabbit2 with rabbit@rabbit1
> Starting node rabbit@rabbit2 ...
> Stopping rabbit application on node rabbit@rabbit3 ...
> Resetting node rabbit@rabbit3 ...
> Clustering node rabbit@rabbit3 with rabbit@rabbit1
> Starting node rabbit@rabbit3 ...
> ```

### 普通方式

#### 先决条件

> [!TIP]
>
> 所有节点使用相同版本的Erlang（可通过`erl -version`检查）

- 至少两台Ubuntu服务器（建议18.04 LTS或更高版本）
- 所有服务器之间网络互通
- 每台服务器上已安装相同版本的RabbitMQ
- 所有节点使用相同版本的Erlang

#### 1、安装RabbitMQ

在所有节点上执行以下步骤：

```bash
# 更新软件包列表
sudo apt update

# 安装必要的依赖
sudo apt install -y curl gnupg

# 移除之前添加的 packagecloud.io 仓库
sudo rm /etc/apt/sources.list.d/rabbitmq.list

# 添加 RabbitMQ 官方仓库密钥
sudo apt-get install curl gnupg apt-transport-https -y

## Team RabbitMQ's main signing key
curl -1sLf "https://keys.openpgp.org/vks/v1/by-fingerprint/0A9AF2115F4687BD29803A206B73A36E6026DFCA" | sudo gpg --dearmor | sudo tee /usr/share/keyrings/com.rabbitmq.team.gpg > /dev/null

## Cloudsmith: modern Erlang repository
curl -1sLf "https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-erlang/gpg.E495BB49CC4BBE5B.key" | sudo gpg --dearmor | sudo tee /usr/share/keyrings/io.cloudsmith.rabbitmq.E495BB49CC4BBE5B.gpg > /dev/null

## Cloudsmith: RabbitMQ repository
curl -1sLf "https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-server/gpg.9F4587F226208342.key" | sudo gpg --dearmor | sudo tee /usr/share/keyrings/io.cloudsmith.rabbitmq.9F4587F226208342.gpg > /dev/null

# 添加仓库源
sudo tee /etc/apt/sources.list.d/rabbitmq.list <<EOF
## Provides modern Erlang/OTP releases
deb [signed-by=/usr/share/keyrings/io.cloudsmith.rabbitmq.E495BB49CC4BBE5B.gpg] https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-erlang/deb/ubuntu $(lsb_release -cs) main
deb-src [signed-by=/usr/share/keyrings/io.cloudsmith.rabbitmq.E495BB49CC4BBE5B.gpg] https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-erlang/deb/ubuntu $(lsb_release -cs) main

## Provides RabbitMQ
deb [signed-by=/usr/share/keyrings/io.cloudsmith.rabbitmq.9F4587F226208342.gpg] https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-server/deb/ubuntu $(lsb_release -cs) main
deb-src [signed-by=/usr/share/keyrings/io.cloudsmith.rabbitmq.9F4587F226208342.gpg] https://dl.cloudsmith.io/public/rabbitmq/rabbitmq-server/deb/ubuntu $(lsb_release -cs) main
EOF

# 更新软件包列表
sudo apt-get update -y

# 安装 Erlang 和 RabbitMQ
sudo apt-get install -y erlang-base \
                        erlang-asn1 erlang-crypto erlang-eldap erlang-ftp erlang-inets \
                        erlang-mnesia erlang-os-mon erlang-parsetools erlang-public-key \
                        erlang-runtime-tools erlang-snmp erlang-ssl erlang-syntax-tools \
                        erlang-tftp erlang-tools erlang-xmerl

sudo apt-get install rabbitmq-server -y --fix-missing

# 启动RabbitMQ服务
sudo systemctl start rabbitmq-server
sudo systemctl enable rabbitmq-server

# 检查状态
sudo systemctl status rabbitmq-server
```

#### 2、配置主机名和hosts文件

> [!CAUTION]
>
> 所有主机上的`hostname`都必须和配置的一样，否则无法加入集群还会报错。
>
> 查看本机的hostname；如果输出的和配置的hosts的不一致，需要修改hostname。
>
> ```bash
> # 查看本机的hostname
> hostname
> 
> # 永久修改hostname（以rabbit1为例）
> sudo hostnamectl set-hostname rabbit1
> ```
>
> **例如：**
>
> - 主节点hostname为rabbit1
> - 192.168.3.145节点hostname为rabbit2
> - 192.168.3.146节点hostname为rabbit3
> - 以此类推

在所有节点上编辑`/etc/hosts`文件，确保包含所有集群节点的IP和主机名：

```bash
sudo vim /etc/hosts
```

添加类似以下内容（根据您的实际IP和主机名修改）：

```bash
192.168.3.144 rabbit1
192.168.3.145 rabbit2
192.168.3.146 rabbit3
192.168.3.147 rabbit4
```

#### 3、设置Erlang Cookie

> [!IMPORTANT]
>
> 普通方式搭建集群时，`.erlang.cookie`的权限必须为`400`
>
> ```bash
> sudo chmod 400 /var/lib/rabbitmq/.erlang.cookie
> ```

RabbitMQ节点使用Erlang cookie进行认证，所有节点必须使用相同的cookie。

如果是克隆的虚拟机，可以不检查（最好还是检查下），因为克隆的基本上是一样的。

选择一个节点作为主节点，将其cookie复制到其他节点：

```bash
# 在主节点上查看cookie
sudo cat /var/lib/rabbitmq/.erlang.cookie

# 在其他节点上停止RabbitMQ服务
sudo systemctl stop rabbitmq-server

# 编辑或创建cookie文件
sudo nano /var/lib/rabbitmq/.erlang.cookie

# 粘贴主节点的cookie内容
sudo chown rabbitmq:rabbitmq /var/lib/rabbitmq/.erlang.cookie
# 然后设置正确的权限
sudo chmod 400 /var/lib/rabbitmq/.erlang.cookie

# 重新启动RabbitMQ服务
sudo systemctl start rabbitmq-server
```

#### 4、加入集群

> [!NOTE]
>
> 加入集群时，必须要是这样的格式`sudo rabbitmqctl join_cluster rabbit@主节点名称`

在非主节点上执行以下命令加入集群：

> [!WARNING]
> `reset`命令会清除该节点的所有数据和配置，生产环境慎用

```bash
# 停止RabbitMQ应用（不停止服务）
sudo rabbitmqctl stop_app

# 重置节点（如果是新安装可以跳过，生产环境谨慎使用）
sudo rabbitmqctl reset

# 加入集群（将rabbit1替换为您的主节点主机名）
sudo rabbitmqctl join_cluster rabbit@rabbit1

# 启动RabbitMQ应用
sudo rabbitmqctl start_app
```

> [!CAUTION]
>
> 如果因为别的原因弄乱了，可以删除原来数据
>
> 仔细甄别下面是否有你需要的！！！
>
> ```bash
> # 停止 RabbitMQ
> sudo systemctl stop rabbitmq-server
> 
> # 删除旧数据
> sudo rm -rf /var/lib/rabbitmq/mnesia/
> 
> # 确保 cookie 与其他节点一致
> sudo cat /var/lib/rabbitmq/.erlang.cookie  # 必须与其他节点相同
> 
> # 启动服务
> sudo systemctl start rabbitmq-server
> 
> # 加入集群（使用新主机名）
> sudo rabbitmqctl stop_app
> sudo rabbitmqctl reset
> sudo rabbitmqctl join_cluster rabbit@rabbit1  # 假设 rabbit1 是主节点
> sudo rabbitmqctl start_app
> ```

#### 5、验证集群状态

在任何节点上执行以下命令检查集群状态：

```bash
sudo rabbitmqctl cluster_status

# OR 查看集群节点列表
sudo rabbitmqctl cluster_status | grep -A10 'running_nodes'

# 检查服务状态
sudo rabbitmqctl status
```

> 为了提高可用性，可以设置镜像队列策略：
>
> ```bash
> sudo rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}'
> ```

#### 6、启用管理插件（可选）

- 如果需要Web管理界面：

> [!IMPORTANT]
>
> 如果使用Web界面，需要在每个集群上都执行一下，否则情况会出现下面情况，举个例子。
>
> 如果在主节点上执行，那么主节点可以访问，之后输入账户密码登录之后可以看到主节点的情况，但是其余节点会出现【Node statistics not available】。
>
> 为了避免这种情况，要在每个节点上都执行一下下面的命令。

```bash
sudo rabbitmq-plugins enable rabbitmq_management
```

然后可以通过`http://<node-ip>:15672`访问，默认用户名/密码为`guest/guest`（建议更改）。

如果需要开启防火墙

```bash
# 开放15672端口（如果使用防火墙）
sudo ufw allow 15672/tcp
```

> [!TIP]
>
> 如果到页面中查看，没有用户或者没有权限可以通过下面方式进行更改或添加。

如果需要添加用户，在你安装管理界面的机器上运行。

```bash
# 如果存在可以先删除
sudo rabbitmqctl delete_user 用户名

# 添加用户
sudo rabbitmqctl add_user 用户名 密码

#设置用户操作权限
sudo rabbitmqctl set_user_tags 用户名 administrator
```

**示例**

```bash
# 如果存在可以先删除
sudo rabbitmqctl delete_user admin

# 添加用户
sudo rabbitmqctl add_user admin admin

#设置用户操作权限
sudo rabbitmqctl set_user_tags admin administrator
```

- **修改配置文件**（如 `/etc/hosts`、防火墙规则等）需在所有节点操作。
- **重启 RabbitMQ 服务**（如果修改了核心配置）：

```bash
sudo systemctl restart rabbitmq-server
```

### 常见问题

Q：打开主界面没有用户可以登录。

A：添加或者创建新用户，参考【普通方式->第六点】

Q：打开之后显示【⚠ All stable feature flags must be enabled after completing an upgrade. 】

```bash
# 查看当前 Feature Flags 状态
sudo rabbitmqctl list_feature_flags

# 启用所有稳定的 Feature Flags，这会启用所有 稳定（stable） 的功能标志（不包括实验性功能）。
sudo rabbitmqctl enable_feature_flag all

# 检查是否成功启用
sudo rabbitmqctl list_feature_flags

# 重启 RabbitMQ（可选）
sudo systemctl restart rabbitmq-server
```

Q：出现下面情况【Node statistics not available】

![image-20250519224201895](./images/image-20250519224201895.png)

A：解决方法

**情况一 管理插件未在所有节点启用**

```bash
sudo rabbitmq-plugins enable rabbitmq_management
sudo systemctl restart rabbitmq-server
```

**情况二 **

如果节点之间无法正常通信（如防火墙、网络策略阻止），管理界面无法获取其他节点的统计信息。

**检查方法**

在任意节点执行：

```bash
sudo rabbitmqctl cluster_status
```

**检查防火墙**（确保 `4369`、`25672`、`5672`、`15672` 端口开放）：

```bash
sudo ufw allow 4369/tcp   # Erlang 分布式通信端口
sudo ufw allow 25672/tcp  # RabbitMQ 集群通信端口
sudo ufw allow 5672/tcp   # AMQP 协议端口
sudo ufw allow 15672/tcp  # 管理界面端口
sudo ufw reload
```

**情况三 Cookie 不一致**

在所有节点执行：

```bash
sudo cat /var/lib/rabbitmq/.erlang.cookie
```

1. 停止 RabbitMQ：

   ```bash
   sudo systemctl stop rabbitmq-server
   ```

2. 修改 Cookie（复制主节点的 Cookie 到其他节点）：

   ```bash
   echo "YOUR_MASTER_NODE_COOKIE" | sudo tee /var/lib/rabbitmq/.erlang.cookie
   ```

3. 设置权限：

   ```bash
   sudo chown rabbitmq:rabbitmq /var/lib/rabbitmq/.erlang.cookie
   sudo chmod 400 /var/lib/rabbitmq/.erlang.cookie
   ```

4. 重启 RabbitMQ：

   ```bash
   sudo systemctl start rabbitmq-server
   ```

**情况四 管理界面用户权限问题**

如果用户没有 **跨节点访问权限**，管理界面可能无法获取其他节点的数据。

**解决方案**

确保用户在所有节点都有 **管理员权限**：

```bash
sudo rabbitmqctl set_user_tags <username> administrator
sudo rabbitmqctl set_permissions -p / <username> ".*" ".*" ".*"
```

例如：

```bash
sudo rabbitmqctl set_user_tags admin administrator
sudo rabbitmqctl set_permissions -p / admin ".*" ".*" ".*"
```

**情况五 管理界面缓存问题**

浏览器可能缓存了旧数据，导致显示异常。

### 集群环境-负载均衡

#### 1. 安装 HAProxy
```bash
sudo apt update
sudo apt install haproxy -y
```

#### 2. 配置 HAProxy
编辑 HAProxy 的配置文件 `/etc/haproxy/haproxy.cfg`：
```bash
sudo vim /etc/haproxy/haproxy.cfg
```
添加以下内容（假设 RabbitMQ 节点为 `rabbit1:5672` 和 `rabbit2:5672`）：

> [!IMPORTANT]
>
> 绑定的端口号（`bind *:`）不能重复！！！

```ini
frontend rabbitmq_front
    bind *:22222
    mode tcp
    default_backend rabbitmq_back

backend rabbitmq_back
    mode tcp
    balance roundrobin
    # 在之前设置了hosts文件，所以这里没有写端口号 == server rabbit1 192.168.3.144:5672 check
    server rabbit1 rabbit1:5672 check
    server rabbit2 rabbit2:5672 check
    server rabbit3 rabbit3:5672 check
    server rabbit4 rabbit4:5672 check

# 可选：启用 RabbitMQ 管理界面的负载均衡（默认端口 15672）
frontend rabbitmq_admin
    bind *:12222
    mode http
    default_backend rabbitmq_admin_back

backend rabbitmq_admin_back
    mode http
    balance roundrobin
    # 在之前设置了hosts文件，所以这里没有写端口号 == server rabbit1 192.168.3.144:15672 check
    server rabbit1 rabbit1:15672 check
    server rabbit2 rabbit2:15672 check
    server rabbit3 rabbit3:15672 check
    server rabbit4 rabbit4:15672 check
```

#### 3. 重启 HAProxy

> [!TIP]
>
> 重启报错可以看下报错内容: journalctl -xeu haproxy.service
>
> 如果使用一些远程软件，修改时可能会出现，多行配置变一行问题，需要手动通过vim查看。

```bash
sudo systemctl restart haproxy
```
### 集群环境的连接

> [!NOTE]
>
> 如果是新建环境，需要将之前写的监听内容注释，否则启动容易报错。

使用Java程序进行连接。如果搭建了集群环境还配置了负载均衡，就是用负载均衡的方式进行连接。

负载均衡配置的是端口是：`5672`对应上面的文件是`22222`。所以在连接时候是`22222`这个 端口（参考【2. 配置 HAProxy】）

#### Java配置

```yaml
# application.yaml
rabbitmq:
  host: ${bunny.rabbitmq.host}
  port: ${bunny.rabbitmq.port}
  username: ${bunny.rabbitmq.username}
  password: ${bunny.rabbitmq.password}
  virtual-host: ${bunny.rabbitmq.virtual-host}

# application-dev.yaml
bunny:
  rabbitmq:
    host: 192.168.3.144
    # port: 5672
    # 集群环境的端口号
    port: 22222
    virtual-host: /
    username: admin
    password: admin
```

#### 创建环境

**交换机：**exchange.cluster.test

![image-20250520105125396](./images/image-20250520105125396.png)

**队列：**queue.cluster.test

![image-20250520105109623](./images/image-20250520105109623.png)

**路由键：**routing.key.cluster.test

![image-20250520105149567](./images/image-20250520105149567.png)

## 集群中的队列

### 镜像队列

最新版本中已经退出历史舞台了，就不说了。

### 仲裁队列

当我们在集群中创建队列时，如【集群环境的连接】那一节，可以看到当前的队列是位于，第一个节点上的。如果这时候服务器宕机或者其他情况，这时这个节点上数据没了，其它j节点时访问不到的；我们理想的情况是，如果一台服务器宕机，这台服务器上的所有内容在别的节点上都是可以看到的。

![image-20250520141530322](./images/image-20250520141530322.png)

#### 创建仲裁队列

**交换机：**exchange.quorum.test

**队列：**queue.quorum.test

**l路由键：**routing.key.quorum.test

![image-20250520141821885](./images/image-20250520141821885.png)

#### 测试Code

**生产者**

```java
/* 仲裁队列发送消息 */
@Test
void clusterQuorumTest2() {
    final String EXCHANGE = "exchange.quorum.test";
    final String ROUTING_KEY = "routing.key.quorum.test";

    for (int i = 0; i <= 10; i++) {
        rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "仲裁队列发送消息-" + i);
    }
}
```

**消费者**

```java
/* 仲裁队列监听 */
@RabbitListener(queues = "queue.quorum.test")
public void processMessage(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    log.info("<<仲裁队列监听下消息>>----{}", dataString);
    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
}
```

### 联邦队列

Federation插件的设计目标是使RabbitMQ在不同的Broker节点之间进行消息传递而无须建立集群。

它可以在不同的管理域中的Broker或集群间传递消息，这些管理域可能设置了不同的用户和vhost，也可能运行在不同版本的RabbitMQ和Erlang上。Federation基于AMQP0-9-1协议在不同的Broker之间进行通信，并且设计成能够容忍不稳定的网络连接情况。

- 各节点操作：启用联邦插件
- 下游操作：
  - 添加上游连接端点
  - 创建控制策略

> [!NOTE]
>
> 因为联邦队列不是集群，需要两个不同rabbit，比如说，一个服务器在上海，一个在杭州，两个不同的服务器且不是集群环境。
>
> 所以在创建时候不要加入集群。

#### 创建docker容器

**设置权限**

```bash
sudo chown -R 999:999 ~/docker/docker_data/rabbitmq/rabbitmq_master
sudo chown -R 999:999 ~/docker/docker_data/rabbitmq/rabbitmq_2
```

**创建容器**

```bash
# 第一个
docker run -d --name rabbitmq_master --restart=always \
-p 5672:5672 -p 15672:15672   \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/data:/var/lib/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/conf:/etc/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_master/log:/var/log/rabbitmq \
-e RABBITMQ_DEFAULT_VHOST=rabbitmq_master \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.13.7-management

# 第二个
docker run -d --name rabbitmq_2 --restart=always \
-p 5673:5672 -p 15673:15672   \
-v ~/docker/docker_data/rabbitmq/rabbitmq_2/data:/var/lib/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_2/conf:/etc/rabbitmq \
-v ~/docker/docker_data/rabbitmq/rabbitmq_2/log:/var/log/rabbitmq \
-e RABBITMQ_DEFAULT_VHOST=rabbitmq_2 \
-e RABBITMQ_DEFAULT_USER=admin \
-e RABBITMQ_DEFAULT_PASS=admin rabbitmq:3.13.7-management
```

#### 启动联邦插件

> [!NOTE]
>
> 联邦插件并不需要下载启动即可。

> [!IMPORTANT]
>
> 创建时候需要重新设置Virtual Hosts否则设置了内容会显示`not_allow`，不能使用默认的Virtual Hosts。

```bash
# 执行下面命令进行开启
rabbitmq-plugins enable rabbitmq_federation
rabbitmq-plugins enable rabbitmq_federation_mangement
```

不进入docker容器直接运行方式

```bash
# 开启全部插件防止警告
docker exec -it rabbitmq_master rabbitmqctl enable_feature_flag all
docker exec -it rabbitmq_2 rabbitmqctl enable_feature_flag all

docker exec -it rabbitmq_master rabbitmq-plugins enable rabbitmq_federation
docker exec -it rabbitmq_master rabbitmq-plugins enable rabbitmq_federation_management

docker exec -it rabbitmq_2 rabbitmq-plugins enable rabbitmq_federation
docker exec -it rabbitmq_2 rabbitmq-plugins enable rabbitmq_federation_management
```

![image-20250520145425061](./images/image-20250520145425061.png)

启用成功后会有下面两个选项

![image-20250520145937321](./images/image-20250520145937321.png)

在此页面中添加

![image-20250520150530165](./images/image-20250520150530165.png)

上图输入框中的内容

```bash
# 连接名称
bunny.upstream

# 连接地址
amqp://admin:admin@172.17.0.1:5672
```

创建Policies

![image-20250520150907385](./images/image-20250520150907385.png)

上图输入框内容

```bash
name: policy.federation.exchange
Pattern: ^federation\.
Apply to: Exchanges
Priority: 10 

# 这里的federation-upstream 是我们之前创建的联邦名称，详细看上面【上图的，输入框中的内容】
federation-upstream bunny.upstream
```

#### 创建交换机

> [!IMPORTANT]
>
> 创建联邦交换机时，需要匹配上面写的`Pattern`，如果按照文档创建，那么名称需要是：`federation.xxx.xxxxx`这种格式，反正肯定是以`federation`开头的。
>
> 其次路由键要一致，队列名称可以不一样。

| 所在机房                      | 交换机名称               | 路由键                | 队列名称               |
| ----------------------------- | ------------------------ | --------------------- | ---------------------- |
| rabbitmq_master（你的上游MQ） | federation.exchange.demo | routing.key.demo.test | queue.normal.master    |
| rabbitmq2（你的下游MQ）       | federation.exchange.demo | routing.key.demo.test | queue.normal.rabbitmq2 |

> [!NOTE]
>
> 但此时发现下游节点中联邦队列并没有接收到消息，这是为什么呢？这里就体现出了联邦队列和联邦交换机工作逻辑的区别。
> 对联邦队列来说，如果没有监听联邦队列的消费端程序，它是不会到上游去拉取消窶的！
> 如果有消费端监听联邦队列，那么首先消费联邦队列自身的消息；如果联邦队列为空，这时候才会到上游队列节点中拉取消息。
> 所以现在的测试效果需要消费端程序配合才能看到：
