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

### 开发环境develop

#### 常见问题

1. 关于sonarqube错误解决

```
sudo chown -R 1000:1000 ~/docker-compose/develop/sonarqube/
```

根据你的环境可以修改下面的名称：`name: microservices-dev-stack  `

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
      - ~/docker-compose/develop/mysql/master/conf:/etc/mysql/conf.d
      - ~/docker-compose/develop/mysql/master/data:/var/lib/mysql
      - ~/docker-compose/develop/mysql/master/backup:/backup
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
      - ~/docker-compose/develop/mysql/slave/conf:/etc/mysql/conf.d
      - ~/docker-compose/develop/mysql/slave/data:/var/lib/mysql
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
      - ~/docker-compose/develop/postgresql/data:/var/lib/postgresql/data
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
      - ~/docker-compose/develop/mongodb/node1/data:/data/db
    command: [ "--replSet", "rs0", "--bind_ip_all" ]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb2:
    container_name: mongodb-node2
    image: mongo:7.0
    ports:
      - "27018:27017"
    volumes:
      - ~/docker-compose/develop/mongodb/node2/data:/data/db
    command: [ "--replSet", "rs0", "--bind_ip_all" ]
    networks:
      - microservices-net
    restart: unless-stopped

  mongodb3:
    container_name: mongodb-node3
    image: mongo:7.0
    ports:
      - "27019:27017"
    volumes:
      - ~/docker-compose/develop/mongodb/node3/data:/data/db
    command: [ "--replSet", "rs0", "--bind_ip_all" ]
    networks:
      - microservices-net
    restart: unless-stopped

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
      - ~/docker-compose/develop/minio/data:/data
    command: "server /data --console-address :9090"
    networks:
      - microservices-net
    restart: unless-stopped

  # ==================== GitLab ====================
  gitlab:
    container_name: gitlab
    image: gitlab/gitlab-ce:latest
    hostname: 'gitlab.local'
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://gitlab.local:8929'
        gitlab_rails['gitlab_shell_ssh_port'] = 2224
        gitlab_rails['time_zone'] = 'Asia/Shanghai'
        gitlab_rails['gitlab_email_enabled'] = true
        gitlab_rails['gitlab_email_from'] = 'gitlab@local.dev'
        gitlab_rails['smtp_enable'] = false
        gitlab_rails['gitlab_default_can_create_group'] = true
        gitlab_rails['gitlab_username_changing_enabled'] = true
        gitlab_rails['initial_root_password'] = 'Dev1234!'
        gitlab_rails['initial_shared_runners_registration_token'] = 'GR1348941'
        gitlab_rails['gitlab_default_projects_features_issues'] = true
        gitlab_rails['gitlab_default_projects_features_merge_requests'] = true
        gitlab_rails['gitlab_default_projects_features_wiki'] = true
        gitlab_rails['gitlab_default_projects_features_snippets'] = true
        gitlab_rails['gitlab_default_projects_features_builds'] = true
    ports:
      - "8929:8929"   # HTTP端口
      - "2224:22"     # SSH端口
      - "8443:443"     # HTTPS端口
    volumes:
      - ~/docker-compose/develop/gitlab/config:/etc/gitlab
      - ~/docker-compose/develop/gitlab/logs:/var/log/gitlab
      - ~/docker-compose/develop/gitlab/data:/var/opt/gitlab
    networks:
      - microservices-net
    restart: unless-stopped
    shm_size: '256m'

  # ==================== GitLab Runner ====================
  gitlab-runner:
    container_name: gitlab-runner
    image: gitlab/gitlab-runner:latest
    volumes:
      - ~/docker-compose/develop/gitlab-runner/config:/etc/gitlab-runner
      - /var/run/docker.sock:/var/run/docker.sock
    environment:
      - DOCKER_HOST=unix:///var/run/docker.sock
    networks:
      - microservices-net
    restart: unless-stopped
    depends_on:
      - gitlab

  # ==================== Jenkins (可选，作为CI/CD的替代方案) ====================
  jenkins:
    container_name: jenkins
    image: jenkins/jenkins:lts-jdk17
    ports:
      - "8080:8080"
      - "50000:50000"
    environment:
      - JAVA_OPTS=-Djenkins.install.runSetupWizard=false
      - JENKINS_ADMIN_ID=admin
      - JENKINS_ADMIN_PASSWORD=Dev1234!
    volumes:
      - ~/docker-compose/develop/jenkins/data:/var/jenkins_home
      - /var/run/docker.sock:/var/run/docker.sock
      - ~/docker-compose/develop/jenkins/jobs:/var/jenkins_home/jobs
    networks:
      - microservices-net
    restart: unless-stopped
    user: root

  # ==================== SonarQube (代码质量检查) ====================
  sonarqube:
    container_name: sonarqube
    image: sonarqube:lts-community
    ports:
      - "9001:9000"
    environment:
      - SONAR_JDBC_URL=jdbc:postgresql://postgresql:5432/sonarqube
      - SONAR_JDBC_USERNAME=postgres
      - SONAR_JDBC_PASSWORD=Dev1234!
      - SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true
      - SONAR_WEB_JAVAADDITIONALOPTS=-Djava.security.egd=file:/dev/./urandom
    volumes:
      - ~/docker-compose/develop/sonarqube/data:/opt/sonarqube/data
      - ~/docker-compose/develop/sonarqube/extensions:/opt/sonarqube/extensions
      - ~/docker-compose/develop/sonarqube/logs:/opt/sonarqube/logs
    depends_on:
      - postgresql
    networks:
      - microservices-net
    restart: unless-stopped
    # 添加健康检查
    healthcheck:
      test: [ "CMD", "curl", "-f", "http://localhost:9000/api/system/status" ]
      interval: 30s
      timeout: 10s
      retries: 3

  # ==================== Nacos ====================
  nacos:
    container_name: nacos-server
    image: nacos/nacos-server:v3.1.0
    ports:
      - "8081:8080"
      - "8848:8848"
      - "9848:9848"
      - "9849:9849"
    environment:
      - MODE=standalone
      - NACOS_AUTH_TOKEN=QnVubnktQXV0aC1TZXJ2ZXItUHJpdmF0ZS1TZWNyZXRLZXk=
      - NACOS_AUTH_IDENTITY_KEY=nacosAdmin
      - NACOS_AUTH_IDENTITY_VALUE=Dev1234!
      - MYSQL_SERVICE_HOST=mysql-master
      - MYSQL_SERVICE_PORT=3306
      - MYSQL_SERVICE_DB_NAME=nacos
      - MYSQL_SERVICE_USER=dev_user
      - MYSQL_SERVICE_PASSWORD=Dev1234!
    volumes:
      - ~/docker-compose/develop/nacos/logs:/home/nacos/logs
      - ~/docker-compose/develop/nacos/data:/home/nacos/data
      - ~/docker-compose/develop/nacos/config:/home/nacos/config
    depends_on:
      - mysql-master
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

  # ==================== gitea ====================
  gitea:
    image: gitea/gitea:1.25
    container_name: gitea
    environment:
      - USER_UID=1000
      - USER_GID=1000
      - GITEA__database__DB_TYPE=mysql
      - GITEA__database__HOST=mysql-master
      - GITEA__database__NAME=gitea
      - GITEA__database__USER=root
      - GITEA__database__PASSWD=Dev1234!
    restart: always
    volumes:
      - ~/docker-compose/develop/gitea:/data
      - /etc/timezone:/etc/timezone:ro
      - /etc/localtime:/etc/localtime:ro
    ports:
      - "3000:3000"
      - "222:22"
    depends_on:
      - mysql-master
    networks:
      - microservices-net

networks:
  microservices-net:
    name: microservices-dev-network
    driver: bridge
```

## 常见问题

### MySQL环境

如果需要忽略大小写，或者需要下面的配置

#### 配置my.cnf

将下面的`my.cnf`复制到对应的`xxx/conf/my.cnf`文件中

```sh
# 主机
mkdir -p /home/develop/docker-compose/develop/mysql/master/conf
cd /home/develop/docker-compose/develop/mysql/master/conf
sudo vim /home/develop/docker-compose/develop/mysql/master/conf/my.cnf

# 从机
mkdir -p /home/develop/docker-compose/develop/mysql/slave/conf
cd /home/develop/docker-compose/develop/mysql/slave/conf
sudo vim /home/develop/docker-compose/develop/mysql/slave/conf/my.cnf
```

**my.cnf**

```properties
[mysqld]
skip-host-cache
skip-name-resolve
secure-file-priv=/var/lib/mysql-files
user=mysql

# 设置字符集
character-set-server=utf8mb4
collation-server=utf8mb4_unicode_ci

# 设置服务器ID（如果是复制集群，确保每个节点的ID唯一）
server-id=1

# 启用二进制日志
log-bin=mysql-bin

# 设置表名不区分大小写
lower_case_table_names = 1
```

### ES无法启动

#### 没有权限

路径是上面的`develop`路径，如果变化需要修改路径

```sh
sudo chown -R 1000:1000 docker-compose/develop/elasticsearch
```

### MongoDB问题

#### 无法连接

可能原因：

-   **DataGrip** 可能只连接主节点，不尝试解析其他节点
-   **MongoDB Compass** 会尝试解析所有副本集成员的主机名 (`mongodb-node2`, `mongodb-node3`)
-   这些主机名只在 Docker 内部网络中可以解析，外部网络无法解析

**尝试方案一**

第一次是可以启动的并且可以连接的，但是虚拟机重启或者电脑重启就无法连接了。

```sh
# 1. 停止所有服务
docker compose down

# 2. 启动所有服务
docker compose up -d

# 3. 等待所有服务启动（约30秒），然后初始化 MongoDB 副本集
sleep 30
docker exec -it mongodb-node1 mongosh --eval "rs.initiate({
  _id: 'rs0',
  members: [
    {_id: 0, host: 'mongodb-node1:27017', priority: 5},
    {_id: 1, host: 'mongodb-node2:27017', priority: 3},
    {_id: 2, host: 'mongodb-node3:27017', priority: 1, arbiterOnly: true}
  ]
})"

# 4. 检查服务状态
docker compose ps
docker compose logs
```

**尝试方案二**

这里有个情况DataGrip可以连接，但是MongoDB Compass无法连接，如果在日志中也没有找到特别的错误，可以尝试下面的方法。

在`host`文件中添加你的**IP地址**和**docker容器名称**：

```sh
192.168.2.19 mongodb-node1
192.168.2.19 mongodb-node2  
192.168.2.19 mongodb-node3
```

如果可以直接连接就可以跳过下面这步了，如果不行继续尝试在MongoDB Compass中修改连接字符串

```sh
mongodb://admin:Dev1234!@192.168.2.19:27017,192.168.2.19:27018,192.168.2.19:27019/?replicaSet=rs0&authSource=admin
```

### Sonarqube问题

#### 权限问题

```
docker logs sonarqube 
2025.12.18 15:02:23 INFO  app[][o.s.a.AppFileSystem] Cleaning or creating temp directory /opt/sonarqube/temp
2025.12.18 15:02:23 INFO  app[][o.s.a.es.EsSettings] Elasticsearch listening on [HTTP: 127.0.0.1:9001, TCP: 127.0.0.1:34017]
2025.12.18 15:02:23 INFO  app[][o.s.a.ProcessLauncherImpl] Launch process[ELASTICSEARCH] from [/opt/sonarqube/elasticsearch]: /opt/sonarqube/elasticsearch/bin/elasticsearch
2025.12.18 15:02:23 INFO  app[][o.s.a.SchedulerImpl] Waiting for Elasticsearch to be up and running
2025-12-18 15:02:24,756 main ERROR Unable to create file /opt/sonarqube/logs/es.log java.io.IOException: Permission denied
	at java.base/java.io.UnixFileSystem.createFileExclusively(Native Method)
	at java.base/java.io.File.createNewFile(Unknown Source)
	at org.apache.logging.log4j.core.appender.rolling.RollingFileManager$RollingFileManagerFactory.cre
```

将下面的目录改成你的目录地址

```bash
mkdir -p ~/develop/docker-compose/develop/sonarqube/{data,logs,extensions}
sudo chown -R 1000:1000 ~/develop/docker-compose/develop/sonarqube/
sudo chmod -R 755 ~/develop/docker-compose/develop/sonarqube/
```

#### 没有创建数据库

到postgres中创建`sonarqube`数据库即可

#### 推送不成功

需要生成Token放到`SONAR_TOKEN`中

![image-20251219005338390](./assets/image-20251219005338390.png)

#### 配置地址

| 名称           | 值                                                 |
| -------------- | -------------------------------------------------- |
| SONAR_HOST_URL | 选项修改成：`Visible` </br> 取消`Protect variable` |
| SONAR_TOKEN    | 选项修改成：`Visible` </br> 取消`Protect variable` |

![image-20251219004703776](./assets/image-20251219004703776.png)

### Gitlab问题

#### 权限问题

```
        - create symlink at /opt/gitlab/service/gitlab-workhorse to /opt/gitlab/sv/gitlab-workhorse
      * directory[/opt/gitlab/service/gitlab-workhorse/supervise] action create[2025-12-19T02:09:31+00:00] INFO: directory[/opt/gitlab/service/gitlab-workhorse/supervise] created directory /opt/gitlab/service/gitlab-workhorse/supervise

        - create new directory /opt/gitlab/service/gitlab-workhorse/supervise[2025-12-19T02:09:31+00:00] INFO: directory[/opt/gitlab/service/gitlab-workhorse/supervise] mode changed to 755

        - change mode from '' to '0755'
      * directory[/opt/gitlab/service/gitlab-workhorse/log/supervise] action create[2025-12-19T02:09:31+00:00] INFO: directory[/opt/gitlab/service/gitlab-workhorse/log/supervise] created directory /opt/gitlab/service/gitlab-workhorse/log/supervise

        - create new directory /opt/gitlab/service/gitlab-workhorse/log/supervise[2025-12-19T02:09:31+00:00] INFO: directory[/opt/gitlab/service/gitlab-workhorse/log/supervise] mode changed to 755

        - change mode from '' to '0755'

```

**解决方式**

```bash
sudo mkdir -p ~/develop/docker-compose/develop/gitlab/{data,logs,extensions}
sudo chown -R 1000:1000 ~/develop/docker-compose/develop/gitlab/
sudo chmod -R 755 ~/develop/docker-compose/develop/gitlab/

# 删除现有数据目录
sudo rm -rf ~/develop/docker-compose/develop/gitlab/data/*
sudo rm -rf ~/develop/docker-compose/develop/gitlab/logs/*
sudo rm -rf ~/develop/docker-compose/develop/gitlab/config/*

# 创建目录并设置官方推荐的权限
sudo mkdir -p ~/develop/docker-compose/develop/gitlab/{config,logs,data}
sudo chown -R 1000:1000 ~/develop/docker-compose/develop/gitlab/
sudo chmod -R 777 ~/develop/docker-compose/develop/gitlab/
```

