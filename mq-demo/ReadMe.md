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