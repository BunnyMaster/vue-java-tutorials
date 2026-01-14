## Ubuntu基础环境

### 卸载Docker

```bash
yum remove docker \
    docker-client \
    docker-client-latest \
    docker-common \
    docker-latest \
    docker-latest-logrotate \
    docker-logrotate \
    docker-engine \
    docker-selinux
```

### 安装Docker

```shell
sudo apt-get remove docker docker-engine docker.io containerd runc

# 添加阿里云Docker仓库的正确方法
sudo mkdir -p /etc/apt/keyrings
curl -fsSL https://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg

# 添加阿里云源
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://mirrors.aliyun.com/docker-ce/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# 安装docker# 安装docker
sudo apt update
sudo apt install docker-ce docker-ce-cli containerd.io docker-compose-plugin
sudo apt upgrade

# 重启终端生效
exit
```

#### 配置镜像

```shell
sudo mkdir /etc/docker
sudo touch /etc/docker/daemon.json
sudo chmod 777 /etc/docker/daemon.json
sudo vim /etc/docker/daemon.json

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

sudo systemctl restart docker.socket
```

### 安装 Docker-Compose

>[!MOTE]
>目前新版的Docker已经内置了Docker Compose下面的是CentOS系统安装示例

```bash
sudo yum install docker-ce docker-ce-cli containerd.io

sudo yum install curl
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.4/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

sudo docker-compose --version
```