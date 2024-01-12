FROM openjdk:17-jdk

WORKDIR /app
COPY target/ProfileLoader.jar /app/ProfileLoader.jar

EXPOSE 8081
CMD ["java", "-jar",  "/app/ProfileLoader.jar"]