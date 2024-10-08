# 使用 OpenJDK 21 作为基础镜像
FROM adoptopenjdk/openjdk21:alpine

# 作者信息
LABEL maintainer="Zhuanz"

# 设置环境变量
ENV SPRING_OUTPUT_ANSI_ENABLED=ALWAYS \
    JAVA_OPTS="-Xms2048m -Xmx2048m -Dspring.profiles.active=prod -Dserver.port=8200"\
    PORT=8200
# spring-boot-admin.jar相对于Dockerfile的路径
COPY target/recovery-1.0-jar-with-dependencies.jar /shanyuzaihang-test.jar

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /shanyuzaihang-test.jar"]

#通过 EXPOSE 命令暴露外部访问容器端口
EXPOSE $PORT
