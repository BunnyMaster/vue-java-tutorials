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

+ docker镜像：
    - [https://hub.docker.com/r/gitlab/gitlab-ee/tags?name=17.9.6](https://hub.docker.com/r/gitlab/gitlab-ee/tags?name=17.9.6)
    - [https://hub.docker.com/r/gitlab/gitlab-runner/tags?name=17.11.0](https://hub.docker.com/r/gitlab/gitlab-runner/tags?name=17.11.0)

## 安装Java和Maven
### Java安装
```bash
# 安装JDK21
wget https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.deb
sudo dpkg -i jdk-21_linux-x64_bin.deb
java --version
```

### Maven 3.8.8安装
maven的镜像

```xml
<mirror>
  <id>aliyun</id>
  <name>Aliyun Maven Mirror</name>
  <url>https://maven.aliyun.com/repository/public</url>
  <mirrorOf>central</mirrorOf>
</mirror>
```

安装

```bash
# 安装maven
wget https://archive.apache.org/dist/maven/maven-3/3.8.8/binaries/apache-maven-3.8.8-bin.tar.gz
sudo mkdir -p /opt/maven
sudo tar -xzf apache-maven-3.8.8-bin.tar.gz -C /opt/maven
sudo mv /opt/maven/apache-maven-3.8.8 /opt/maven/maven-3.8.8

# 修改镜像配置
cd /opt/maven/maven-3.8.8/conf
# 赋予权限修改
sudo chmod 666 settings.xml

# 编写配置
sudo vim /etc/profile

# 添加以下内容
# export PATH=$PATH:/opt/maven/maven-3.8.8/bin

# 刷新配置
source /etc/profile
mvn -V
```

## 安装Gitlab
### centos
```bash
# centos
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/yum/el7/gitlab-ce-17.1.1-ce.0.el7.x86_64.rpm

# 安装gitlab
rpm -ivh gitlab-ce-17.1.1-ce.0.el7.x86_64.rpm
```

### Ubuntu
```bash
# Ubuntu
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/ubuntu/pool/focal/main/g/gitlab-ce/gitlab-ce_18.0.0-ce.0_amd64.deb

# dpkg 
sudo dpkg -i gitlab-ce_18.0.0-ce.0_amd64.deb
```

### docker
```bash
docker run -d \
  --hostname localhost  \
  --publish 13001:443 --publish 3001:80 --publish 222:22 \
  --name gitlab \
  --restart always \
  --volume ~/docker/docker_data/srv/gitlab/config:/etc/gitlab \
  --volume ~/docker/docker_data/srv/gitlab/logs:/var/log/gitlab \
  --volume ~/docker/docker_data/srv/gitlab/data:/var/opt/gitlab \
  gitlab/gitlab-ee:17.9.6-ee.0
```

### k8s安装
```yaml
---
kind: Deployment
apiVersion: apps/v1
metadata:
  labels:
    k8s-app: gitlab
  name: gitlab
  namespace: devops
spec:
  replicas: 1
  revisionHistoryLimit: 10
  selector:
    matchLabels:
      k8s-app: gitlab
  template:
    metadata:
      labels:
        k8s-app: gitlab
      namespace: devops
      name: gitlab
    spec:
      containers:
        - name: gitlab
          image: gitlab/gitlab-ce:12.9.0-ce.0
          imagePullPolicy: IfNotPresent
          ports:
            - containerPort: 30088
              name: web
              protocol: TCP
            - containerPort: 22
              name: agent
              protocol: TCP
          resources:
            limits:
              cpu: 2000m
              memory: 8Gi
            requests:
              cpu: 500m
              memory: 512Mi
          livenessProbe:
            httpGet:
              path: /users/sign_in
              port: 30088
            initialDelaySeconds: 60
            timeoutSeconds: 5
            failureThreshold: 12
          readinessProbe:
            httpGet:
              path: /users/sign_in
              port: 30088
            initialDelaySeconds: 60
            timeoutSeconds: 5
            failureThreshold: 12
          volumeMounts:
            - name: gitlab-conf
              mountPath: /etc/gitlab
            - name: gitlab-log
              mountPath: /var/log/gitlab
            - name: gitlab-data
              mountPath: /var/opt/gitlab
          env:
            - name: gitlab_HOME
              value: /var/lib/gitlab
      volumes:
        - name: gitlab-conf
          hostPath: 
            path: /data/devops/gitlab/config
            type: Directory
        - name: gitlab-log
          hostPath: 
            path: /data/devops/gitlab/logs
            type: Directory
        - name: gitlab-data
          hostPath: 
            path: /data/devops/gitlab/data
            type: Directory
      serviceAccountName: gitlab
---
apiVersion: v1
kind: ServiceAccount
metadata:
  labels:
    k8s-app: gitlab
  name: gitlab
  namespace: devops
---
kind: Service
apiVersion: v1
metadata:
  labels:
    k8s-app: gitlab
  name: gitlab
  namespace: devops
spec:
  type: NodePort
  ports:
    - name: web
      port: 30088
      targetPort: 30088
      nodePort: 30088
    - name: slave
      port: 22
      targetPort: 22
      nodePort: 30022
  selector:
    k8s-app: gitlab
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1beta1
metadata:
 name: gitlab
 namespace: devops
rules:
 - apiGroups: [""]
   resources: ["pods"]
   verbs: ["create","delete","get","list","patch","update","watch"]
 - apiGroups: [""]
   resources: ["pods/exec"]
   verbs: ["create","delete","get","list","patch","update","watch"]
 - apiGroups: [""]
   resources: ["pods/log"]
   verbs: ["get","list","watch"]
 - apiGroups: [""]
   resources: ["secrets"]
   verbs: ["get"]
---
apiVersion: rbac.authorization.k8s.io/v1beta1
kind: RoleBinding
metadata:
 name: gitlab
 namespace: devops
roleRef:
 apiGroup: rbac.authorization.k8s.io
 kind: Role
 name: gitlab
subjects:
 - kind: ServiceAccount
   name: gitlab
   namespace: devops
```

### 编辑配置
```bash
# 编辑站点
sudo vim /etc/gitlab/gitlab.rb
```

更改配置

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745074398190-77423fbf-9c28-4f26-a9bd-9d59a602557e.png)

```ruby
external_url 'http://192.168.95.132:3001'
```

服务控制

```bash
# 服务控制
sudo gitlab-ctl start
sudo gitlab-ctl status
sudo gitlab-ctl stop

# 应用配置 
sudo gitlab-ctl reconfigure

# 重启
sudo gitlab-ctl restart
```

#### 初始密码
账号是root，密码输入你看到的密码

```bash
# 24 小时后自动删除
sudo cat /etc/gitlab/initial_root_password
```

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745062434272-f4801a2a-2ca9-41c7-8ba5-972cda4899e0.png)



## 安装GitLab Runner
### 方式一 （很慢）
```bash
# -----------------------------------Centos----------------------------
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.rpm.sh > gitlab-runner.sh
chmod +x gitlab-runner.sh
sudo ./gitlab-runner.sh

sudo yum update
sudo yum install gitlab-runner
# 安装指定版本
# sudo yum install gitlab-runner=10.0.0

# -----------------------------------Ubuntu----------------------------
sudo apt-get update
sudo apt-get install gitlab-runner

# 安装指定版本
# sudo apt-get install gitlab-runner=10.0.0
curl -L https://packages.gitlab.com/install/repositories/runner/gitlab-runner/script.deb.sh > gitlab-runner.sh
chmod +x gitlab-runner.sh
sudo ./gitlab-runner.sh
```

### 方式二（推荐）
+ 两个版本需要对应上（测试Ubuntu是这样的）
+ 目前没有测试centos是否可以

```bash
# -----------------------------------Centos----------------------------
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/yum/el7/gitlab-runner-17.1.0-1.x86_64.rpm
rpm -ivh gitlab-runner-17.1.0-1.x86_64.rpm

# 启动gitlab-runner
systemctl start gitlab-runner
systemctl status gitlab-runner

# -----------------------------------Ubuntu----------------------------
# 需要 gitlab-runner-helper-images
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner-helper-images/gitlab-runner-helper-images_18.0.1-1_all.deb
sudo dpkg -i gitlab-runner-helper-images_18.0.1-1_all.deb

# 之后安装 gitlab-runner
wget https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner/gitlab-runner_18.0.1-1_amd64.deb
sudo dpkg -i gitlab-runner_18.0.1-1_amd64.deb
```

### 方式三docker
```bash
docker run -d \
  --hostname localhost  \
  --publish 13001:443 --publish 3001:80 --publish 222:22 \
  --name gitlab \
  --restart always \
  --volume ~/docker/docker_data/srv/gitlab/config:/etc/gitlab \
  --volume ~/docker/docker_data/srv/gitlab/logs:/var/log/gitlab \
  --volume ~/docker/docker_data/srv/gitlab/data:/var/opt/gitlab \
  gitlab/gitlab-ee:17.9.6-ee.0

# 安装runner
docker run -d \
  --name gitlab-runner \
  --restart always \
  -v ~/docker/docker_data/srv/gitlab-runner/config:/etc/gitlab-runner \
  -v /var/run/docker.sock:/var/run/docker.sock \
  gitlab/gitlab-runner:v17.11.0
```

### 配置Gitlab-Runner用户
> [!NOTE]
>
> 如果有需要清理缓存：`sudo rm -rf /opt/maven/maven-3.8.8/conf/builds/**`**
>
> gitlab-ce：[https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/ubuntu/pool/focal/main/g/gitlab-ce/](https://mirrors.tuna.tsinghua.edu.cn/gitlab-ce/ubuntu/pool/focal/main/g/gitlab-ce/)
>
> gitlab-runner-helper-images：[https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner-helper-images/](https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner-helper-images/)
>
> gitlab-runner：[https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner/](https://mirrors.tuna.tsinghua.edu.cn/gitlab-runner/ubuntu/pool/focal/main/g/gitlab-runner/)
>

```bash
sudo gitlab-runner uninstall
sudo gitlab-runner install --working-directory /home/gitlab-runner --user root
sudo systemctl restart gitlab-runner
```

### 检查 GitLab Runner 配置
```bash
sudo vim /etc/gitlab-runner/config.toml
```

修改文件

```bash
[[runners]]
  name = "my-runner"
  executor = "shell"
  shell = "bash"
  user = "gitlab-runner"  # 确保用户有权限
  working_directory = "/home/gitlab-runner"
```

### 检查 Maven 安装目录权限
```bash
sudo chmod 777 -R /opt/maven/maven-3.8.8
sudo chmod 777 -R /opt/maven/maven-3.8.8/
sudo chown -R gitlab-runner:gitlab-runner /opt/maven/maven-3.8.8/
```

# 使用GitLab Runner
需要卸载默认用户

```bash
sudo gitlab-runner uninstall

# 清理下文件，看情况
sudo rm -rf /etc/gitlab-runner/
sudo rm -rf ~/.gitlab-runner/
sudo rm -f /usr/local/bin/gitlab-runner

mkdir -p ~/gitlab-runner
# 确保 bunny 用户有权限
chown bunny:bunny ~/gitlab-runner  
# 添加你的用户
sudo gitlab-runner install \
  --working-directory /home/bunny/gitlab-runner \
  --user bunny

# 重启gitlab-runner
sudo systemctl restart gitlab-runner.service
```

需要有权限的账号，在上面说的`root`，每个版本不一样，但都在`Runners`中获取`token`。

为下面做注册使用

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745072450701-0ce866b2-8da3-415b-8bb3-a2715215eb5d.png)

## 配置用户
![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745075472617-70731ef3-10df-4909-89c0-303d653dd0ec.png)

设置管理员，否则没有权限创建`Runner`，当然也可以用`root`用户创建。

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745075514454-a5b7c047-0955-4f3c-9d2b-e248ffa9bb83.png)

## 注册Runner
```bash
sudo gitlab-runner register
```

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745073357479-c3fe55e6-10f3-4412-bc7b-805daaee10b0.png)

预览示例

```bash
bunny@bunny:~$ sudo gitlab-runner register
Runtime platform                                    arch=amd64 os=linux pid=107000 revision=0f67ff19 version=17.11.0
Running in system-mode.

Enter the GitLab instance URL (for example, https://gitlab.com/):
http://192.168.95.132:3001/
Enter the registration token:
QNf9PQ41CPSx9839xEsg
Enter a description for the runner:
[bunny]: build-test
Enter tags for the runner (comma-separated):
build
Enter optional maintenance note for the runner:
build_demo_test
WARNING: Support for registration tokens and runner parameters in the 'register' command has been deprecated in GitLab Runner 15.6 and will be replaced with support for authentication tokens. For more information, see https://docs.gitlab.com/ci/runners/new_creation_workflow/
Registering runner... succeeded                     runner=QNf9PQ41
Enter an executor: kubernetes, instance, shell, virtualbox, docker, docker+machine, docker-autoscaler, custom, ssh, parallels, docker-windows:
shell
Runner registered successfully. Feel free to start it, but if it's running already the config should be automatically reloaded!

Configuration (with the authentication token) was saved in "/etc/gitlab-runner/config.toml"

```

## 非交互式创建
```bash
gitlab-runner register \
  --non-interactive \
  --executor "shell" \
  --url "http://localhost:3001/" \
  --registration-token "JRzzw2jlJi6aBjwvkxAv" \
  --description"devops-runner" \
  --tag-list "build,deploy" \
  --run-untagged="true" \
  --locked="false" \
  --access-level="not_protected"
```

### 示例
```bash
gitlab-runner register \
  --non-interactive \
  --executor "shell" \
  --url "http://192.168.95.132:3001/" \
  --registration-token "QNf9PQ41CPSx9839xEsg" \
  --description "devops-runner" \
  --tag-list "build,deploy" \
  --run-untagged="true" \
  --locked="false" \
  --access-level="not_protected"
```

+ 运行没有标记的`Runners`
+ `--registration-token`：你的token
+ 标签内容就是 一开始在上面手动输入的内容

![](https://cdn.nlark.com/yuque/0/2025/png/42943943/1745074979841-1ad2a3a7-c862-4415-88a3-d55a55e9a246.png)



