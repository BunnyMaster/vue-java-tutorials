## 配置说明

以`sonarqube`质量分析为说明，这里的配置跳过了测试方法

### gitlab-ci.yaml配置

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

sonarqube-analysis-dev:
  stage: sonarqube
  script:
    # 验证 Maven 是否可用
    - mvn --version
    - |
      mvn clean compile sonar:sonar \
        -s ci-settings.xml \
        -Dsonar.projectKey=generator-code-server-dev \
        -Dsonar.projectName="Generator Code Serve Dev" \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.login=$SONAR_TOKEN \
        -Dsonar.projectVersion=$CI_COMMIT_SHORT_SHA \
        -Dsonar.java.source=17 \
        -Dsonar.java.target=17 \
        -Dsonar.sources=src/main/java \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
        -Dsonar.coverage.exclusions=**/annotation/**,**/config/**,**/model/**,**/exception/**,**/handler/**,**/core/**,**/freemarker/*Freemarker.java,**/*Application.java \
        -DskipTests=true
  only:
    - merge-requests
    - dev
  artifacts:
    paths:
      - target/
    expire_in: 1 hour
  tags:
    - microservices
    - docker

sonarqube-analysis:
  stage: sonarqube
  script:
    # 验证 Maven 是否可用
    - mvn --version
    - |
      mvn clean compile sonar:sonar \
        -s ci-settings.xml \
        -Dsonar.projectKey=generator-code-server \
        -Dsonar.projectName="Generator Code Serve" \
        -Dsonar.host.url=$SONAR_HOST_URL \
        -Dsonar.login=$SONAR_TOKEN \
        -Dsonar.projectVersion=$CI_COMMIT_SHORT_SHA \
        -Dsonar.java.source=17 \
        -Dsonar.java.target=17 \
        -Dsonar.sources=src/main/java \
        -Dsonar.java.binaries=target/classes \
        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
        -Dsonar.coverage.exclusions=**/annotation/**,**/config/**,**/model/**,**/exception/**,**/handler/**,**/core/**,**/freemarker/*Freemarker.java,**/*Application.java \
        -DskipTests=true
  only:
    - merge-requests
    - master
  artifacts:
    paths:
      - target/
    expire_in: 1 hour
  tags:
    - microservices
    - docker

# 本地测试使用：mvn clean compile sonar:sonar "-Dsonar.host.url=http://192.168.3.19:9001" "-Dsonar.login=squ_6bacbf1239560373ed1e3634e6631241c72e7e81" -DskipTests
```

### ci-settings.xml配置

由于是国内网站，配置阿里镜像可以用下面的方式。在上面的`gitlab-ci`中已经有如下配置，所以要`ci-settings.xml`

```yaml
  mvn clean compile sonar:sonar \
    -s ci-settings.xml \
```

下面是示例内容

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

### pom.xml配置

在`pom.xml`中添加以下内容

```xml
<properties>
    <sonar.qualitygate.wait>true</sonar.qualitygate.wait>
    <argLine>-Dfile.encoding=UTF-8</argLine>
    <jacoco.version>0.8.11</jacoco.version>
</properties>
```

build内容

```xml
<build>
    <plugins>
        <!-- JaCoCo 配置 -->
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
                    <phase>prepare-package</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
                <execution>
                    <id>post-unit-test</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                    <configuration>
                        <dataFile>target/jacoco.exec</dataFile>
                        <outputDirectory>target/jacoco-ut</outputDirectory>
                    </configuration>
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

## 常见问题

### 1. GitLab 启动错误
如果遇到配置语法错误，检查：
- YAML 格式是否正确缩进
- `GITLAB_OMNIBUS_CONFIG` 的格式
- 端口是否被占用

### 2. 权限问题
```bash
# 如果遇到权限问题，设置正确的目录权限
sudo chown -R 1000:1000 ~/docker-compose/develop/gitlab/
