## 环境搭建

### Docker镜像源设置

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

使用docker-compose有的时候会因为版本不同，但是配置文件主要内容就是这些。需要注意版本问题

### 配置相关

#### MySQL配置问题

| **特性**     | my.cnf                       |      conf.d **目录**       |
| ------------ | :--------------------------- | :------------------------: |
| **文件类型** | 单个文件                     | 目录，包含多个 `.cnf` 文件 |
| **配置方式** | 集中式配置                   |         分布式配置         |
| **优先级**   | 高（覆盖 `conf.d` 中的配置） |   低（被 `my.cnf` 覆盖）   |
| **适用场景** | 全局配置，核心配置           | 模块化配置，便于扩展和维护 |

#### MongoDB配置

```bash
sudo mkdir -p ~/docker/docker_data/mongo/conf
sudo mkdir -p ~/docker/docker_data/mongo/logs
sudo chmod 777 ~/docker/docker_data/mongo/logs
sudo chmod 777 ~/docker/docker_data/mongo/conf

cd ~/docker/docker_data/mongo/logs
sudo touch mongod.log
sudo chmod 755 mongod.log

cd ~/docker/docker_data/mongo/conf
sudo vim mongod.conf

cd ~
```

##### 配置文件

```bash
# 数据库文件存储位置
dbpath = /data/db
# log文件存储位置
logpath = /data/log/mongod.log
# 使用追加的方式写日志
logappend = true
# 是否以守护进程方式运行
# fork = true
# 全部ip可以访问
bind_ip = 0.0.0.0
# 端口号
port = 27017
# 是否启用认证
auth = true
# 设置oplog的大小(MB)
oplogSize=2048
```

##### 设置账户密码

```shell
#进入容器
docker exec -it mongodb /bin/bash

#进入mongodb shell
mongosh --port 27017

#切换到admin库
use admin

#创建账号/密码
db.createUser({ user: 'admin', pwd: '02120212', roles: [ { role: "root", db: "admin" } ] });
# db.createUser({ user: 'admin', pwd: '123456', roles: [ { role: "userAdminAnyDatabase", db: "admin" } ] });
```

### docker-compose.yml

如果休要所有的微服务环境，可以直接复制下面的内容，看清楚目录是否和自己需要的一样。

| 功能                 | 旧版 (docker-compose)   | 新版 (docker  compose)  |
| -------------------- | ----------------------- | ----------------------- |
| **启动服务**         | docker-compose  up -d   | docker  compose up -d   |
| **停止服务**         | docker-compose  down    | docker  compose down    |
| **查看日志**         | docker-compose  logs -f | docker  compose logs -f |
| **列出容器**         | docker-compose  ps      | docker  compose ps      |
| **停止不删除容器**   | docker-compose  stop    | docker  compose stop    |
| **启动已停止的容器** | docker-compose  start   | docker  compose start   |
| **重启服务**         | docker-compose  restart | docker  compose restart |
| **构建镜像**         | docker-compose  build   | docker  compose build   |

