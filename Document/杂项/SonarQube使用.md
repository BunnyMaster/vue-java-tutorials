## 环境搭建

为了理好的集成，下面有Gitlab的SonarQube

PostgreSQL：

- 为了和SonarQube结合

Gtlab说明：

- `external_url`要改成你的服务地址
- `initial_root_password`是设置的密码

GitRunner也是使用Docker方式集成的

SonarQube说明：

- `SONAR_JDBC_URL`：PGSQl地址
- `SONAR_JDBC_USERNAME`：用户名
- `SONAR_JDBC_PASSWORD`：密码

```yaml
# 定义整个开发栈的名称
name: microservices-dev-stack

services:
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

  # ==================== GitLab ====================
  gitlab:
    container_name: gitlab
    image: gitlab/gitlab-ce:latest
    hostname: 'gitlab.local'
    environment:
      GITLAB_OMNIBUS_CONFIG: |
        external_url 'http://192.168.2.19:8929'
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

networks:
  microservices-net:
    name: microservices-dev-network
    driver: bridge
```

## 前置条件

首次访问 SonarQube 需要完成以下配置：

1. **访问地址**: http://192.168.2.19:9001
2. **默认账号**: admin/admin
3. **首次登录强制修改密码**: 建议改为 Dev1234!（与【环境搭建】一致）
4. **创建项目**: 
   - 手动创建项目与 CI 中的 projectKey 对应
   - 或配置为自动创建项目

如果要想使用有两种办法，一是使用密码方式，二是使用Token方式(推荐)。

Token生成方式如下

### 创建用户

![image-20251120155741122](./assets/image-20251120155741122.png)

### 生成TOKEN

#### 方式

> [!IMPORTANT]
>
> Token只会显示一次，记得复制保存
>
> ![image-20251120160105674](./assets/image-20251120160105674.png)

Token示例

```
squ_6bacbf1239560373ed1e3634e6631241c72e7e81
sqp_e9e1e0f74e251cabf5e6c47d36597f775cbbdbde
```

![image-20251120155817057](./assets/image-20251120155817057.png)

![image-20251120155853948](./assets/image-20251120155853948.png)

#### 测试

```sh
curl -u "your_token": "http://192.168.2.19:9001/api/authentication/validate"
curl -u "your_token": "http://192.168.2.19:9001/api/system/status"
```

## 使用示例

### Java推送方式

#### POM文件

在`pom.xml`中添加下面内容：

1. 设置
2. 添加`JaCoCo`

```xml
<properties>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <java.version>21</java.version>
  	<!-- 添加下面这个 -->
    <sonar.qualitygate.wait>true</sonar.qualitygate.wait>
    <jacoco.version>0.8.11</jacoco.version>
</properties>

    <build>
        <plugins>
            <!-- 添加 JaCoCo 插件 -->
            <plugin>
                <groupId>org.jacoco</groupId>
                <artifactId>jacoco-maven-plugin</artifactId>
                <version>${jacoco.version}</version>
                <executions>
                    <execution>
                        <id>prepare-agent</id>
                        <goals>
                            <goal>prepare-agent</goal>
                        </goals>
                    </execution>
                    <execution>
                        <id>report</id>
                        <phase>test</phase>
                        <goals>
                            <goal>report</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
            <!-- 修复 surefire 插件配置 -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>${maven-surefire-plugin.version}</version>
                <configuration>
                    <!-- 与 JaCoCo 集成 -->
                    <argLine>@{argLine} -Dfile.encoding=UTF-8 -XX:+EnableDynamicAgentLoading</argLine>
                    <!-- 取消跳过测试 -->
                    <skip>false</skip>
                    <encoding>UTF-8</encoding>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <encoding>UTF-8</encoding>
                    <parameters>true</parameters>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

###  GitLab Runner 注册配置

在 gitlab-runner 容器中执行：

```bash
docker exec -it gitlab-runner gitlab-runner register
  --url "http://gitlab:8929" \
  --registration-token "GR1348941" \
  --executor "docker" \
  --docker-image "maven:3.9.9-eclipse-temurin-21" \
  --description "Docker Runner" \
  --tag-list "docker,java" \
  --run-untagged="true"
```

### 创建`.gitlab-ci.yml`

> [!NOTE]
>
> `.gitlab-ci.yml`不能有空格否则在构建时会报错
>
> - 确保 SonarQube 用户具有 "Execute Analysis" 权限
> - 检查项目权限设置

#### 不同项目的配置

```yaml
# 前端项目
- Dsonar.projectKey=frontend-project
- Dsonar.sources=src/
- Dsonar.javascript.lcov.reportPaths=coverage/lcov.info

# 后端项目  
- Dsonar.projectKey=backend-project
- Dsonar.sources=src/main/java
- Dsonar.tests=src/test/java
- Dsonar.java.binaries=target/classes
```

#### 添加阿里镜像

创建`ci-settings.xml`文件

```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <localRepository>${env.CI_PROJECT_DIR}/.m2/repository</localRepository>

    <mirrors>
        <mirror>
            <id>aliyunmaven</id>
            <mirrorOf>*</mirrorOf>
            <name>阿里云公共仓库</name>
            <url>https://maven.aliyun.com/repository/public</url>
        </mirror>
    </mirrors>

    <profiles>
        <profile>
            <id>default</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <repositories>
                <repository>
                    <id>central</id>
                    <url>https://maven.aliyun.com/repository/public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </repository>
            </repositories>
            <pluginRepositories>
                <pluginRepository>
                    <id>central</id>
                    <url>https://maven.aliyun.com/repository/public</url>
                    <releases>
                        <enabled>true</enabled>
                    </releases>
                    <snapshots>
                        <enabled>false</enabled>
                    </snapshots>
                </pluginRepository>
            </pluginRepositories>
        </profile>
    </profiles>
</settings>

```

#### 方式一 使用Token

在Gitlab中添加变量：

- 选项中`Visibility`勾选`Visible`
- `Flags`取消勾选`Protect variable`

| 变量名            | 变量值                                          | 备注         |
| -------------- | -------------------------------------------- | ---------- |
| SONAR_HOST_URL | http://192.168.2.19:9001                     | 为你的地址      |
| SONAR_TOKEN    | squ_6bacbf1239560373ed1e3634e6631241c72e7e81 | 为【前置条件】的操作 |

![企业微信截图_17636265328667](./assets/企业微信截图_17636265328667.png)

写入`.gitlab-ci.yml`：

- `url`SonarQube的地址

- `projectKey`项目的名称不要重复
- `projectName`显示在页面上的名称
- `login`在Gitlab中的配置
- `only`触发条件的分支

```yaml
image: maven:3.9.9-eclipse-temurin-21

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository -Dhttps.protocols=TLSv1.2"
  SONAR_USER_HOME: "${CI_PROJECT_DIR}/.sonar"
  GIT_DEPTH: "0"

stages:
  - sonarqube

cache:
  paths:
    - .m2/repository
    - .sonar/cache

sonarqube-analysis:
  stage: sonarqube
  script:
    - |
      mvn clean compile sonar:sonar \
        -s ci-settings.xml \
        -Dsonar.projectKey=gitlab-ci-testr \
        -Dsonar.projectName="gitlab-ci-test" \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.login=$SONAR_TOKEN \
        -Dsonar.projectVersion=$CI_COMMIT_SHORT_SHA \
        -Dsonar.java.source=21 \
        -Dsonar.java.target=21 \
        -Dsonar.sources=src/main/java \
        -Dsonar.java.binaries=target/classes \
        -DskipTests=true
  only:
    - merge_requests
    - dev
    - master
    - main
  artifacts:
    paths:
      - target/
    when: on_failure
    expire_in: 1 week
```

#### 方式二 使用密码

- `url`SonarQube的地址

- `projectKey`项目的名称不要重复
- `projectName`显示在页面上的名称
- `login`用户名
- `password`密码
- `only`触发条件的分支

```yaml
Gitlab:
glpat-TNp4ZTsz9pd3gRYUT0uurW86MQp1OjF3CA.01.0y05c3fnf


image: maven:3.9.9-eclipse-temurin-21

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository -Dhttps.protocols=TLSv1.2"
  SONAR_USER_HOME: "${CI_PROJECT_DIR}/.sonar"
  GIT_DEPTH: "0"

stages:
  - sonarqube

cache:
  paths:
    - .m2/repository
    - .sonar/cache

sonarqube-analysis:
  stage: sonarqube
  script:
    - |
      mvn clean compile sonar:sonar \
        -s ci-settings.xml \
        -Dsonar.projectKey=gitlab-ci-testr \
        -Dsonar.projectName="gitlab-ci-test" \
        -Dsonar.host.url=http://192.168.2.19:9001 \
        -Dsonar.login=bunny \
        -Dsonar.password=Dev1234! \
        -Dsonar.projectVersion=$CI_COMMIT_SHORT_SHA \
        -Dsonar.java.source=21 \
        -Dsonar.java.target=21 \
        -Dsonar.sources=src/main/java \
        -Dsonar.java.binaries=target/classes \
        -DskipTests=true
  only:
    - merge_requests
    - dev
    - master
    - main
  artifacts:
    paths:
      - target/
    when: on_failure
    expire_in: 1 week
```

