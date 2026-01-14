## 使用 `SDKMAN`

目前`SpringFramework`使用的是`gradle 7.2`如果使用别的会有兼容问题，如果下载`SpringFramwork`配置`Gradle`还要安装`JDK25`。
`SpringSecutiy`使用的是`Gradle 8.5`

```bash
# 安装 SDKMAN
curl -s "https://get.sdkman.io" | bash

# 重新打开终端或运行
source "$HOME/.sdkman/bin/sdkman-init.sh"

# 安装最新版 Gradle
sdk install gradle

# 或安装特定版本
sdk install gradle 8.5

# 卸载 gradle
sdk uninstall --force gradle 8.5
```

## 配置国内镜像

在`~/.gradle/init.d/init.gradle`创建或者编辑`init.gradle`，填写以下内容。

```groovy
allprojects {
    repositories {
        // 1. 优先使用本地 Maven 仓库
        mavenLocal()

        // 2. 阿里云镜像仓库
        maven { url "https://maven.aliyun.com/repository/central" }
        maven { url "https://maven.aliyun.com/repository/public" }
        maven { url "https://maven.aliyun.com/repository/gradle-plugin" }
        maven { url "https://maven.aliyun.com/repository/apache-snapshots" }

        mavenCentral()
        google()
    }
}

buildscript {
    repositories {
        mavenLocal()
        maven { url "https://maven.aliyun.com/repository/central" }
        maven { url "https://maven.aliyun.com/repository/public" }
        maven { url "https://maven.aliyun.com/repository/gradle-plugin" }
        maven { url "https://maven.aliyun.com/repository/apache-snapshots" }
        maven { url "http://mirrors.cloud.tencent.com/nexus/repository/maven-public" }
    }
}
```

