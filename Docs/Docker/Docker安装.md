# Docker安装

Docker是一个开源的应用容器引擎，它允许开发者打包他们的应用以及应用的依赖包到一个可移植的容器中，然后发布到任何流行的Linux机器或Windows服务器上。容器是完全使用**沙箱机制**(程序受控的环境)，相互之间不会有任何接口（类似iPhone的app）而且更轻量级。

## 卸载Docker

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

## 安装Docker

### CentOS

```shell
cd /etc/yum.repos.d/
sed -i 's/mirrorlist/#mirrorlist/g' /etc/yum.repos.d/CentOS-*

sed -i 's|#baseurl=http://mirror.centos.org|baseurl=http://vault.centos.org|g' /etc/yum.repos.d/CentOS-*
wget -O /etc/yum.repos.d/CentOS-Base.repo https://mirrors.aliyun.com/repo/Centos-vault-8.5.2111.repo

yum clean all
yum makecache
yum update

yum install -y yum-utils device-mapper-persistent-data lvm2
sudo yum-config-manager --add-repo http://mirrors.aliyun.com/docker-ce/linux/centos/docker-ce.repo
yum list docker-ce --showduplicates | sort -r
yum -y install docker-ce.x86_64  --allowerasing
sudo yum install docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
sudo systemctl enable docker --now
docker ps

systemctl start docker
systemctl enable docker
systemctl status docker
```

### Ubuntu

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

sudo mkdir /etc/docker
sudo touch /etc/docker/daemon.json
sudo chmod 777 /etc/docker/daemon.json
sudo vim /etc/docker/daemon.json
```

**重启docker**

```shell
sudo systemctl restart docker.socket
```

### 安装 Docker-Compose

```bash
sudo yum install docker-ce docker-ce-cli containerd.io

sudo yum install curl
curl -L https://get.daocloud.io/docker/compose/releases/download/1.25.4/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose

sudo chmod +x /usr/local/bin/docker-compose

sudo docker-compose --version
```

