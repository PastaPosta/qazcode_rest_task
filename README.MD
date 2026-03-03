
Order Management System (REST API)

Java 17

Spring Boot 3 (Web, Data JPA, Validation)

PostgreSQL

Flyway

Docker & Docker Compose

Swagger

JUnit 5 & Mockito

🛠 Запуск приложения

Для запуска необходим установленный Docker и Docker Compose.

Клонируйте репозиторий:

git clone github.com/PastaPosta/qazcode_rest_task

cd qazcode_rest_task

Соберите и запустите контейнеры:

docker-compose up --build
(Процесс №16 может проходить долго, это скачивается gradle)
После запуска приложение будет доступно по адресу: http://localhost:8080

📖 Документация API (Swagger)

После запуска приложения интерактивная документация доступна по адресу:
http://localhost:8080/swagger-ui/index.html

Здесь можно просмотреть все доступные эндпоинты и отправить тестовые запросы.

🧪 Тестирование
1. Unit-тесты

Запуск модульных тестов сервисного слоя:

./gradlew test
2. Postman коллекция

В корне проекта находится файл Postman Collection(ai).json для тестирования через Postman

В коллекции настроены тесты для всех CRUD операций и бизнес-логики (оплата, отмена, валидация).

Тестовые данные (ID 1, 2 и т.д.) уже в базе (Flyway).

🗄 База данных

Параметры подключения (настроены в docker-compose.yml):

DB Name: orders

User: admin

Password: admin

Port: 5432

Схема базы данных автоматически создается Flyway миграциями при первом запуске.
