FROM openjdk:17-jdk

## Установка зависимостей
#RUN apt-get update \
#    && apt-get install -y openjdk:17-jdk


WORKDIR /app
COPY target/ProfileLoader.jar /app/ProfileLoader.jar

#ENV PGUSER=kata
#ENV PGHOST=localhost
#ENV PGDATABASE=profile-loader
#ENV PGPASSWORD=kata
#ENV PGPORT=5433
#

## Настройка переменных окружения для подключения к Kafka и Zookeeper
#ENV KAFKA_BOOTSTRAP_SERVERS=localhost:9092
#ENV KAFKA_GROUP_ID=profile-loader-group
#ENV KAFKA_TOPIC=profiles
#
#ENV ZOOKEEPER_CONNECT=localhost:2181

EXPOSE 8081
CMD ["java", "-jar",  "/app/ProfileLoader.jar"]