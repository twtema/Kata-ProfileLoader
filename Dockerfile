FROM openjdk:17

WORKDIR /app
COPY target/ProfileLoader.jar /app/ProfileLoader.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "ProfileLoader.jar"]