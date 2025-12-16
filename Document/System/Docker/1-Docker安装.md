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
sudo apt update
sudo apt upgrade
sudo apt-get install ca-certificates curl gnupg lsb-release
# 添加Docker官方GPG密钥
sudo curl -fsSL http://mirrors.aliyun.com/docker-ce/linux/ubuntu/gpg | sudo apt-key add -
sudo add-apt-repository "deb [arch=amd64] http://mirrors.aliyun.com/docker-ce/linux/ubuntu $(lsb_release -cs) stable"
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

# 创建分组一般
sudo groupadd docker
# 将当前用户添加到分组
sudo usermod -aG docker $USER
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