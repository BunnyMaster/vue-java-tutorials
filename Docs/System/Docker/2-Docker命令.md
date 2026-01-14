# Docker命令

`docker run` 命令中的常见参数：
- `-d`：让容器后台运行
- `--name`：给容器命名
- `-e`：设置环境变量
- `-p`：将宿主机端口映射到容器内端口
- `-v`：挂载数据卷
- `--network`：指定容器网络
- `Repository:tag`：镜像名:版本号

## 镜像管理

### 搜索镜像

**docker search**

```bash
# 搜索镜像
docker search nginx

# 限制搜索结果数量
docker search --limit 5 nginx
```

**推荐直接去官网搜索下载：**

[Docker Hub 官方网站](https://hub.docker.com/)

### 查看镜像

**docker images**

```bash
# 查看所有镜像
docker images

# 常用选项
-a, --all：    展示所有镜像（包括中间层镜像）
-q, --quiet：  只展示镜像ID
--no-trunc：   显示完整的镜像信息
--digests：    显示镜像的摘要信息

# 示例
docker images -aq
```

### 拉取镜像

**docker pull**

```bash
# 拉取最新版本镜像
docker pull nginx

# 拉取指定版本镜像
docker pull nginx:1.26.0

# 拉取指定平台的镜像
docker pull --platform linux/amd64 nginx
```

### 删除镜像

**docker rmi**

```bash
# 使用镜像名和标签删除
docker rmi nginx:latest

# 使用镜像ID删除（推荐）
docker rmi 3f8a4339aadd

# 强制删除镜像
docker rmi -f 3f8a4339aadd

# 删除所有镜像
docker rmi -f $(docker images -aq)

# 清理悬空镜像（无标签镜像）
docker image prune
```

## 容器管理

### 运行容器

**docker run**

```bash
# 基本参数说明
--name：为容器指定一个名称；
-d：后台运行容器，并返回容器ID，即启动守护式容器；
    非后台运行会阻塞控制台；
    常见问题：当后台运行容器时，若容器没有提供服务，则会自动停止；
-i, -t, -it：以交互模式运行容器；为容器重新分配一个伪输入终端；二者通常同时使用；
             交互方式运行，可进入容器内部查看内容；
             exit：停止容器并退回主机；
             Ctrl + P + Q：不停止容器退出；

-p：端口映射，将容器内的端口映射到宿主机端口，格式如下：
    -p ip:主机端口:容器端口 
    -p 主机端口:容器端口（常用）
    -p 容器端口
    容器端口

-P：随机端口映射；
-v：数据卷挂载；
-w：指定命令执行时所在的路径；
--restart：设置容器重启策略；
--network：指定容器网络；

# 常用示例
# 后台运行nginx
docker run -d --name nginx -p 80:80 nginx

# 交互式运行ubuntu容器
docker run -it --name my-ubuntu ubuntu:20.04 /bin/bash

# 带环境变量运行
docker run -d --name mysql -e MYSQL_ROOT_PASSWORD=123456 -p 3306:3306 mysql:8.0

# 挂载数据卷
docker run -d --name nginx -p 80:80 -v /host/path:/container/path nginx
```

### 停止容器

**docker stop**

```bash
# 基本语法
docker stop [OPTIONS] CONTAINER [CONTAINER...]

# 常用选项
-t, --time int：指定在发送 SIGTERM 信号后等待容器停止的秒数。
容器中的应用程序可以捕获 SIGTERM 信号并执行清理操作，如保存状态、关闭连接、释放资源等再退出。
如果在指定时间内容器未停止，Docker 将发送 SIGKILL 信号强制停止容器。
默认情况下，docker stop 会等待 10 秒。

# 示例
docker stop my-nginx                    # 停止容器，默认等待10秒
docker stop -t 20 my-nginx              # 容器自行停止或20秒后强制停止
docker stop container1 container2       # 同时停止多个容器
```

**docker kill**

```bash
# 立即终止容器
docker kill my-nginx

# 发送指定信号
docker kill --signal=SIGTERM my-nginx

# 强制立即终止
docker kill --signal=SIGKILL my-nginx
```

**docker pause/unpause**

```bash
# 暂停容器
docker pause my-nginx

# 恢复暂停的容器
docker unpause my-nginx
```

### 查看容器

**docker ps**

```bash
# 查看运行中的容器
docker ps

# 常用选项
-a, --all：查看所有容器（包括已停止的）
-n, --last int：展示最近运行的指定数量容器
-q, --quiet：只显示容器ID
-s, --size：显示容器大小
--no-trunc：不截断输出
-f, --filter：根据条件过滤输出

# 示例
docker ps -a                          # 查看所有容器
docker ps -n 3                        # 查看最近运行的3个容器
docker ps -q                          # 只显示容器ID
docker ps -f "status=running"         # 查看运行中的容器
docker ps -f "name=nginx"             # 查看名称包含nginx的容器
```

### 删除容器

**docker rm**

```bash
# 删除已停止的容器
docker rm my-nginx

# 强制删除运行中的容器
docker rm -f my-nginx

# 删除所有已停止的容器
docker container prune

# 删除所有容器（包括运行中的）
docker rm -f $(docker ps -aq)

# 删除容器时同时删除关联的匿名卷
docker rm -v my-nginx
```

### 启动和重启容器

**docker start**

```bash
# 启动已停止的容器
docker start my-nginx

# 启动容器并附加交互式终端
docker start -i my-nginx
```

**docker restart**

```bash
# 重启容器
docker restart my-nginx

# 设置重启超时时间
docker restart -t 20 my-nginx
```

### 容器状态和日志

**docker stats**

```bash
# 实时查看容器资源使用情况
docker stats

# 查看指定容器
docker stats my-nginx

# 不流式输出，只显示当前状态
docker stats --no-stream

# 格式化输出
docker stats --format "table {{.Name}}\t{{.CPUPerc}}\t{{.MemUsage}}"
```

**docker logs**

```bash
# 查看容器日志
docker logs my-nginx

# 常用选项
-f, --follow：实时跟踪日志输出
--tail string：从日志末尾显示的行数（默认"all"）
-t, --timestamps：显示时间戳
--since string：显示自某个时间之后的日志
--until string：显示到某个时间之前的日志

# 示例
docker logs -f my-nginx               # 实时查看日志
docker logs --tail 50 my-nginx        # 查看最后50行日志
docker logs -t my-nginx               # 显示带时间戳的日志
docker logs --since 2024-01-01 my-nginx  # 查看指定时间后的日志
```

### 其他常用命令

**docker exec**

```bash
# 在运行中的容器中执行命令
docker exec -it my-nginx /bin/bash

# 在容器中执行单次命令
docker exec my-nginx ls -l /etc/nginx
```

**docker inspect**

```bash
# 查看容器详细信息
docker inspect my-nginx

# 查看特定信息
docker inspect -f '{{.NetworkSettings.IPAddress}}' my-nginx
```

**docker cp**

```bash
# 从容器复制文件到主机
docker cp my-nginx:/etc/nginx/nginx.conf ./nginx.conf

# 从主机复制文件到容器
docker cp ./config.txt my-nginx:/tmp/config.txt
```
