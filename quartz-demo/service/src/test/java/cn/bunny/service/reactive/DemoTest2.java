package cn.bunny.service.reactive;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

public class DemoTest2 {
    public static void main(String[] args) throws InterruptedException {
        // 1. 定义发布者
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>();

        // 2. 绑定订阅
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
            private Flow.Subscription subscription;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                // 输出接受消息
                System.out.println("在订阅消息：" + subscription);

                // 将当前订阅赋值
                this.subscription = subscription;
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(String item) {
                System.out.println("在下一个接受时：" + item);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("接收者接受到错误信号");
            }

            @Override
            public void onComplete() {
                System.out.println("订阅完成了。。。");
            }
        };

        // 3. 发布者先绑定中间处理器，处理器绑定发布者
        // MyProcessor processor = new MyProcessor();
        // publisher.subscribe(processor);
        // processor.subscribe(subscriber);

        // 3. 假设处理器有很多
        MyProcessor processor1 = new MyProcessor();
        MyProcessor processor2 = new MyProcessor();
        MyProcessor processor3 = new MyProcessor();
        publisher.subscribe(processor1);
        processor1.subscribe(processor2);
        processor2.subscribe(processor3);
        processor3.subscribe(subscriber);


        // 使用发布者发布消息
        for (int i = 0; i < 10; i++) {
            publisher.submit("提交内哦容：" + i);
        }
        Thread.sleep(2000);

        // 发布者关闭
        publisher.close();
    }

    public static class MyProcessor extends SubmissionPublisher<String> implements Flow.Processor<String, String> {
        Flow.Subscription subscription;

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            System.out.println("中间处理器订阅。。。");
            subscription.request(Long.MAX_VALUE);
        }

        @Override
        public void onNext(String item) {
            System.out.println("处理器拿到数据" + item);
            item += "你看我开心吗？";
            submit(item);
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("中间处理器出错了。。。");
        }

        @Override
        public void onComplete() {
            System.out.println("中间处理器，处理完了。。。");
        }
    }
}
