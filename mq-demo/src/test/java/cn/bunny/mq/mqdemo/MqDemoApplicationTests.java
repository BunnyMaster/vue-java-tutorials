package cn.bunny.mq.mqdemo;

import cn.bunny.mq.mqdemo.domain.RabbitMQMessageListenerConstants;
import cn.bunny.mq.mqdemo.domain.entity.Bunny;
import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    }

    /* 测试失败交换机的情况 */
    @Test
    void publishExchangeErrorTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;

        Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
        rabbitTemplate.convertAndSend(exchangeDirect + "~", routingKeyDirect, JSON.toJSONString(bunny));
    }


    /* 测试失败队列的情况 */
    @Test
    void publishQueueErrorTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;

        Bunny bunny = Bunny.builder().rabbitName("Bunny").age(2).build();
        rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect + "~", JSON.toJSONString(bunny));
    }

    /* 发送消息，发送多条消息，测试使用 */
    @Test
    void buildMessageTest() {
        String exchangeDirect = RabbitMQMessageListenerConstants.EXCHANGE_DIRECT;
        String routingKeyDirect = RabbitMQMessageListenerConstants.ROUTING_KEY_DIRECT;

        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(exchangeDirect, routingKeyDirect, "测试消息发送【" + i + "】");
        }
    }

    /* 测试过期时间 */
    @Test
    void buildExchangeTimeoutTest1() {
        String EXCHANGE = "exchange.test.timeout";
        String ROUTING_KEY = "routing.key.test.timeout";

        for (int i = 0; i < 100; i++) {
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "测试消息超时时间【" + i + "】");
        }
    }

    /* 测试过期时间---消息层面实现消息超时自动删除 */
    @Test
    void buildExchangeTimeoutTest2() {
        String EXCHANGE = "exchange.test.timeout";
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

    /* 因超时或移除产生死信 */
    @Test
    void buildExchangeOverflowTest() {
        String EXCHANGE = "exchange.normal.video";
        String ROUTING_KEY = "routing.key.normal.video";

        for (int i = 0; i < 40; i++) {
            rabbitTemplate.convertAndSend(EXCHANGE, ROUTING_KEY, "因超时或移除产生死信【" + i + "】");
        }
    }

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
}
