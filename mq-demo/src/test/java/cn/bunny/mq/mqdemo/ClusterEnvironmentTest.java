package cn.bunny.mq.mqdemo;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ClusterEnvironmentTest {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /* 集群环境下消息 */
    @Test
    void clusterTest() {
        for (int i = 0; i <= 10; i++) {
            rabbitTemplate.convertAndSend("exchange.cluster.test",
                    "routing.key.cluster.test",
                    "集群环境下消息-" + i);
        }
    }
}
