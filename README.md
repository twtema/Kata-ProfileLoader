# 🚀 МКС ProfileLoader
## 📌 Введение
МКС ProfileLoader - это высокопроизводительная система, спроектированная с учетом лучших практик, чтобы обеспечивать надежное и быстрое чтение и запись данных в базу данных.

## 🌟 Основные характеристики
**Производительность:** Эффективные алгоритмы чтения и записи обеспечивают высокую производительность при работе с большими объемами данных.

**Docker-интеграция:** Встроенная поддержка Docker с помощью docker-compose.yml позволяет легко и быстро развертывать необходимые сервисы, такие как PostgreSql и Kafka.

**Работа с сущностями:** В системе хранятся разнообразные сущности, предоставляя гибкость и масштабируемость для будущих расширений.

**MapStruct:** Интеграция с MapStruct обеспечивает быструю и надежную конвертацию данных между DTO и сущностями.

**Liquibase:** Система управления версиями базы данных с помощью Liquibase гарантирует стабильность и последовательность миграций.

##  🚀 Начало работы
Запуск с помощью Docker
Для запуска PostgreSql и Kafka:

<pre>
docker-compose up -d
</pre>

Это автоматически развернет необходимые сервисы и подготовит их к работе.

После чего можно запустить микросервис

Для подключения к сервису pgAdmin необходимо перейти по ссылке http://localhost:5050 
логин: kata_user@gmail.com
пароль: kata_password

## 📚 Документация
Подробная документация по API доступна через Swagger. Просто перейдите по следующей [ссылке](http://localhost:8081/swagger-ui/index.html) для доступа к интерактивной документации.

Работа с базой данных
Ченжсеты для Liquibase находятся по пути:

<pre>
\src\main\resources\db\changelog\changeset
</pre>

Следуйте инструкциям в ченжсетах для корректного применения миграций.