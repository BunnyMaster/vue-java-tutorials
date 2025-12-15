## Maven编译乱码

升级到 idea 2025.2.2 后发现之前的 maven 工程打包出现乱码, 这里记录下如何配置编码

### 1.先确保工程编码是UTF-8

打开 idea, 点击 File -> Settings 搜索 encoding, 将搜索结果出现的位置 全部设置为 UTF-8

![image-20251118151959620](./assets/image-20251118151959620.png)

### 2.设置 maven 编译参数

打开 idea, 点击 File -> Settings -> Build,Execution,Deployment -> Build Tools -> Maven -> Runner
(也可以在操作系统的环境变量里设置)

```properties
Name=MAVEN_OPTS
Value=-Dsun.stdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -Dconsole.encoding=UTF-8 -Dfile.encoding=UTF-8
```

![image-20251118151935360](./assets/image-20251118151935360.png)
