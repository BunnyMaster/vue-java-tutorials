package cn.bunny.mq.mqdemo.mq.listener;

import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ClusterEnvironmentListener {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /* 测试优先级队列 */
    @RabbitListener(queues = "queue.cluster.test")
    public void processMessagePriority(String dataString, Channel channel, Message message) throws IOException, InterruptedException {
        log.info("<<集群环境下消息>>----<cluster>{}", dataString);
    }
}
