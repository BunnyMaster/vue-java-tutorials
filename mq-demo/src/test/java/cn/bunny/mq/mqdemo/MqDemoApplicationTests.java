package cn.bunny.mq.mqdemo;

import cn.bunny.mq.mqdemo.domain.RabbitMQMessageListenerConstants;
import cn.bunny.mq.mqdemo.domain.entity.Bunny;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    /* 测试失败交换机的情况 */
    @Test
    void publishExchangeErrorTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "----失败的消息发送----");

        Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
        rabbitTemplate.convertAndSend(exchangeDirect + "~", routingKeyDirect, JSON.toJSONString(bunny));
    }


    /* 测试失败队列的情况 */
    @Test
    void publishQueueErrorTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "----失败的队列发送----");

        Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect + "~", JSON.toJSONString(bunny));
    }
}
