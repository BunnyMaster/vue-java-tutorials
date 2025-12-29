## Factory method 'redisEmbeddingStore' threw exception with message: Invalid rule type: JSON

一般是没有加载JSON模块导致的，如果是普通的docker安装一般没有这个问题，如是使用docker-compose会有。

如果使用正常Docker进行搭建参考这个：

```
docker run -d \
  --name redis-stack \
  -p 16379:6379 \
  redislabs/redisearch:2.8.8 \
  redis-server \
    --loadmodule /usr/lib/redis/modules/redisearch.so \
    --loadmodule /usr/lib/redis/modules/rejson.so \
    --requirepass Dev1234!
```

在`docker-compose.yaml`修改如下，之后就可以了。

需要为其添加`--loadmodule /usr/lib/redis/modules/rejson.so `

```yaml
# ==================== Redis RedisSearch ====================
redis-stack:
  container_name: redis-stack
  image: redislabs/redisearch:2.8.8
  ports:
    - "16379:6379"
  command: >
    redis-server
    --loadmodule /usr/lib/redis/modules/redisearch.so
    --loadmodule /usr/lib/redis/modules/rejson.so 
    --requirepass Dev1234!
  volumes:
    - ~/docker-compose/develop/redis-stack/data:/data
  networks:
    - microservices-net
  restart: unless-stopped
```

完整配置示例

```yaml
# 定义整个开发栈的名称
name: microservices-dev-stack

services:
  # ==================== Redis 哨兵模式集群 ====================
  redis-master:
    container_name: redis-master
    image: redis:7.0-alpine
    ports:
      - "6379:6379"
    command: |
      redis-server
      --requirepass Dev1234!
      --masterauth Dev1234!
      --appendonly yes
    volumes:
      - ~/docker-compose/develop/redis/master/data:/data
    networks:
      - microservices-net
    restart: unless-stopped

  redis-slave1:
    container_name: redis-slave1
    image: redis:7.0-alpine
    ports:
      - "6380:6379"
    command: |
      redis-server
      --slaveof redis-master 6379
      --masterauth Dev1234!
      --requirepass Dev1234!
      --appendonly yes
    depends_on:
      - redis-master
    networks:
      - microservices-net
    restart: unless-stopped

  redis-slave2:
    container_name: redis-slave2
    image: redis:7.0-alpine
    ports:
      - "6381:6379"
    command: |
      redis-server
      --slaveof redis-master 6379
      --masterauth Dev1234!
      --requirepass Dev1234!
      --appendonly yes
    depends_on:
      - redis-master
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Sentinel 实例 ====================
  redis-sentinel1:
    container_name: redis-sentinel1
    image: redis:7.0-alpine
    ports:
      - "26379:26379"
    command: |
      sh -c "
      mkdir -p /etc/redis &&
      cat > /etc/redis/sentinel.conf << 'EOF'
      port 26379
      dir /tmp
      sentinel monitor mymaster redis-master 6379 2
      sentinel auth-pass mymaster Dev1234!
      sentinel down-after-milliseconds mymaster 5000
      sentinel parallel-syncs mymaster 1
      sentinel failover-timeout mymaster 10000
      sentinel resolve-hostnames yes
      sentinel announce-hostnames yes
      EOF
      redis-sentinel /etc/redis/sentinel.conf
      "
    depends_on:
      - redis-master
      - redis-slave1
      - redis-slave2
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Redis RedisSearch ====================
  redis-stack:
    container_name: redis-stack
    image: redislabs/redisearch:2.8.8
    ports:
      - "16379:6379"
    command: >
      redis-server
      --loadmodule /usr/lib/redis/modules/redisearch.so
      --loadmodule /usr/lib/redis/modules/rejson.so 
      --requirepass Dev1234!
    volumes:
      - ~/docker-compose/develop/redis-stack/data:/data
    networks:
      - microservices-net
    restart: unless-stopped
```