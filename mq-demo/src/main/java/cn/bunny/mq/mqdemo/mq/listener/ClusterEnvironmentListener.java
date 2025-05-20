package cn.bunny.mq.mqdemo.mq.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClusterEnvironmentListener {

    // /* 测试优先级队列 */
    // @RabbitListener(queues = "queue.cluster.test")
    // public void processMessagePriority(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    //     log.info("<<集群环境下消息>>----<cluster>{}", dataString);
    //     channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    // }
    //
    // /* 仲裁队列监听 */
    // @RabbitListener(queues = "queue.quorum.test")
    // public void processMessage(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
    //     log.info("<<仲裁队列监听下消息>>----{}", dataString);
    //     channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    // }
}
