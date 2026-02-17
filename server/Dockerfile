FROM eclipse-temurin:21-jre

WORKDIR /app
COPY server/target/*.jar app.jar

EXPOSE 9090
ENTRYPOINT ["java","-jar","/app/app.jar"]