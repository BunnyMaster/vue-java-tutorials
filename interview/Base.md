# Java面试

## 高频面试题

### Bean生命周期

#### Spring Bean生命周期主要阶段

1. **实例化(Instantiation)**：
   - 通过构造函数或工厂方法创建Bean实例
   - 对应`new`关键字或`FactoryBean`的`getObject()`方法

2. **属性赋值(Populate properties)**：
   - Spring容器注入Bean的依赖项(通过setter、构造函数或自动装配)

3. **BeanNameAware的setBeanName()**：
   - 如果Bean实现了`BeanNameAware`接口，会调用`setBeanName()`方法

4. **BeanFactoryAware的setBeanFactory()**：
   - 如果Bean实现了`BeanFactoryAware`接口，会调用`setBeanFactory()`方法

5. **ApplicationContextAware的setApplicationContext()**：
   - 如果Bean实现了`ApplicationContextAware`接口，会调用`setApplicationContext()`方法

6. **预初始化(BeanPostProcessor的postProcessBeforeInitialization())**：
   - 任何实现了`BeanPostProcessor`接口的Bean会在此处调用`postProcessBeforeInitialization()`方法

7. **初始化(Initialization)**：
   - 如果Bean实现了`InitializingBean`接口，调用`afterPropertiesSet()`方法
   - 如果配置了自定义的init方法(如`init-method`或`@PostConstruct`)，调用该方法

8. **后初始化(BeanPostProcessor的postProcessAfterInitialization())**：
   - 任何实现了`BeanPostProcessor`接口的Bean会在此处调用`postProcessAfterInitialization()`方法

9. **使用中(Ready for use)**：
   - Bean已经完全初始化，可以被应用程序使用

10. **销毁(Destruction)**：
    - 如果Bean实现了`DisposableBean`接口，调用`destroy()`方法
    - 如果配置了自定义的destroy方法(如`destroy-method`或`@PreDestroy`)，调用该方法

#### 常用生命周期回调方式

1. **注解方式**：
   - `@PostConstruct`：初始化方法
   - `@PreDestroy`：销毁方法

2. **接口方式**：
   - `InitializingBean`和`afterPropertiesSet()`
   - `DisposableBean`和`destroy()`

3. **XML配置方式**：
   - `init-method`和`destroy-method`属性

#### 示例代码

```java
public class ExampleBean implements BeanNameAware, InitializingBean, DisposableBean {
    
    private String name;
    
    // 1. 构造函数
    public ExampleBean() {
        System.out.println("1. 构造函数调用 - Bean实例化");
    }
    
    // 2. 属性注入
    public void setName(String name) {
        System.out.println("2. 属性注入 - setName()");
        this.name = name;
    }
    
    // 3. BeanNameAware
    @Override
    public void setBeanName(String name) {
        System.out.println("3. BeanNameAware.setBeanName()");
    }
    
    // 4. @PostConstruct
    @PostConstruct
    public void postConstruct() {
        System.out.println("4. @PostConstruct方法");
    }
    
    // 5. InitializingBean
    @Override
    public void afterPropertiesSet() {
        System.out.println("5. InitializingBean.afterPropertiesSet()");
    }
    
    // 6. 自定义init方法
    public void initMethod() {
        System.out.println("6. 自定义init方法");
    }
    
    // 7. @PreDestroy
    @PreDestroy
    public void preDestroy() {
        System.out.println("7. @PreDestroy方法");
    }
    
    // 8. DisposableBean
    @Override
    public void destroy() {
        System.out.println("8. DisposableBean.destroy()");
    }
    
    // 9. 自定义destroy方法
    public void destroyMethod() {
        System.out.println("9. 自定义destroy方法");
    }
}
```

#### 面试回答技巧

1. 从简单到详细：先概述主要阶段，再深入细节
2. 结合实际经验：可以提到你在项目中如何使用生命周期回调
3. 区分容器管理和普通Java对象：强调Spring容器对生命周期的管理
4. 提及相关扩展点：如`BeanPostProcessor`、`BeanFactoryPostProcessor`等

记住，不同版本的Spring框架可能会有细微差别，但核心生命周期阶段基本保持一致。

### AOP（面向切面编程）

#### AOP核心概念

1. **切面(Aspect)**：横切关注点的模块化，包含通知和切点
2. **连接点(Join Point)**：程序执行过程中的特定点，如方法调用或异常抛出
3. **通知(Advice)**：在切面的某个连接点上执行的动作
4. **切点(Pointcut)**：匹配连接点的谓词，确定通知应该应用到哪些连接点
5. **引入(Introduction)**：向现有类添加新方法或属性
6. **目标对象(Target Object)**：被一个或多个切面通知的对象
7. **AOP代理(AOP Proxy)**：由AOP框架创建的对象，用于实现切面契约

#### AOP实现原理

##### 1. 代理模式

AOP的核心实现基于**代理模式**，主要有两种实现方式：

(1) JDK动态代理

- **适用场景**：目标对象实现了至少一个接口
- **实现方式**：
  - 通过`java.lang.reflect.Proxy`类创建代理对象
  - 实现`InvocationHandler`接口处理代理逻辑
- **特点**：
  - 基于接口代理
  - Java原生支持，无需额外依赖
  - 性能较好

(2) CGLIB代理

- **适用场景**：目标对象没有实现接口
- **实现方式**：
  - 通过继承目标类生成子类
  - 重写父类方法实现代理逻辑
- **特点**：
  - 基于类代理
  - 需要引入CGLIB库
  - 不能代理final类和方法

##### 2. Spring AOP实现机制

Spring AOP默认使用以下策略：
- 如果目标对象实现了接口，使用JDK动态代理
- 如果目标对象没有实现接口，使用CGLIB代理
- 可以通过配置强制使用CGLIB

##### 3. 通知类型

1. **前置通知(Before advice)**：在连接点之前执行
2. **后置通知(After returning advice)**：连接点正常完成后执行
3. **异常通知(After throwing advice)**：连接点抛出异常后执行
4. **最终通知(After (finally) advice)**：连接点完成后执行（无论正常或异常）
5. **环绕通知(Around advice)**：包围连接点，可以控制是否执行连接点

#### AOP工作流程

1. **解析切面配置**：读取@Aspect注解或XML配置
2. **创建代理对象**：
   - 根据目标对象选择代理方式(JDK或CGLIB)
   - 生成代理类字节码
   - 实例化代理对象
3. **拦截方法调用**：
   - 当调用代理对象方法时
   - 检查该方法是否匹配切点表达式
   - 如果匹配，按顺序执行相关通知
4. **执行目标方法**：
   - 在环绕通知中通过`ProceedingJoinPoint.proceed()`调用目标方法
   - 或由代理直接调用目标方法

#### Spring AOP与AspectJ比较

| 特性     | Spring AOP           | AspectJ                          |
| -------- | -------------------- | -------------------------------- |
| 实现方式 | 运行时代理           | 编译时/加载时编织                |
| 性能     | 较慢                 | 更快                             |
| 功能     | 仅支持方法级别的切点 | 支持字段、构造器、方法等更多切点 |
| 依赖     | Spring容器           | 需要AspectJ编译器或织入器        |
| 学习曲线 | 较简单               | 较复杂                           |
| 适用场景 | 大多数Spring应用     | 需要更强大AOP功能的场景          |

#### 示例代码

```java
// 定义切面
@Aspect
@Component
public class LoggingAspect {
    
    // 定义切点
    @Pointcut("execution(* com.example.service.*.*(..))")
    private void serviceLayer() {}
    
    // 前置通知
    @Before("serviceLayer()")
    public void beforeAdvice(JoinPoint joinPoint) {
        System.out.println("Before method: " + joinPoint.getSignature().getName());
    }
    
    // 环绕通知
    @Around("serviceLayer()")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around before: " + joinPoint.getSignature().getName());
        Object result = joinPoint.proceed();
        System.out.println("Around after: " + joinPoint.getSignature().getName());
        return result;
    }
}
```

#### 面试回答技巧

1. **分层回答**：先讲核心概念，再深入实现原理
2. **结合实际**：举例说明你在项目中如何使用AOP解决实际问题
3. **对比分析**：比较不同实现方式的优缺点
4. **扩展知识**：可以提到AOP的适用场景(日志、事务、安全等)
5. **性能考量**：讨论代理方式的选择对性能的影响

记住，AOP是Spring框架的核心功能之一，理解其原理对于掌握Spring至关重要。

### 事务传播机制详解

事务传播机制是Spring事务管理中的重要概念，它定义了在多个事务方法相互调用时，事务应该如何传播。

#### 七种传播行为

Spring定义了7种事务传播行为，通过`Propagation`枚举表示：

1. **REQUIRED（默认）**：
   - 如果当前存在事务，则加入该事务
   - 如果当前没有事务，则创建一个新事务
   - **适用场景**：大多数业务方法使用此级别

2. **SUPPORTS**：
   - 如果当前存在事务，则加入该事务
   - 如果当前没有事务，则以非事务方式执行
   - **适用场景**：查询方法，可有可无事务

3. **MANDATORY**：
   - 如果当前存在事务，则加入该事务
   - 如果当前没有事务，则抛出异常
   - **适用场景**：必须运行在事务中的方法

4. **REQUIRES_NEW**：
   - 总是创建一个新事务
   - 如果当前存在事务，则挂起当前事务
   - **适用场景**：需要独立事务的方法，如日志记录

5. **NOT_SUPPORTED**：
   - 以非事务方式执行
   - 如果当前存在事务，则挂起当前事务
   - **适用场景**：不需要事务支持的方法

6. **NEVER**：
   - 以非事务方式执行
   - 如果当前存在事务，则抛出异常
   - **适用场景**：不允许在事务中运行的方法

7. **NESTED**：
   - 如果当前存在事务，则在嵌套事务内执行
   - 如果当前没有事务，则创建一个新事务
   - **特点**：
     - 嵌套事务是外部事务的子事务
     - 子事务回滚不会影响外部事务
     - 外部事务回滚会导致子事务回滚
   - **适用场景**：需要部分回滚的场景
   - **注意**：需要JDBC 3.0以上驱动支持

#### 传播行为对比表

| 传播行为      | 当前有事务 | 当前无事务 | 特点说明                 |
| ------------- | ---------- | ---------- | ------------------------ |
| REQUIRED      | 加入       | 创建新事务 | 默认设置，最常用         |
| SUPPORTS      | 加入       | 非事务执行 | 可有可无的事务           |
| MANDATORY     | 加入       | 抛出异常   | 强制要求存在事务         |
| REQUIRES_NEW  | 挂起并新建 | 创建新事务 | 完全独立的新事务         |
| NOT_SUPPORTED | 挂起       | 非事务执行 | 强制非事务执行           |
| NEVER         | 抛出异常   | 非事务执行 | 强制要求不能有事务       |
| NESTED        | 嵌套事务   | 创建新事务 | 嵌套在现有事务中的子事务 |

#### 典型应用场景

1. **REQUIRED**：
   ```java
   @Transactional(propagation = Propagation.REQUIRED)
   public void placeOrder(Order order) {
       // 订单处理逻辑
   }
   ```

2. **REQUIRES_NEW**（日志记录）：
   ```java
   @Transactional(propagation = Propagation.REQUIRES_NEW)
   public void logActivity(Activity activity) {
       // 活动日志记录，即使主事务回滚也要记录
   }
   ```

3. **NESTED**（部分回滚）：
   ```java
   @Transactional(propagation = Propagation.NESTED)
   public void updateInventory(Order order) {
       // 库存更新，可以独立回滚而不影响整个订单
   }
   ```

#### 实现原理

Spring事务传播机制的实现基于以下技术：

1. **事务管理器**（PlatformTransactionManager）
2. **事务定义**（TransactionDefinition）
3. **事务状态**（TransactionStatus）
4. **线程绑定**（ThreadLocal保存当前事务状态）

当方法调用发生时：
1. 检查当前线程是否存在事务
2. 根据传播行为决定是加入、创建新事务还是挂起当前事务
3. 通过TransactionSynchronizationManager管理事务资源

#### 常见面试问题示例

**Q: REQUIRED和REQUIRES_NEW有什么区别？**

A: 
- REQUIRED会加入当前事务（如果存在），而REQUIRES_NEW总是创建新事务并挂起当前事务（如果存在）
- REQUIRED的事务是同一个物理事务，REQUIRES_NEW创建的是完全独立的新事务
- REQUIRED中内部方法回滚会导致外部事务回滚，而REQUIRES_NEW的内部方法回滚不会影响外部事务

**Q: NESTED和REQUIRES_NEW有什么区别？**

A:
- NESTED是嵌套事务，REQUIRES_NEW是独立新事务
- NESTED事务的回滚不会影响外部事务，但外部事务回滚会导致NESTED事务回滚
- REQUIRES_NEW事务完全独立，内外事务互不影响
- NESTED事务会使用保存点(Savepoint)实现部分回滚

#### 最佳实践建议

1. 大多数业务方法使用默认的REQUIRED传播行为
2. 需要独立事务的操作（如日志记录）使用REQUIRES_NEW
3. 需要部分回滚能力的场景考虑使用NESTED
4. 查询方法可以使用SUPPORTS以减少事务开销
5. 明确不需要事务的方法使用NOT_SUPPORTED
6. 避免滥用REQUIRES_NEW，因为它会带来更多数据库连接开销

### 分布式锁详解

分布式锁是分布式系统中用于协调多个节点对共享资源访问的重要机制。以下是关于分布式锁的全面解析：

#### 为什么需要分布式锁？

在分布式系统中，当多个服务实例需要访问/修改共享资源时，需要一种跨JVM的互斥机制来保证：
- 数据一致性
- 避免重复处理
- 防止资源竞争

#### 分布式锁的核心特性

1. **互斥性**：同一时刻只有一个客户端能持有锁
2. **可重入性**：同一客户端可多次获取同一把锁
3. **锁超时**：防止死锁，自动释放机制
4. **高可用**：锁服务需要高可用
5. **非阻塞**：获取锁失败应快速返回而非阻塞
6. **公平性**（可选）：按申请顺序获取锁

#### 常见实现方案

##### 1. 基于数据库

**实现方式**：

- 唯一索引：利用唯一键冲突实现
- 乐观锁：版本号机制
- 悲观锁：`select for update`

**特点**：
- 实现简单
- 性能较差（高并发下数据库压力大）
- 无自动过期机制（需自行实现）

##### 2. 基于Redis

**常用命令**：
```bash
SET key value [EX seconds] [PX milliseconds] [NX|XX]
```

**Redlock算法**（Redis官方推荐的分布式锁算法）：
1. 获取当前时间
2. 依次尝试从N个Redis节点获取锁
3. 计算获取锁耗时，只有耗时小于锁超时时间且从多数节点获取成功才算获取成功
4. 锁的实际有效时间 = 初始有效时间 - 获取锁耗时
5. 如果获取失败，向所有节点发送释放锁请求

**特点**：
- 性能好
- 需要处理锁续期问题（看门狗机制）
- 需考虑Redis集群故障转移时的安全性

##### 3. 基于Zookeeper

**实现原理**：
- 创建临时顺序节点
- 判断自己是否是最小节点，是则获取锁
- 否则监听前一个节点的删除事件

**特点**：
- 可靠性高（CP系统）
- 性能比Redis略差
- 原生支持锁释放（临时节点特性）
- 天然支持公平锁

##### 4. 基于Etcd

**实现原理**：
- 利用租约（Lease）机制
- 事务比较并交换（CAS）操作
- 前缀查询和监听

**特点**：
- 强一致性
- 高可用
- 支持TTL自动过期

#### 方案对比

| 特性       | 数据库         | Redis      | Zookeeper    | Etcd       |
| ---------- | -------------- | ---------- | ------------ | ---------- |
| 性能       | 低             | 高         | 中           | 中高       |
| 实现复杂度 | 简单           | 中等       | 复杂         | 中等       |
| 可靠性     | 一般           | 依赖配置   | 高           | 高         |
| 自动过期   | 不支持         | 支持       | 支持         | 支持       |
| 公平锁     | 不支持         | 可实现     | 原生支持     | 可实现     |
| 适用场景   | 低并发简单场景 | 高并发场景 | 高可靠性场景 | 云原生环境 |

#### 最佳实践

1. **锁粒度**：尽量细化，避免大范围锁
2. **超时设置**：设置合理的锁超时时间
3. **幂等性**：即使锁失效，业务逻辑也应保持幂等
4. **锁续期**：对于长任务实现锁续期机制
5. **容错处理**：考虑锁服务不可用时的降级方案
6. **监控**：实现锁获取/释放的监控

#### Redis分布式锁示例（Redisson实现）

```java
// 获取锁
RLock lock = redisson.getLock("myLock");
try {
    // 尝试加锁，最多等待100秒，上锁后30秒自动解锁
    boolean res = lock.tryLock(100, 30, TimeUnit.SECONDS);
    if (res) {
        // 业务逻辑
    }
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
} finally {
    // 释放锁
    lock.unlock();
}
```

#### 常见问题解决方案

**问题1：锁提前过期**
- 解决方案：实现锁续期（看门狗机制）

**问题2：误释放其他客户端锁**
- 解决方案：锁值使用唯一客户端标识

**问题3：Redis主从切换导致锁失效**
- 解决方案：使用Redlock算法或集群模式

#### 面试回答技巧

1. **分层回答**：先讲为什么需要分布式锁，再讲实现方案
2. **对比分析**：比较不同实现方案的优缺点
3. **结合实际**：分享你在项目中如何使用分布式锁
4. **深入细节**：对熟悉的方案可以深入实现原理
5. **问题意识**：讨论分布式锁的常见问题及解决方案

分布式锁是分布式系统设计中的重要组件，理解其原理和实现方式对于构建可靠的分布式系统至关重要。

### Redis + RabbitMQ 高并发场景解决方案

#### Redis 核心问题

#### 缓存穿透
**问题**：大量请求查询不存在的数据，绕过缓存直接访问数据库

**解决方案**：
1. **缓存空对象**：对查询结果为null的数据也进行缓存，设置较短过期时间
   ```java
   // 伪代码示例
   Object value = redis.get(key);
   if (value == null) {
       value = db.get(key);
       if (value == null) {
           redis.setex(key, 300, "NULL"); // 缓存空值5分钟
       }
   }
   ```
2. **布隆过滤器**：在缓存前加布隆过滤器判断key是否存在
   ```java
   if (!bloomFilter.mightContain(key)) {
       return null; // 肯定不存在
   }
   ```

#### 缓存雪崩
**问题**：大量缓存同时失效，导致请求直接打到数据库

**解决方案**：
1. **差异化过期时间**：为缓存设置随机的过期时间
   ```java
   redis.setex(key, 3600 + Random.nextInt(600), value); // 基础1小时+随机10分钟
   ```
2. **多级缓存**：构建本地缓存+Redis缓存的层级结构
3. **热点数据永不过期**：配合定期异步更新策略

#### 缓存击穿
**问题**：热点key突然失效，大量请求直接访问数据库

**解决方案**：
1. **互斥锁**：使用Redis的SETNX实现分布式锁
   ```java
   if (redis.setnx("lock:"+key, 1, 30, TimeUnit.SECONDS)) {
       try {
           // 查询数据库并重建缓存
       } finally {
           redis.del("lock:"+key);
       }
   } else {
       Thread.sleep(50); // 稍后重试
   }
   ```
2. **逻辑过期**：缓存值中包含过期时间字段，异步刷新

#### 持久化
1. **RDB**：
   - 定时快照
   - 优点：恢复速度快，适合备份
   - 缺点：可能丢失最后一次快照后的数据

2. **AOF**：
   - 记录所有写操作命令
   - 优点：数据安全性高（可配置fsync策略）
   - 缺点：文件体积大，恢复速度慢

**生产建议**：
- 同时开启RDB和AOF
- AOF使用everysec配置
- 定期执行BGREWRITEAOF压缩AOF文件

#### 集群模式
1. **主从复制**：
   - 读写分离
   - 一主多从架构
   - 故障需手动切换

2. **哨兵模式**：
   - 监控主节点状态
   - 自动故障转移
   - 配置至少3个哨兵节点

3. **Cluster模式**：
   - 数据分片（16384个slot）
   - 每个节点保存部分数据
   - 节点间通过Gossip协议通信
   - 至少需要3主3从

**选择建议**：
- 小规模：哨兵模式
- 大规模：Cluster模式
- 超大规模：Cluster分片+代理层

### RabbitMQ 核心问题

#### 保证消息不丢失

**完整方案**：
1. **生产者确认**：
   ```java
   // 开启确认模式
   channel.confirmSelect();
   // 异步确认回调
   channel.addConfirmListener((sequenceNumber, multiple) -> {
       // 处理成功确认
   }, (sequenceNumber, multiple) -> {
       // 处理失败确认
   });
   ```

2. **消息持久化**：
   ```java
   // 声明持久化队列
   channel.queueDeclare(QUEUE_NAME, true, false, false, null);
   // 发送持久化消息
   channel.basicPublish("", QUEUE_NAME, 
       MessageProperties.PERSISTENT_TEXT_PLAIN,
       message.getBytes());
   ```

3. **消费者手动ACK**：
   ```java
   // 关闭自动ACK
   channel.basicConsume(QUEUE_NAME, false, consumer);
   // 处理完成后手动ACK
   channel.basicAck(deliveryTag, false);
   ```

4. **镜像队列**（高可用）：
   ```bash
   # 设置镜像队列策略
   rabbitmqctl set_policy ha-all "^ha." '{"ha-mode":"all"}'
   ```

#### 延迟队列实现

**方案1：TTL+死信队列**
1. 创建普通队列A并设置死信交换器
2. 发送消息到A并设置TTL
3. 消息过期后转入死信队列B
4. 消费者监听B队列

**方案2：rabbitmq-delayed-message-exchange插件**
1. 安装插件
   ```bash
   rabbitmq-plugins enable rabbitmq_delayed_message_exchange
   ```
2. 声明延迟交换器
   ```java
   Map<String, Object> args = new HashMap<>();
   args.put("x-delayed-type", "direct");
   channel.exchangeDeclare("delayed.exchange", "x-delayed-message", true, false, args);
   ```
3. 发送延迟消息
   ```java
   AMQP.BasicProperties.Builder props = new AMQP.BasicProperties.Builder();
   props.headers(new HashMap<>()).headers().put("x-delay", 5000);
   channel.basicPublish("delayed.exchange", "", props.build(), message.getBytes());
   ```

#### 集群搭建

**普通集群模式**：
1. 同步.erlang.cookie文件到所有节点
2. 节点加入集群
   ```bash
   # 在slave节点执行
   rabbitmqctl stop_app
   rabbitmqctl join_cluster rabbit@master
   rabbitmqctl start_app
   ```
3. 查看集群状态
   ```bash
   rabbitmqctl cluster_status
   ```

**镜像队列集群**：
1. 先搭建普通集群
2. 设置镜像策略
   ```bash
   rabbitmqctl set_policy ha-all "^" '{"ha-mode":"all"}'
   ```

**集群节点类型**：
- **磁盘节点**：保存元数据到磁盘（至少需要一个）
- **内存节点**：只保存元数据到内存（性能更好）

**最佳实践**：
- 3-5个节点组成集群
- 混合部署磁盘节点和内存节点
- 使用HAProxy/Nginx做负载均衡

#### Redis + RabbitMQ 组合实践

**典型高并发场景架构**：
```
客户端 → Nginx → 应用集群 → Redis缓存 → RabbitMQ → 数据库
```

**流量削峰示例**：
1. 请求先查Redis缓存
2. 缓存未命中则发送MQ消息
   ```java
   // 生成唯一消息ID
   String msgId = UUID.randomUUID().toString();
   // 存储到Redis做状态跟踪
   redis.setex("msg:"+msgId, 3600, "processing");
   // 发送消息
   channel.basicPublish("", "order_queue", 
       new AMQP.BasicProperties.Builder()
           .messageId(msgId)
           .build(),
       message.getBytes());
   ```
3. 异步消费者处理并更新结果
   ```java
   // 处理成功
   redis.set("msg:"+msgId, "completed");
   redis.set("data:"+orderId, result);
   // 处理失败
   redis.set("msg:"+msgId, "failed");
   ```

### Spring Security 6 过滤器链面试题解析

在 Spring Security 6 面试中，关于过滤器链的问题是高频考点。以下是针对这个主题的全面解析：

#### 核心概念：SecurityFilterChain

Spring Security 6 的核心安全功能是通过一系列过滤器实现的，这些过滤器组成了 **过滤器链（Filter Chain）**。在 Spring Security 6 中，`SecurityFilterChain` 是配置安全规则的主要方式。

#### 常见面试问题形式

1. **基础概念类**：
   - "请解释 Spring Security 的过滤器链工作原理"
   - "Spring Security 如何处理 HTTP 请求？"
   
2. **具体实现类**：
   - "Spring Security 6 中默认包含哪些重要过滤器？"
   - "如何自定义一个 Security 过滤器？"
   
3. **配置相关类**：
   - "在 Spring Security 6 中如何配置多个 SecurityFilterChain？"
   - "WebSecurity 和 HttpSecurity 有什么区别？"

4. **深度原理类**：
   - "过滤器链中的过滤器顺序为什么很重要？"
   - "Spring Security 6 相比之前版本在过滤器方面有什么变化？"

#### 关键过滤器及其顺序

Spring Security 6 的默认过滤器链包含以下重要过滤器（按执行顺序）：

1. **ForceEagerSessionCreationFilter** (新增 in 6.x)
   - 强制提前创建会话

2. **WebAsyncManagerIntegrationFilter**
   - 集成 WebAsyncManager 与 SecurityContext

3. **SecurityContextPersistenceFilter**
   - 在请求间持久化 SecurityContext

4. **HeaderWriterFilter**
   - 添加安全相关的头部信息

5. **CorsFilter**
   - 处理 CORS 跨域请求

6. **CsrfFilter**
   - CSRF 防护

7. **LogoutFilter**
   - 处理注销请求

8. **OAuth2AuthorizationRequestRedirectFilter** (OAuth2 相关)
   - OAuth2 授权请求重定向

9. **Saml2WebSsoAuthenticationRequestFilter** (SAML 相关)
   - SAML 认证请求处理

10. **X509AuthenticationFilter**
    - X509 证书认证

11. **AbstractPreAuthenticatedProcessingFilter**
    - 预认证处理

12. **CasAuthenticationFilter** (CAS 相关)
    - CAS 认证处理

13. **UsernamePasswordAuthenticationFilter**
    - 表单登录认证处理

14. **OpenIDAuthenticationFilter** (OpenID 相关)
    - OpenID 认证处理

15. **DefaultLoginPageGeneratingFilter**
    - 默认登录页生成

16. **DefaultLogoutPageGeneratingFilter**
    - 默认注销页生成

17. **BasicAuthenticationFilter**
    - HTTP 基本认证处理

18. **RequestCacheAwareFilter**
    - 请求缓存处理

19. **SecurityContextHolderAwareRequestFilter**
    - 使 HttpServletRequest 感知 SecurityContext

20. **JaasApiIntegrationFilter** (JAAS 相关)
    - JAAS 集成

21. **RememberMeAuthenticationFilter**
    - 记住我功能处理

22. **AnonymousAuthenticationFilter**
    - 匿名用户认证处理

23. **OAuth2LoginAuthenticationFilter** (OAuth2 相关)
    - OAuth2 登录认证

24. **Saml2WebSsoAuthenticationFilter** (SAML 相关)
    - SAML Web SSO 认证

25. **ExceptionTranslationFilter**
    - 安全异常转换

26. **FilterSecurityInterceptor**
    - 最终访问决策

#### 常见面试问题及回答示例

#### Q1: 请解释 Spring Security 的过滤器链工作原理

**回答要点**：
1. Spring Security 基于 Servlet 过滤器链实现
2. 每个过滤器负责特定安全功能
3. 请求按顺序通过所有过滤器
4. 过滤器可以决定是否中断链式调用
5. 最后一个过滤器 (`FilterSecurityInterceptor`) 做最终访问控制决策

**示例回答**：
"Spring Security 的安全功能是通过一系列过滤器组成的链条实现的。当 HTTP 请求到达时，它会依次通过这些过滤器，每个过滤器处理特定的安全关注点。比如早期的过滤器可能处理 CSRF 防护，后续的过滤器处理认证逻辑，最后的过滤器做授权决策。如果任何过滤器判定请求不安全，它可以中断处理流程并返回错误响应。"

#### Q2: 如何在 Spring Security 6 中自定义过滤器？

**回答要点**：
1. 实现 `Filter` 接口或继承 `GenericFilterBean`
2. 通过 `HttpSecurity` 配置添加
3. 可以指定过滤器的位置
4. 示例代码：

```java
@Bean
SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .addFilterBefore(new CustomFilter(), UsernamePasswordAuthenticationFilter.class)
        .addFilterAfter(new AnotherFilter(), BasicAuthenticationFilter.class);
    return http.build();
}
```

#### Q3: Spring Security 6 在过滤器方面有哪些重要变化？

**回答要点**：
1. 移除了 `WebSecurityConfigurerAdapter`，改用 `SecurityFilterChain` bean
2. 默认情况下不再自动创建会话
3. 新增 `ForceEagerSessionCreationFilter`
4. 更清晰的过滤器配置 API
5. 更强调函数式配置风格

#### Q4: 为什么过滤器顺序很重要？

**回答要点**：
1. 某些过滤器依赖前置过滤器的处理结果
2. 例如认证过滤器需要在授权过滤器之前
3. 错误的顺序可能导致安全漏洞
4. CSRF 防护通常需要在早期执行
5. 异常处理过滤器需要在可能抛出异常的过滤器之后

#### 配置多个 SecurityFilterChain

Spring Security 6 支持基于请求路径配置多个过滤器链：

```java
@Bean
@Order(1)
public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
    http
        .securityMatcher("/api/**")
        .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
        .httpBasic(Customizer.withDefaults());
    return http.build();
}

@Bean
@Order(2)
public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
    return http.build();
}
```

#### 面试技巧

1. **结合实践**：分享你在项目中如何定制过滤器链
2. **对比版本**：说明 Spring Security 5 和 6 在过滤器配置上的区别
3. **强调安全**：解释为什么默认过滤器链的顺序是安全的
4. **准备示例**：准备一个自定义过滤器的代码示例
5. **理解原理**：能解释过滤器链与 Servlet 容器的关系

理解 Spring Security 的过滤器链机制是掌握其工作原理的关键，也是面试中的高频考察点。

### MySQL索引优化

#### 1. 索引基础概念

索引是帮助MySQL高效获取数据的数据结构，类似于书籍的目录，可以加快查询速度。

#### 2. 索引类型

##### (1) 按数据结构分类：
- **B+Tree索引**：最常用，适合范围查询
- **Hash索引**：适合等值查询，不支持范围查询
- **Full-text索引**：全文检索
- **R-Tree索引**：空间数据索引

##### (2) 按逻辑分类：
- **普通索引**：最基本的索引，无限制
- **唯一索引**：列值必须唯一，允许NULL
- **主键索引**：特殊的唯一索引，不允许NULL
- **复合索引**：多列组合的索引
- **覆盖索引**：查询的列都在索引中

#### 3. 索引优化原则

1. **最左前缀原则**：
   - 复合索引(a,b,c)，有效查询条件：
     - a
     - a,b
     - a,b,c
   - 无效查询条件：
     - b
     - b,c
     - c

2. **避免索引失效场景**：
   - 使用`!=`或`<>`操作符
   - 使用`OR`连接条件（除非所有OR条件都有索引）
   - 对索引列进行运算或函数操作
   - 使用`LIKE`以通配符开头('%abc')
   - 类型转换（如字符串列用数字查询）

3. **选择合适的索引列**：
   - 高选择性的列（区分度高）
   - 常用于WHERE、ORDER BY、GROUP BY、JOIN的列
   - 避免对频繁更新的列建过多索引

4. **控制索引数量**：
   - 不是越多越好，每个索引占用存储空间
   - 增删改操作需要维护索引，影响性能

#### 4. 索引优化实战技巧

1. **使用覆盖索引**：
   ```sql
   -- 假设有索引(username, age)
   SELECT username, age FROM users WHERE username = 'john';
   ```

2. **索引列顺序优化**：
   - 将选择性高的列放在前面
   - 考虑查询频率和排序需求

3. **使用索引提示**：
   ```sql
   SELECT * FROM users USE INDEX(index_name) WHERE ...
   SELECT * FROM users FORCE INDEX(index_name) WHERE ...
   ```

4. **前缀索引**：
   ```sql
   ALTER TABLE users ADD INDEX idx_name(name(10)); -- 只索引前10个字符
   ```

### Explain执行计划详解

#### 1. Explain基本用法

```sql
EXPLAIN SELECT * FROM users WHERE id = 1;
```

#### 2. 关键字段解析

| 字段          | 说明                                                         |
| ------------- | ------------------------------------------------------------ |
| id            | 查询标识符，相同id表示同一查询块                             |
| select_type   | 查询类型(SIMPLE, PRIMARY, SUBQUERY, DERIVED等)               |
| table         | 访问的表名                                                   |
| partitions    | 匹配的分区                                                   |
| type          | 访问类型(从好到差: system > const > eq_ref > ref > range > index > ALL) |
| possible_keys | 可能使用的索引                                               |
| key           | 实际使用的索引                                               |
| key_len       | 使用的索引长度                                               |
| ref           | 与索引比较的列或常量                                         |
| rows          | 预估需要读取的行数                                           |
| filtered      | 返回结果的行数占读取行数的百分比                             |
| Extra         | 额外信息(Using index, Using where, Using temporary, Using filesort等) |

#### 3. type字段详解

1. **system**：表只有一行记录
2. **const**：通过主键或唯一索引一次就找到
   ```sql
   EXPLAIN SELECT * FROM users WHERE id = 1;
   ```
3. **eq_ref**：联表查询时使用主键或唯一索引
   ```sql
   EXPLAIN SELECT * FROM users u JOIN orders o ON u.id = o.user_id;
   ```
4. **ref**：使用非唯一索引查找
   ```sql
   EXPLAIN SELECT * FROM users WHERE username = 'john';
   ```
5. **range**：索引范围扫描
   ```sql
   EXPLAIN SELECT * FROM users WHERE id > 10 AND id < 100;
   ```
6. **index**：全索引扫描
7. **ALL**：全表扫描

#### 4. Extra字段常见值

1. **Using index**：使用覆盖索引
2. **Using where**：服务器在存储引擎检索行后再过滤
3. **Using temporary**：使用临时表
4. **Using filesort**：需要额外排序
5. **Using join buffer**：使用连接缓存
6. **Impossible WHERE**：WHERE条件永远为false

#### 5. 执行计划优化案例

**案例1：索引失效**
```sql
EXPLAIN SELECT * FROM users WHERE DATE(create_time) = '2023-01-01';
-- 优化后
EXPLAIN SELECT * FROM users WHERE create_time BETWEEN '2023-01-01 00:00:00' AND '2023-01-01 23:59:59';
```

**案例2：避免filesort**
```sql
-- 有索引(a,b)
EXPLAIN SELECT * FROM table ORDER BY a, b;  -- 不会filesort
EXPLAIN SELECT * FROM table ORDER BY b, a;  -- 会filesort
```

**案例3：联表优化**
```sql
-- 不好的写法
EXPLAIN SELECT * FROM a, b WHERE a.id = b.a_id;
-- 优化写法
EXPLAIN SELECT * FROM a JOIN b ON a.id = b.a_id;
```

#### 三、综合优化建议

1. **定期分析慢查询**：
   ```sql
   -- 开启慢查询日志
   SET GLOBAL slow_query_log = 'ON';
   SET GLOBAL long_query_time = 1;
   ```

2. **使用PROFILE分析**：
   ```sql
   SET profiling = 1;
   SELECT * FROM users WHERE ...;
   SHOW PROFILE FOR QUERY 1;
   ```

3. **监控索引使用情况**：
   ```sql
   SELECT * FROM sys.schema_unused_indexes;
   ```

4. **定期优化表**：
   ```sql
   ANALYZE TABLE users;
   OPTIMIZE TABLE users;
   ```

5. **合理使用索引合并**：
   - 当多个单列索引条件用AND连接时，MySQL可能使用Index Merge优化
