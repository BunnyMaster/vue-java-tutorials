# 环境搭建
## Ubuntu基础环境
### 升级和安装docker
```bash
sudo apt-get remove docker docker-engine docker.io containerd runc
sudo apt update
sudo apt upgrade
sudo apt-get install ca-certificates curl gnupg lsb-release
# 添加Docker官方GPG密钥
sudo curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
# 安装 xdg-open
sudo apt install xdg-utils -y  
# 安装docker
sudo apt-get install docker-ce docker-ce-cli containerd.io
# 默认情况下，只有root用户和docker组的用户才能运行Docker命令。我们可以将当前用户添加到docker组，以避免每次使用Docker时都需要使用sudo，设置完成后退出当前用户之后再进入既可
sudo usermod -aG docker $USER
# 运行docker
sudo systemctl start docker
# 安装工具
sudo apt-get -y install apt-transport-https ca-certificates curl software-properties-common
# 重启docker
sudo service docker restart
```

### 配置docker
```bash
# 启动所有容器
docker start $(docker ps -aq)

# 停止所有容器
docker stop $(docker ps -aq)

# 移除所有容器
docker rm $(docker ps -aq)
```

#### 设置免密码
```bash
# 创建分组一般
sudo groupadd docker
# 将当前用户添加到分组
sudo usermod -aG docker $USER
# 重启终端生效
exit
```

#### 设置镜像加速
```bash
# 创建目录
sudo mkdir -p /etc/docker
# 写入配置文件
sudo tee /etc/docker/daemon.json <<-'EOF'
{
    "registry-mirrors": [
        "https://docker-0.unsee.tech",
        "https://docker-cf.registry.cyou",
        "https://docker.1panel.live"
    ]
}
EOF
# 重启docker服务
sudo systemctl daemon-reload && sudo systemctl restart docker
```

### 配置docker-compose
> [!WARNING]
>
> 如果安装最新docker是存在`docker compose`的。
>
> 如果没有这个可以尝试下面的安装。
>

#### 安装方式
**Centos和Rocky**

```bash
sudo yum install docker-ce docker-ce-cli containerd.io

sudo yum install curl
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.4/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

sudo docker-compose --version
```

**Ubuntu安装**

```bash
sudo apt install docker-compose
```

#### 常用命令
**Docker Compose 命令速查表**

| 命令 | 作用描述 | 常用参数及示例 |
| :--- | :--- | :--- |
| `docker-compose up` | **核心命令**：构建镜像、（重新）创建、启动所有服务。 | `-d`：后台运行。   `--build`：启动前重新构建镜像。   `--force-recreate`：强制重建容器。   **示例**：`docker-compose up -d --build` |
| `docker-compose down` | **停止并清理**：停止容器并移除容器、网络。 | `-v`：同时删除**命名数据卷**（数据会丢失！）。   `--rmi all`：删除所有相关镜像。   **示例**：`docker-compose down -v` |
| `docker-compose start` | **启动**：启动已存在但处于停止状态（stopped）的容器。 | **示例**：`docker-compose start` |
| `docker-compose stop` | **停止**：停止正在运行的容器，但不删除它们。 | **示例**：`docker-compose stop` |
| `docker-compose restart` | **重启**：重启服务容器。 | **示例**：`docker-compose restart web` |
| `docker-compose pause` | **暂停**：暂停服务容器的所有进程。 | **示例**：`docker-compose pause web` |
| `docker-compose unpause` | **恢复**：恢复被暂停的服务容器。 | **示例**：`docker-compose unpause web` |
| `docker-compose run` | **一次性运行**：在新的容器中执行一次性命令。 | `--rm`：运行后自动删除容器。   `-e`：设置环境变量。   **示例**：`docker-compose run --rm web bash` |
| `docker-compose exec` | **执行命令**：在**正在运行**的容器中执行命令。 | `-d`：后台执行命令。   `-u`：指定用户。   **示例**：`docker-compose exec web bash` |
| `docker-compose logs` | **查看日志**：查看服务的日志输出。 | `-f`：实时跟踪日志。   `--tail=N`：仅显示最后N行。   **示例**：`docker-compose logs -f web` |
| `docker-compose ps` | **查看状态**：列出项目中的所有容器及其状态。 | **示例**：`docker-compose ps` |
| `docker-compose build` | **构建镜像**：构建或重新构建服务的镜像。 | `--no-cache`：构建时不使用缓存。   **示例**：`docker-compose build --no-cache web` |
| `docker-compose pull` | **拉取镜像**：拉取服务所需的镜像，但不启动容器。 | **示例**：`docker-compose pull` |
| `docker-compose config` | **检查配置**：验证和查看已解析的 Compose 文件配置。 | `-q`：只验证，不输出（静默模式）。   **示例**：`docker-compose config` |
| `docker-compose images` | **列出镜像**：列出项目中所有容器使用的镜像。 | **示例**：`docker-compose images` |
| `docker-compose top` | **查看进程**：显示每个容器内运行的进程。 | **示例**：`docker-compose top` |
| `docker-compose port` | **查看端口**：显示某个端口映射到的公共端口。 | **示例**：`docker-compose port web 80` |
| `docker-compose kill` | **强制停止**：通过发送 SIGKILL 信号来强制停止容器。 | `-s SIGINT`：指定其他信号。   **示例**：`docker-compose kill -s SIGINT` |


**常用操作流程示例**

| 场景 | 命令组合 |
| :--- | :--- |
| **首次启动/重新部署** | `docker-compose up -d --build` |
| **查看实时日志** | `docker-compose logs -f` |
| **进入容器调试** | `docker-compose exec <service_name> sh` (或 `bash`) |
| **执行数据库迁移** | `docker-compose run --rm web python manage.py migrate` |
| **优雅停止并保留数据** | `docker-compose down` |
| **彻底清理（含数据卷）** | `docker-compose down -v` |
| **仅重启某个服务** | `docker-compose restart <service_name>` |
| **扩容服务实例** | `docker-compose up -d --scale <service_name>=3` |


### 设置静态IP地址
#### 打开配置文件
```bash
# 文件路径
sudo vim /etc/netplan/你的文件.yaml

# 示例
sudo vim /etc/netplan/00-installer-config.yaml
```

#### 编辑配置文件
**源文件示例**

```yaml
# This file is generated from information provided by the datasource.  Changes
# to it will not persist across an instance reboot.  To disable cloud-init's
# network configuration capabilities, write a file
# /etc/cloud/cloud.cfg.d/99-disable-network-config.cfg with the following:
# network: {config: disabled}
network:
    ethernets:
        eth0:
            dhcp4: true
    version: 2
```

**示例文件**

```yaml
network:
  ethernets:
    eth0:
      dhcp4: false
      addresses: [172.20.116.203/20]
      optional: true
      routes:
        - to: default
          via: 172.20.112.1
      nameservers:
        addresses: [8.8.8.8]
  version: 2
```

#### 应用配置文件
```bash
sudo netplan apply
sudo reboot
```

### 校准时间
#### 安装`tzdata`
```bash
sudo apt install tzdata
```

#### 设置时区
```bash
sudo timedatectl set-timezone Asia/Shanghai
```

#### 验证时间
```bash
timedatectl
```

### 设置防火墙
```bash
# 查看详细规则
sudo ufw show added

# 查看防火墙日志
sudo tail -f /var/log/ufw.log

# 暂时禁用防火墙（调试用）
sudo systemctl stop ufw
sudo systemctl start ufw

# 完全重置防火墙
sudo ufw reset
```

#### 设置打开防火墙
```bash
# 启用防火墙
sudo ufw enable

# 查看防火墙状态
sudo ufw status verbose
```

#### 设置关闭防火墙
```bash
# 禁用防火墙
sudo ufw disable

# 查看状态确认已关闭
sudo ufw status
```

#### 只开放部分端口
```bash
# 开放 SSH 端口 (22)
sudo ufw allow 22

# 开放 HTTP 端口 (80)
sudo ufw allow 80

# 开放 HTTPS 端口 (443)
sudo ufw allow 443

# 开放特定端口给特定协议
sudo ufw allow 53/udp  # DNS
sudo ufw allow 53/tcp  # DNS

# 开放端口范围
sudo ufw allow 8000:9000/tcp  # 开放8000-9000的TCP端口
```

### 设置镜像源
#### 备份镜像源
```bash
sudo cp /etc/apt/sources.list /etc/apt/sources.list.back
```

#### 编辑镜像源
```bash
sudo vim /etc/apt/sources.list
```

**镜像源配置**

```shell
deb https://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ focal main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ focal-security main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ focal-updates main restricted universe multiverse

# deb https://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse
# deb-src https://mirrors.aliyun.com/ubuntu/ focal-proposed main restricted universe multiverse

deb https://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
deb-src https://mirrors.aliyun.com/ubuntu/ focal-backports main restricted universe multiverse
```

#### 更新软件包列表
```bash
# 首先更新软件包列表
sudo apt update

# 升级已安装的软件包
sudo apt upgrade

# 如果需要完整升级
sudo apt full-upgrade
```

## 服务环境搭建
### 使用说明
1. **创建目录和配置文件**：

```bash
mkdir -p ~/docker-compose/dev/{data,config}
cd ~/docker-compose/dev
```

2. **设置es权限**如果es无法启动设置下目录权限

```bash
# 开发可以这样设置
sudo chown -R $USER:$USER docker-compose/develop/elasticsearch/
chmod -R 755 docker-compose/develop/elasticsearch/

# 更安全的设置
chown -R 1000:1000 docker-compose/develop/elasticsearch/
```

3. **启动所有服务**：

```bash
docker compose up -d
```

4. **初始化MongoDB副本集**：等待`mongodb`全部启动完成之后，执行下面命令：

```bash
docker exec -it mongodb-node1 mongosh --eval "rs.initiate({
  _id: 'rs0',
  members: [
    {_id: 0, host: 'mongodb-node1:27017'},
    {_id: 1, host: 'mongodb-node2:27017'},
    {_id: 2, host: 'mongodb-node3:27017'}
  ]
})"
```

需要进入主节点`mongodb`的容器中

```bash
#进入容器
docker exec -it mongodb-node1 bash

#进入mongodb shell
mongosh --port 27017

#切换到admin库
use admin

#创建账号/密码
db.createUser({ user: 'admin', pwd: 'Dev1234!', roles: [ { role: "root", db: "admin" } ] });
```

### 服务访问信息
> [!TIP]
>
> 所有密码都是: Dev1234!
>

| 服务 | 访问地址 | 用户名 |
| --- | --- | --- |
| MySQL Master | `172.20.119.27:3306` | `root` |
| PostgreSQL | `172.20.119.27:5432` | `postgres` |
| MongoDB | `172.20.119.27:27017` | `admin` |
| Redis | `172.20.119.27:6379` | - |
| RabbitMQ管理台 | `http://172.20.119.27:15672` | `admin` |
| MinIO控制台 | `http://172.20.119.27:9090` | `admin` |
| Nacos | `http://172.20.119.27:8848/nacos` | `admin` |
| Elasticsearch | `http://172.20.119.27:9200` | - |
| Kibana | `http://172.20.119.27:5601` | - |
| Sentinel | `http://172.20.119.27:8858` | `sentinel` |
| Seata | `172.20.119.27:8091` | - |
| Grafana | `http://172.20.119.27:9700` | `admin`，`admin` |


这样所有中间件都使用统一的密码 `Dev1234!`，方便记忆和管理。

### Docker Compose配置
根据你的环境可以修改下面的名称：`name: microservices-dev-stack  `

#### 开发环境develop
```yaml
# 定义整个开发栈的名称
name: microservices-dev-stack  

services:
  # ==================== MySQL 主从复制集群 ====================
  mysql-master:
    container_name: mysql-master
    image: mysql:8.0.33
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=Dev1234!
      - MYSQL_DATABASE=auth_db
      - MYSQL_USER=dev_user
      - MYSQL_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
      - MYSQL_ROOT_HOST=%
    volumes:
      - ./docker-compose/develop/mysql/master/conf:/etc/mysql/conf.d
      - ./docker-compose/develop/mysql/master/data:/var/lib/mysql
      - ./docker-compose/develop/mysql/master/backup:/backup
    command:
      - "--server-id=1"
      - "--log-bin=mysql-bin"
      - "--binlog-format=ROW"
      - "--gtid-mode=ON"
      - "--enforce-gtid-consistency=ON"
    networks:
      - microservices-net
    restart: unless-stopped

  mysql-slave:
    container_name: mysql-slave
    image: mysql:8.0.33
    ports:
      - "3307:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
    volumes:
      - ./docker-compose/develop/mysql/slave/conf:/etc/mysql/conf.d
      - ./docker-compose/develop/mysql/slave/data:/var/lib/mysql
    command:
      - "--server-id=2"
      - "--log-bin=mysql-bin"
      - "--binlog-format=ROW"
      - "--gtid-mode=ON"
      - "--enforce-gtid-consistency=ON"
      - "--read-only=1"
    depends_on:
      - mysql-master
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== PostgreSQL ====================
  postgresql:
    container_name: postgresql-primary
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=auth_service
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
    volumes:
      - ./docker-compose/develop/postgresql/data:/var/lib/postgresql/data
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== MongoDB 副本集 ====================
  mongodb1:
    container_name: mongodb-node1
    image: mongo:7.0
    ports:
      - "27017:27017"
    volumes:
      - ./docker-compose/develop/mongodb/node1/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb2:
    container_name: mongodb-node2
    image: mongo:7.0
    ports:
      - "27018:27017"
    volumes:
      - ./docker-compose/develop/mongodb/node2/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb3:
    container_name: mongodb-node3
    image: mongo:7.0
    ports:
      - "27019:27017"
    volumes:
      - ./docker-compose/develop/mongodb/node3/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Redis 哨兵模式集群 ====================
  redis-master:
    container_name: redis-master
    image: redis:7.0-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass Dev1234! --appendonly yes
    volumes:
      - ./docker-compose/develop/redis/master/data:/data
    networks:
      - microservices-net
    restart: unless-stopped

  redis-slave1:
    container_name: redis-slave1
    image: redis:7.0-alpine
    ports:
      - "6380:6379"
    command: >
      redis-server --slaveof redis-master 6379
      --masterauth Dev1234!
      --requirepass Dev1234!
      --appendonly yes
    depends_on:
      - redis-master
    networks:
      - microservices-net
    restart: unless-stopped

  redis-sentinel1:
    container_name: redis-sentinel1
    image: redis:7.0-alpine
    ports:
      - "26379:26379"
    command: >
      redis-sentinel /etc/redis/sentinel.conf
    volumes:
      - ./docker-compose/develop/redis/sentinel.conf:/etc/redis/sentinel.conf
    depends_on:
      - redis-master
      - redis-slave1
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== RabbitMQ ====================
  rabbitmq:
    container_name: rabbitmq-broker
    image: rabbitmq:3.11-management-alpine
    ports:
      - "5672:5672"   # AMQP协议端口
      - "15672:15672" # 管理界面端口
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=Dev1234!
    volumes:
      - ./docker-compose/develop/rabbitmq/data:/var/lib/rabbitmq
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== MinIO ====================
  minio:
    container_name: minio-storage
    image: minio/minio
    ports:
      - "9000:9000"  # API端口
      - "9090:9090"  # 控制台端口
    environment:
      - MINIO_ROOT_USER=admin
      - MINIO_ROOT_PASSWORD=Dev1234!
    volumes:
      - ./docker-compose/develop/minio/data:/data
    command: "server /data --console-address :9090"
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Nacos ====================
  nacos:
    container_name: nacos-server
    image: nacos/nacos-server:v2.4.3
    ports:
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    environment:
      - MODE=standalone
      - MYSQL_SERVICE_HOST=mysql-master
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_USER=dev_user
      - MYSQL_SERVICE_PASSWORD=Dev1234!
    volumes:
      - ./docker-compose/develop/nacos/logs:/home/nacos/logs
      - ./docker-compose/develop/nacos/data:/home/nacos/data
      - ./docker-compose/develop/nacos/config:/home/nacos/config
    depends_on:
      - mysql-master
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Elasticsearch 集群 (3节点) ====================
  elasticsearch-node1:
    container_name: elasticsearch-node1
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node1
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node2,elasticsearch-node3
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/develop/elasticsearch/node1/data:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
    networks:
      - microservices-net
    restart: unless-stopped

  elasticsearch-node2:
    container_name: elasticsearch-node2
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node2
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node1,elasticsearch-node3
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/develop/elasticsearch/node2/data:/usr/share/elasticsearch/data
    ports:
      - "9201:9200"
    networks:
      - microservices-net
    restart: unless-stopped
    depends_on:
      - elasticsearch-node1

  elasticsearch-node3:
    container_name: elasticsearch-node3
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node3
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node1,elasticsearch-node2
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/develop/elasticsearch/node3/data:/usr/share/elasticsearch/data
    ports:
      - "9202:9200"
    networks:
      - microservices-net
    restart: unless-stopped
    depends_on:
      - elasticsearch-node1
      - elasticsearch-node2

  # ==================== Kibana ====================
  kibana:
    container_name: kibana-dashboard
    image: docker.elastic.co/kibana/kibana:8.17.0
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch-node1:9200
    depends_on:
      - elasticsearch-node1
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Sentinel ====================
  sentinel-dashboard:
    container_name: sentinel-dashboard
    image: bladex/sentinel-dashboard:1.8.6
    ports:
      - "8858:8858"
    environment:
      - AUTH_USERNAME=sentinel
      - AUTH_PASSWORD=Dev1234!
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Seata ====================
  seata-server:
    container_name: seata-server
    image: seataio/seata-server:1.6.1
    ports:
      - "8091:8091"
      - "7091:7091"
    environment:
      - SEATA_IP=seata-server
      - STORE_MODE=db
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== 监控工具 ====================
  grafana:
    container_name: grafana
    image: grafana/grafana:latest
    ports:
      - "9800:9800"
      - "9700:3000"
    restart: on-failure
    networks:
      - microservices-net

networks:
  microservices-net:
    name: microservices-dev-network
    driver: bridge
```

#### 测试环境test
```yaml
# 定义整个开发栈的名称
name: microservices-test-stack  

services:
  # ==================== MySQL 主从复制集群 ====================
  mysql-master:
    container_name: mysql-master
    image: mysql:8.0.33
    ports:
      - "3306:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=Dev1234!
      - MYSQL_DATABASE=auth_db
      - MYSQL_USER=dev_user
      - MYSQL_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
      - MYSQL_ROOT_HOST=%
    volumes:
      - ./docker-compose/test/mysql/master/conf:/etc/mysql/conf.d
      - ./docker-compose/test/mysql/master/data:/var/lib/mysql
      - ./docker-compose/test/mysql/master/backup:/backup
    command:
      - "--server-id=1"
      - "--log-bin=mysql-bin"
      - "--binlog-format=ROW"
      - "--gtid-mode=ON"
      - "--enforce-gtid-consistency=ON"
    networks:
      - microservices-net
    restart: unless-stopped

  mysql-slave:
    container_name: mysql-slave
    image: mysql:8.0.33
    ports:
      - "3307:3306"
    environment:
      - MYSQL_ROOT_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
    volumes:
      - ./docker-compose/test/mysql/slave/conf:/etc/mysql/conf.d
      - ./docker-compose/test/mysql/slave/data:/var/lib/mysql
    command:
      - "--server-id=2"
      - "--log-bin=mysql-bin"
      - "--binlog-format=ROW"
      - "--gtid-mode=ON"
      - "--enforce-gtid-consistency=ON"
      - "--read-only=1"
    depends_on:
      - mysql-master
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== PostgreSQL ====================
  postgresql:
    container_name: postgresql-primary
    image: postgres:15-alpine
    ports:
      - "5432:5432"
    environment:
      - POSTGRES_DB=auth_service
      - POSTGRES_USER=postgres
      - POSTGRES_PASSWORD=Dev1234!
      - TZ=Asia/Shanghai
    volumes:
      - ./docker-compose/test/postgresql/data:/var/lib/postgresql/data
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== MongoDB 副本集 ====================
  mongodb1:
    container_name: mongodb-node1
    image: mongo:7.0
    ports:
      - "27017:27017"
    volumes:
      - ./docker-compose/test/mongodb/node1/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb2:
    container_name: mongodb-node2
    image: mongo:7.0
    ports:
      - "27018:27017"
    volumes:
      - ./docker-compose/test/mongodb/node2/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb3:
    container_name: mongodb-node3
    image: mongo:7.0
    ports:
      - "27019:27017"
    volumes:
      - ./docker-compose/test/mongodb/node3/data:/data/db
    command: ["--replSet", "rs0", "--bind_ip_all"]
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Redis 哨兵模式集群 ====================
  redis-master:
    container_name: redis-master
    image: redis:7.0-alpine
    ports:
      - "6379:6379"
    command: redis-server --requirepass Dev1234! --appendonly yes
    volumes:
      - ./docker-compose/test/redis/master/data:/data
    networks:
      - microservices-net
    restart: unless-stopped

  redis-slave1:
    container_name: redis-slave1
    image: redis:7.0-alpine
    ports:
      - "6380:6379"
    command: >
      redis-server --slaveof redis-master 6379
      --masterauth Dev1234!
      --requirepass Dev1234!
      --appendonly yes
    depends_on:
      - redis-master
    networks:
      - microservices-net
    restart: unless-stopped

  redis-sentinel1:
    container_name: redis-sentinel1
    image: redis:7.0-alpine
    ports:
      - "26379:26379"
    command: >
      redis-sentinel /etc/redis/sentinel.conf
    volumes:
      - ./docker-compose/test/redis/sentinel.conf:/etc/redis/sentinel.conf
    depends_on:
      - redis-master
      - redis-slave1
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== RabbitMQ ====================
  rabbitmq:
    container_name: rabbitmq-broker
    image: rabbitmq:3.11-management-alpine
    ports:
      - "5672:5672"   # AMQP协议端口
      - "15672:15672" # 管理界面端口
    environment:
      - RABBITMQ_DEFAULT_USER=admin
      - RABBITMQ_DEFAULT_PASS=Dev1234!
    volumes:
      - ./docker-compose/test/rabbitmq/data:/var/lib/rabbitmq
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== MinIO ====================
  minio:
    container_name: minio-storage
    image: minio/minio
    ports:
      - "9000:9000"  # API端口
      - "9090:9090"  # 控制台端口
    environment:
      - MINIO_ROOT_USER=admin
      - MINIO_ROOT_PASSWORD=Dev1234!
    volumes:
      - ./docker-compose/test/minio/data:/data
    command: "server /data --console-address :9090"
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Nacos ====================
  nacos:
    container_name: nacos-server
    image: nacos/nacos-server:v2.4.3
    ports:
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    environment:
      - MODE=standalone
      - MYSQL_SERVICE_HOST=mysql-master
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_USER=dev_user
      - MYSQL_SERVICE_PASSWORD=Dev1234!
    volumes:
      - ./docker-compose/test/nacos/logs:/home/nacos/logs
      - ./docker-compose/test/nacos/data:/home/nacos/data
      - ./docker-compose/test/nacos/config:/home/nacos/config
    depends_on:
      - mysql-master
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Elasticsearch 集群 (3节点) ====================
  elasticsearch-node1:
    container_name: elasticsearch-node1
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node1
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node2,elasticsearch-node3
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/test/elasticsearch/node1/data:/usr/share/elasticsearch/data
    ports:
      - "9200:9200"
    networks:
      - microservices-net
    restart: unless-stopped

  elasticsearch-node2:
    container_name: elasticsearch-node2
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node2
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node1,elasticsearch-node3
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/test/elasticsearch/node2/data:/usr/share/elasticsearch/data
    ports:
      - "9201:9200"
    networks:
      - microservices-net
    restart: unless-stopped
    depends_on:
      - elasticsearch-node1

  elasticsearch-node3:
    container_name: elasticsearch-node3
    image: docker.elastic.co/elasticsearch/elasticsearch:8.17.0
    environment:
      - node.name=node3
      - cluster.name=es-dev-cluster
      - discovery.seed_hosts=elasticsearch-node1,elasticsearch-node2
      - cluster.initial_master_nodes=node1,node2,node3
      - bootstrap.memory_lock=true
      - "ES_JAVA_OPTS=-Xms512m -Xmx512m"
      - xpack.security.enabled=false
      - xpack.security.http.ssl.enabled=false
      - xpack.security.transport.ssl.enabled=false
      - network.host=0.0.0.0
      - http.port=9200
    ulimits:
      memlock:
        soft: -1
        hard: -1
    volumes:
      - ./docker-compose/test/elasticsearch/node3/data:/usr/share/elasticsearch/data
    ports:
      - "9202:9200"
    networks:
      - microservices-net
    restart: unless-stopped
    depends_on:
      - elasticsearch-node1
      - elasticsearch-node2

  # ==================== Kibana ====================
  kibana:
    container_name: kibana-dashboard
    image: docker.elastic.co/kibana/kibana:8.17.0
    ports:
      - "5601:5601"
    environment:
      - ELASTICSEARCH_HOSTS=http://elasticsearch-node1:9200
    depends_on:
      - elasticsearch-node1
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Sentinel ====================
  sentinel-dashboard:
    container_name: sentinel-dashboard
    image: bladex/sentinel-dashboard:1.8.6
    ports:
      - "8858:8858"
    environment:
      - AUTH_USERNAME=sentinel
      - AUTH_PASSWORD=Dev1234!
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== Seata ====================
  seata-server:
    container_name: seata-server
    image: seataio/seata-server:1.6.1
    ports:
      - "8091:8091"
      - "7091:7091"
    environment:
      - SEATA_IP=seata-server
      - STORE_MODE=db
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== 监控工具 ====================
  grafana:
    container_name: grafana
    image: grafana/grafana:latest
    ports:
      - "9800:9800"
      - "9700:3000"
    restart: on-failure
    networks:
      - microservices-net

networks:
  microservices-net:
    name: microservices-dev-network
    driver: bridge
```
