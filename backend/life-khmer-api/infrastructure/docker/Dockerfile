FROM eclipse-temurin:21-jre
WORKDIR /app
COPY target/life-khmer-api-*.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
