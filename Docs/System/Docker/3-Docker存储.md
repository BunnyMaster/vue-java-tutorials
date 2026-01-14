	# Docker存储与网络

## 数据卷

### 简介

#### 什么是数据卷

数据卷是Docker用于数据持久化的一种机制，它是一个虚拟目录，将宿主机目录映射到容器内目录。数据卷具有以下特点：

- **数据持久化**：容器删除后，数据卷仍然存在
- **数据共享**：多个容器可以挂载同一个数据卷
- **数据备份**：方便进行数据备份和迁移
- **解耦数据**：将应用数据与容器分离

#### 数据卷类型

1. **匿名卷**：自动创建，名称由Docker生成
2. **命名卷**：用户指定名称，易于管理
3. **绑定挂载**：直接挂载宿主机目录

#### 挂载数据卷

在创建容器时，使用`-v 数据卷名:容器内目录`或`--mount`参数完成挂载。

```bash
# 使用 -v 参数挂载数据卷
docker run -d --name nginx -v nginx_data:/usr/share/nginx/html nginx

# 使用 --mount 参数（更明确的语法）
docker run -d --name nginx --mount source=nginx_data,target=/usr/share/nginx/html nginx

# 绑定挂载宿主机目录
docker run -d --name nginx -v /host/path:/container/path nginx
```

容器创建时，如果发现挂载的数据卷不存在，Docker会自动创建。

### 数据卷操作命令

| 命令                    | 说明               | 示例                              |
| ----------------------- | ------------------ | --------------------------------- |
| `docker volume create`  | 创建数据卷         | `docker volume create my_volume`  |
| `docker volume ls`      | 查看所有数据卷     | `docker volume ls`                |
| `docker volume rm`      | 删除指定数据卷     | `docker volume rm my_volume`      |
| `docker volume inspect` | 查看数据卷详情     | `docker volume inspect my_volume` |
| `docker volume prune`   | 清理未使用的数据卷 | `docker volume prune`             |

### 详细命令示例

```bash
# 创建数据卷
docker volume create mysql_data

# 查看数据卷列表
docker volume ls
DRIVER    VOLUME NAME
local     mysql_data
local     nginx_data

# 查看数据卷详细信息
docker volume inspect mysql_data
[
    {
        "CreatedAt": "2024-01-01T10:00:00Z",
        "Driver": "local",
        "Labels": {},
        "Mountpoint": "/var/lib/docker/volumes/mysql_data/_data",
        "Name": "mysql_data",
        "Options": {},
        "Scope": "local"
    }
]

# 使用数据卷运行容器
docker run -d \
  --name mysql \
  -v mysql_data:/var/lib/mysql \
  -e MYSQL_ROOT_PASSWORD=123456 \
  mysql:8.0

# 清理未使用的数据卷
docker volume prune
WARNING! This will remove all local volumes not used by at least one container.
Are you sure you want to continue? [y/N] y
```

## 保存镜像
### 提交容器更改

#### docker commit

`docker commit` 命令用于将容器的当前状态保存为一个新的镜像。这对于保存对容器所做的更改非常有用。

```bash
# 基本语法
docker commit [OPTIONS] CONTAINER [REPOSITORY[:TAG]]

# 常用选项
-a, --author string    设置作者信息
-c, --change list      应用Dockerfile指令到创建的镜像
-m, --message string   提交消息
-p, --pause            在提交过程中暂停容器（默认true）

# 示例
# 1. 提交容器为新的镜像
docker commit my_container my_image:latest

# 2. 提交并添加描述信息
docker commit -m "Added new configuration" -a "John Doe" my_container my_app:v1.1

# 3. 提交时设置环境变量
docker commit -c "ENV DEBUG=false" my_container my_app:debug

# 4. 提交时暴露端口
docker commit -c "EXPOSE 8080" my_container my_app:latest

# 5. 提交运行中的nginx容器
docker exec my_nginx touch /tmp/new_file.txt
docker commit my_nginx nginx_with_changes:latest
```

### 保存镜像到文件

#### docker save

`docker save` 命令用于将一个或多个镜像保存到tar归档文件，便于迁移或备份。

```bash
# 基本语法
docker save [OPTIONS] IMAGE [IMAGE...]

# 常用选项
-o, --output string   指定输出文件（而不是STDOUT）

# 示例
# 1. 保存单个镜像到文件
docker save -o nginx.tar nginx:latest

# 2. 保存多个镜像到单个文件
docker save -o my_images.tar nginx:latest redis:alpine mysql:8.0

# 3. 使用重定向保存镜像
docker save nginx:latest > nginx_backup.tar

# 4. 保存指定标签的镜像
docker save -o myapp_v1.2.tar my_registry/my_app:v1.2

# 5. 保存所有相关镜像层
docker save -o complete_image.tar my_app:latest
```

### 从文件加载镜像

#### docker load

`docker load` 命令用于从tar归档文件或STDIN加载镜像。

```bash
# 基本语法
docker load [OPTIONS]

# 常用选项
-i, --input string   从tar归档文件读取（而不是STDIN）
-q, --quiet          静默模式，不输出加载信息

# 示例
# 1. 从文件加载镜像
docker load -i nginx.tar

# 2. 使用重定向加载镜像
docker load < nginx_backup.tar

# 3. 静默加载镜像
docker load -q -i my_app.tar

# 4. 加载并查看结果
docker load -i my_images.tar
docker images  # 查看加载的镜像

# 5. 从压缩文件加载
docker load -i my_app.tar.gz
```

### 完整工作流程示例

```bash
# 1. 运行一个容器并做修改
docker run -d --name temp_container nginx:latest
docker exec temp_container apt-get update
docker exec temp_container apt-get install -y curl

# 2. 提交更改为新镜像
docker commit -m "Added curl utility" temp_container nginx-with-curl:latest

# 3. 保存镜像到文件
docker save -o nginx-with-curl.tar nginx-with-curl:latest

# 4. 在另一台机器上加载镜像
docker load -i nginx-with-curl.tar

# 5. 验证镜像
docker images | grep nginx-with-curl
docker run --rm nginx-with-curl:latest curl --version

# 6. 清理
docker stop temp_container
docker rm temp_container
```

### 与 export/import 的区别

| 特性       | docker save/load                     | docker export/import           |
| ---------- | ------------------------------------ | ------------------------------ |
| 操作对象   | 镜像                                 | 容器                           |
| 保存内容   | 完整镜像（包括所有层、历史、元数据） | 容器文件系统（无历史、元数据） |
| 文件系统层 | 保留所有层                           | 扁平化为单层                   |
| 构建历史   | 保留                                 | 丢失                           |
| 标签信息   | 保留                                 | 需要重新标记                   |

## Dockerfile

### 实际应用场景

```bash
# 场景1：备份生产环境镜像
docker save -o production_backup_$(date +%Y%m%d).tar \
  nginx:latest \
  postgres:13 \
  redis:alpine

# 场景2：迁移镜像到无网络环境
# 在可联网机器上
docker pull my_app:latest
docker save -o my_app.tar my_app:latest

# 在无网络机器上
docker load -i my_app.tar
docker run -d --name my_app -p 8080:8080 my_app:latest

# 场景3：批量处理多个镜像
# 保存所有正在使用的镜像
docker images --format "{{.Repository}}:{{.Tag}}" | grep -v "<none>" | xargs docker save -o all_images.tar

# 场景4：版本控制镜像
docker commit -m "Version 1.2 - Added new feature" my_app_container my_app:v1.2
docker save -o my_app_v1.2.tar my_app:v1.2
```

### 基本概念

Dockerfile是一个文本文件，包含构建镜像所需的指令。每个指令都会在镜像中创建一个新的层。

### 常用指令

| 指令           | 说明        | 示例                                                 |
| ------------ | --------- | -------------------------------------------------- |
| `FROM`       | 指定基础镜像    | `FROM ubuntu:20.04`                                |
| `ENV`        | 设置环境变量    | `ENV APP_HOME=/app`                                |
| `WORKDIR`    | 设置工作目录    | `WORKDIR $APP_HOME`                                |
| `COPY`       | 拷贝本地文件到镜像 | `COPY . .`                                         |
| `ADD`        | 拷贝并解压文件   | `ADD app.tar.gz /tmp/`                             |
| `RUN`        | 执行命令      | `RUN apt-get update && apt-get install -y python3` |
| `EXPOSE`     | 声明容器端口    | `EXPOSE 8080`                                      |
| `CMD`        | 容器启动命令    | `CMD ["python3", "app.py"]`                        |
| `ENTRYPOINT` | 入口点命令     | `ENTRYPOINT ["java", "-jar"]`                      |
| `ARG`        | 构建时变量     | `ARG VERSION=latest`                               |
| `LABEL`      | 添加元数据     | `LABEL version="1.0"`                              |
| `USER`       | 指定运行用户    | `USER nobody`                                      |
| `VOLUME`     | 创建挂载点     | `VOLUME /data`                                     |

### Dockerfile示例

```dockerfile
# 使用官方Python运行时作为基础镜像
FROM python:3.9-slim

# 设置环境变量
ENV PYTHONUNBUFFERED=1 \
    APP_HOME=/app

# 设置工作目录
WORKDIR $APP_HOME

# 复制依赖文件
COPY requirements.txt .

# 安装依赖
RUN pip install --no-cache-dir -r requirements.txt

# 复制应用代码
COPY . .

# 创建非root用户
RUN useradd --create-home --shell /bin/bash app \
    && chown -R app:app $APP_HOME
USER app

# 暴露端口
EXPOSE 8000

# 定义健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8000/health || exit 1

# 启动命令
CMD ["gunicorn", "app:app", "--bind", "0.0.0.0:8000"]
```

## Docker网络

容器访问容器的网络，需要将这两个容器加入到同一个容器，之后通过端口访问(容器端口)访问时使用容器名而非宿主机IP地址

### 网络类型

Docker提供多种网络驱动：

1. **bridge**：默认网络驱动，容器通过虚拟网桥连接
2. **host**：容器直接使用宿主机的网络栈
3. **overlay**：用于Swarm集群，连接多个Docker守护进程
4. **macvlan**：为容器分配MAC地址，使其显示为物理设备
5. **none**：禁用所有网络

### 默认网络架构

默认情况下，所有容器都通过bridge方式连接到Docker的虚拟网桥上。加入自定义网络的容器可以通过容器名互相访问。

### 网络操作命令

| 命令                          | 说明       | 示例                                                  |
| --------------------------- | -------- | --------------------------------------------------- |
| `docker network create`     | 创建网络     | `docker network create my_network`                  |
| `docker network ls`         | 查看所有网络   | `docker network ls`                                 |
| `docker network rm`         | 删除网络     | `docker network rm my_network`                      |
| `docker network prune`      | 清理未使用的网络 | `docker network prune`                              |
| `docker network connect`    | 连接容器到网络  | `docker network connect my_network my_container`    |
| `docker network disconnect` | 从网络断开容器  | `docker network disconnect my_network my_container` |
| `docker network inspect`    | 查看网络详情   | `docker network inspect my_network`                 |

### 网络操作示例

```bash
# 创建自定义网络
docker network create --driver bridge my_app_network

# 查看网络列表
docker network ls
NETWORK ID     NAME            DRIVER    SCOPE
abc123...      bridge          bridge    local
def456...      host            host      local
ghi789...      my_app_network  bridge    local
jkl012...      none            null      local

# 在指定网络中运行容器
docker run -d \
  --name web \
  --network my_app_network \
  -p 80:80 \
  nginx

docker run -d \
  --name api \
  --network my_app_network \
  -p 8080:8080 \
  my_api_image

# 容器间通过容器名通信（在my_app_network网络中）
# web容器可以通过 "api" 主机名访问api容器

# 查看网络详情
docker network inspect my_app_network

# 将现有容器连接到网络
docker network connect my_app_network existing_container

# 从网络断开容器
docker network disconnect my_app_network existing_container

# 创建带自定义子网的网络
docker network create \
  --driver bridge \
  --subnet=172.20.0.0/16 \
  --gateway=172.20.0.1 \
  my_custom_network
```

### 网络配置示例

```bash
# 创建用于微服务的网络
docker network create \
  --driver bridge \
  --attachable \
  microservices

# 运行数据库容器
docker run -d \
  --name postgres \
  --network microservices \
  -e POSTGRES_PASSWORD=secret \
  -v postgres_data:/var/lib/postgresql/data \
  postgres:13

# 运行应用容器
docker run -d \
  --name app \
  --network microservices \
  -p 8080:8080 \
  -e DATABASE_URL=postgres://postgres:secret@postgres:5432/mydb \
  my_app_image
```
