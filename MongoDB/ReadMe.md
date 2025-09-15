# 环境搭建

## 安装MongoDB

### 设置权限

```sh
sudo mkdir -p ~/docker/docker_data/mongo/conf
sudo mkdir -p ~/docker/docker_data/mongo/logs
sudo chmod 777 ~/docker/docker_data/mongo/logs
sudo chmod 777 ~/docker/docker_data/mongo/conf

cd ~/docker/docker_data/mongo/logs
sudo touch mongod.log
sudo chmod 777 mongod.log

cd ~/docker/docker_data/mongo/conf
sudo vim mongod.conf
```

**配置文件**

```properties
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

#### 执行安装

```sh
docker run -it \
    --name mongodb \
    --restart=always \
    --privileged \
    -p 27017:27017 \
    -v ~/docker/docker_data/mongo/data:/data/db \
    -v ~/docker/docker_data/mongo/conf:/data/configdb \
    -v ~/docker/docker_data/mongo/logs:/data/log/  \
    -d mongo:latest \
    -f /data/configdb/mongod.conf
```

### 设置账号密码

```sh
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

### 移除Docker镜像 

```sh
docker stop mongodb
docker rm mongodb
```

## 常用链接速查

| 名称            | 地址                                         | 备注 |
| --------------- | -------------------------------------------- | ---- |
| MondoDB Compass | https://www.mongodb.com/try/download/compass |      |

