FROM mirror.gcr.io/library/eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY orng*service/target/*.jar service.jar

EXPOSE 8080

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
