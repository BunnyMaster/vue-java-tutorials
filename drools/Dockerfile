FROM openjdk:24-ea-17-jdk-oraclelinux9
LABEL maintainer="server"

#系统编码
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

# 设置时区，构建镜像时执行的命令
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

# 设定工作目录
WORKDIR /home/server

# 复制jar包
COPY target/*.jar /home/server/app.jar

# 启动容器时的进程
ENTRYPOINT ["java","-jar","/home/server/app.jar"]

#暴露 8080 端口
EXPOSE 8080
EXPOSE 7070

# 生产环境
# mvn clean package -Pprod -DskipTests

# 测试环境
# mvn clean package -Ptest -DskipTests

