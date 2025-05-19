package cn.bunny.mq.mqdemo.mq.listener;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static cn.bunny.mq.mqdemo.domain.RabbitMQMessageListenerConstants.QUEUE_NAME;

@Component
@Slf4j
public class MessageListenerOrder {

    // /* 测试这个，需要注释下main那个 */
    // @RabbitListener(bindings = @QueueBinding(
    //         exchange = @Exchange(value = EXCHANGE_DIRECT),
    //         value = @Queue(value = QUEUE_NAME, durable = "true"),
    //         key = ROUTING_KEY_DIRECT,
    //         arguments = @Argument(name = "alternate-exchange", value = ALTERNATE_EXCHANGE_BACKUP)
    // )
    // )
    // public void processMessage(String dataString, Message message, Channel channel) {
    //     System.out.println("消费端接受消息：" + dataString);
    // }

    // /* 如果测试这个需要注释上面那个 */
    // @RabbitListener(queues = {QUEUE_NAME})
    // public void processQueue(String dataString, Message message, Channel channel) throws IOException {
    //     // 设置deliverTag
    //     // 消费端把消息处理结果ACK、NACK、Reject等返回给Broker之后，Broker需要对对应的消息执行后续操作。
    //     // 例如删除消息、重新排队或标记为死信等等那么Broker就必须知道它现在要操作的消息具体是哪一条。
    //     // 而deliveryTag作为消息的唯一标识就很好的满足了这个需求。
    //     long deliveryTag = message.getMessageProperties().getDeliveryTag();
    //
    //     try {
    //         // 核心操作
    //         System.out.println("消费端 消息内容：" + dataString);
    //         channel.basicAck(deliveryTag, false);
    //
    //         // 核心操作完成，返回ACK信息
    //     } catch (Exception e) {
    //         // 当前参数是否是重新投递的，为true时重复投递过了，为法拉瑟是第一次投递
    //         Boolean redelivered = message.getMessageProperties().getRedelivered();
    //
    //         // 第三个参数：
    //         // true：重新放回队列，broker会重新投递这个消息
    //         // false：不重新放回队列，broker会丢弃这个消息
    //         channel.basicNack(deliveryTag, false, !redelivered);
    //
    //         // 除了 basicNack 外还有 basicReject，其中 basicReject 不能控制是否批量操作
    //         channel.basicReject(deliveryTag, true);
    //
    //         // 核心操作失败，返回NACK信息
    //         throw new RuntimeException(e);
    //     }
    // }

    @RabbitListener(queues = {QUEUE_NAME})
    public void processMessagePrefetch(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
        log.info("消费者 消息内容：{}", dataString);

        TimeUnit.SECONDS.sleep(1);

        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

}
