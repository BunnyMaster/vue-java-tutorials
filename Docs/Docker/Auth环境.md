## 文件方式安装环境
使用docker-compose有的时候会因为版本不同，但是配置文件主要内容就是这些。需要注意版本问题

**mysql配置问题**

| **特性**                                               | `**my.cnf**`                                                                                                                                                                                                                                         | `**conf.d**`<br/>** ****目录**                                                                                                                                                                                                                       |
| ---------------------------------------------------- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **<font style="color:rgb(64, 64, 64);">文件类型</font>** | <font style="color:rgb(64, 64, 64);">单个文件</font>                                                                                                                                                                                                     | <font style="color:rgb(64, 64, 64);">目录，包含多个</font><font style="color:rgb(64, 64, 64);"> </font>`<font style="color:rgb(64, 64, 64);">.cnf</font>`<br/><font style="color:rgb(64, 64, 64);"> </font><font style="color:rgb(64, 64, 64);">文件</font> |
| **<font style="color:rgb(64, 64, 64);">配置方式</font>** | <font style="color:rgb(64, 64, 64);">集中式配置</font>                                                                                                                                                                                                    | <font style="color:rgb(64, 64, 64);">分布式配置</font>                                                                                                                                                                                                  |
| **<font style="color:rgb(64, 64, 64);">优先级</font>**  | <font style="color:rgb(64, 64, 64);">高（覆盖</font><font style="color:rgb(64, 64, 64);"> </font>`<font style="color:rgb(64, 64, 64);">conf.d</font>`<br/><font style="color:rgb(64, 64, 64);"> </font><font style="color:rgb(64, 64, 64);">中的配置）</font> | <font style="color:rgb(64, 64, 64);">低（被</font><font style="color:rgb(64, 64, 64);"> </font>`<font style="color:rgb(64, 64, 64);">my.cnf</font>`<br/><font style="color:rgb(64, 64, 64);"> </font><font style="color:rgb(64, 64, 64);">覆盖）</font>  |
| **<font style="color:rgb(64, 64, 64);">适用场景</font>** | <font style="color:rgb(64, 64, 64);">全局配置，核心配置</font>                                                                                                                                                                                                | <font style="color:rgb(64, 64, 64);">模块化配置，便于扩展和维护</font>                                                                                                                                                                                          |


---

```yaml
name: auth-dependence  # 定义该配置的名称为 auth-dependence
services: # 定义服务列表

  # 安装MySQL
  mysql: # 定义 MySQL 服务
    container_name: mysql_master  # 容器名称为 mysql_master
    image: mysql:8.0.33  # 使用 MySQL 8.0.33 版本的镜像
    ports:
      - "3306:3306"  # 将宿主机的 3306 端口映射到容器的 3306 端口
    environment:
      - MYSQL_ROOT_PASSWORD=123456  # 设置 MySQL 的 root 用户密码为 123456
      - TZ=Asia/Shanghai  # 设置时区为亚洲/上海
    volumes:
      # - ~/docker/docker_data/mysql/mysql_master/etc/my.cnf:/etc/my.cnf # 如果需要创建配置文件
      - ~/docker/docker_data/mysql/mysql_master/etc/mysql:/etc/mysql/conf.d  # 挂载 MySQL 配置文件目录
      - ~/docker/docker_data/mysql/mysql_master/data:/var/lib/mysql  # 挂载 MySQL 数据目录
      - ~/docker/docker_data/mysql/mysql_master/backup:/backup  # 挂载备份目录
    command:
      - "--log-bin=mysql-bin"  # 启用二进制日志
      - "--server-id=1"  # 设置服务器 ID 为 1
      - "--collation-server=utf8mb4_unicode_ci"  # 设置默认的排序规则为 utf8mb4_unicode_ci
      - "--character-set-server=utf8mb4"  # 设置默认的字符集为 utf8mb4
      - "--lower-case-table-names=1"  # 设置表名存储为小写
    restart: always  # 设置容器总是自动重启
    privileged: true  # 赋予容器特权模式
    networks:
      - auth  # 将 MySQL 服务加入到 auth 网络

  # 安装Redis
  redis: # 定义 Redis 服务
    container_name: redis_master  # 容器名称为 redis_master
    image: redis:7.0.10  # 使用 Redis 7.0.10 版本的镜像
    ports:
      - "6379:6379"  # 将宿主机的 6379 端口映射到容器的 6379 端口
    volumes:
      # - ~/docker/docker_data/redis_master/redis.conf:/etc/redis/redis.conf # 需要创建配置文件
      - ~/docker/docker_data/redis_master:/etc/redis  # 挂载 Redis 配置文件目录
      - ~/docker/docker_data/redis_master/data:/data  # 挂载 Redis 数据目录
    command:
      - "--appendonly yes"  # 启用 AOF 持久化
      - "--daemonize no"  # 不以守护进程方式运行
      - "--requirepass 123456"  # 设置 Redis 访问密码为 123456
      - "--tcp-keepalive 300"  # 设置 TCP keepalive 时间为 300 秒
    restart: always  # 设置容器总是自动重启
    networks:
      - auth  # 将 MySQL 服务加入到 auth 网络

  # 安装 Minio
  minio: # 定义 Minio 服务
    image: minio/minio  # 使用 Minio 官方镜像
    container_name: minio_master  # 容器名称为 minio_master
    ports:
      - "9000:9000"  # 将宿主机的 9000 端口映射到容器的 9000 端口
      - "9090:9090"  # 将宿主机的 9090 端口映射到容器的 9090 端口
    volumes:
      - ~/docker/docker_data/minio/data:/data  # 挂载 Minio 数据目录
    environment:
      - MINIO_ROOT_USER=bunny  # 设置 Minio 的 root 用户名为 bunny
      - MINIO_ROOT_PASSWORD=02120212  # 设置 Minio 的 root 用户密码为 02120212
    command: "server /data --console-address :9090"  # 启动 Minio 服务并指定控制台地址
    restart: always  # 设置容器总是自动重启
    networks:
      - auth  # 将 MySQL 服务加入到 auth 网络

networks: # 定义网络
  auth: # 定义名为 auth 的网络
    name: auth  # 网络名称为 auth
    driver: bridge  # 使用 bridge 驱动（默认）
```

