FROM openjdk:17-jdk

WORKDIR /app

COPY target/ProfileLoader.jar /app/ProfileLoader.jar
COPY src/main/resources/static/avatarTestIndividual.jpg /app/avatarTestIndividual.jpg

EXPOSE 8081
CMD ["java", "-jar",  "/app/ProfileLoader.jar"]

#Укажем имя образа: docker build -t profile-loader .