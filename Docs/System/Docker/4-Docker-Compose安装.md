# Docker-Compose使用

## 配置docker-compose

> [!WARNING]
>
> 如果安装最新docker是存在`docker compose`的。
>
> 如果没有这个可以尝试下面的安装。

### 安装方式

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

### 常用命令

**Docker Compose 命令速查表**

| 命令                     | 作用描述                                              | 常用参数及示例                                               |
| :----------------------- | :---------------------------------------------------- | :----------------------------------------------------------- |
| `docker-compose up`      | **核心命令**：构建镜像、（重新）创建、启动所有服务。  | `-d`：后台运行。   `--build`：启动前重新构建镜像。   `--force-recreate`：强制重建容器。   **示例**：`docker-compose up -d --build` |
| `docker-compose down`    | **停止并清理**：停止容器并移除容器、网络。            | `-v`：同时删除**命名数据卷**（数据会丢失！）。   `--rmi all`：删除所有相关镜像。   **示例**：`docker-compose down -v` |
| `docker-compose start`   | **启动**：启动已存在但处于停止状态（stopped）的容器。 | **示例**：`docker-compose start`                             |
| `docker-compose stop`    | **停止**：停止正在运行的容器，但不删除它们。          | **示例**：`docker-compose stop`                              |
| `docker-compose restart` | **重启**：重启服务容器。                              | **示例**：`docker-compose restart web`                       |
| `docker-compose pause`   | **暂停**：暂停服务容器的所有进程。                    | **示例**：`docker-compose pause web`                         |
| `docker-compose unpause` | **恢复**：恢复被暂停的服务容器。                      | **示例**：`docker-compose unpause web`                       |
| `docker-compose run`     | **一次性运行**：在新的容器中执行一次性命令。          | `--rm`：运行后自动删除容器。   `-e`：设置环境变量。   **示例**：`docker-compose run --rm web bash` |
| `docker-compose exec`    | **执行命令**：在**正在运行**的容器中执行命令。        | `-d`：后台执行命令。   `-u`：指定用户。   **示例**：`docker-compose exec web bash` |
| `docker-compose logs`    | **查看日志**：查看服务的日志输出。                    | `-f`：实时跟踪日志。   `--tail=N`：仅显示最后N行。   **示例**：`docker-compose logs -f web` |
| `docker-compose ps`      | **查看状态**：列出项目中的所有容器及其状态。          | **示例**：`docker-compose ps`                                |
| `docker-compose build`   | **构建镜像**：构建或重新构建服务的镜像。              | `--no-cache`：构建时不使用缓存。   **示例**：`docker-compose build --no-cache web` |
| `docker-compose pull`    | **拉取镜像**：拉取服务所需的镜像，但不启动容器。      | **示例**：`docker-compose pull`                              |
| `docker-compose config`  | **检查配置**：验证和查看已解析的 Compose 文件配置。   | `-q`：只验证，不输出（静默模式）。   **示例**：`docker-compose config` |
| `docker-compose images`  | **列出镜像**：列出项目中所有容器使用的镜像。          | **示例**：`docker-compose images`                            |
| `docker-compose top`     | **查看进程**：显示每个容器内运行的进程。              | **示例**：`docker-compose top`                               |
| `docker-compose port`    | **查看端口**：显示某个端口映射到的公共端口。          | **示例**：`docker-compose port web 80`                       |
| `docker-compose kill`    | **强制停止**：通过发送 SIGKILL 信号来强制停止容器。   | `-s SIGINT`：指定其他信号。   **示例**：`docker-compose kill -s SIGINT` |

**常用操作流程示例**

| 场景             | 命令组合                                                   |
| :------------- | :----------------------------------------------------- |
| **首次启动/重新部署**  | `docker-compose up -d --build`                         |
| **查看实时日志**     | `docker-compose logs -f`                               |
| **进入容器调试**     | `docker-compose exec <service_name> sh` (或 `bash`)     |
| **执行数据库迁移**    | `docker-compose run --rm web python manage.py migrate` |
| **优雅停止并保留数据**  | `docker-compose down`                                  |
| **彻底清理（含数据卷）** | `docker-compose down -v`                               |
| **仅重启某个服务**    | `docker-compose restart <service_name>`                |
| **扩容服务实例**     | `docker-compose up -d --scale <service_name>=3`        |
