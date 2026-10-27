# 🎓 Student Management — Web Lab 3

![Java](https://img.shields.io/badge/Java-21%2B-orange?logo=openjdk)
![Jakarta EE](https://img.shields.io/badge/Jakarta%20EE-11-blue)
![GlassFish](https://img.shields.io/badge/GlassFish-8.0.5-1f6feb)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-4169E1?logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?logo=docker&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-WAR-C71A36?logo=apachemaven&logoColor=white)
![OpenAPI](https://img.shields.io/badge/OpenAPI-3.0-6BA539?logo=openapiinitiative&logoColor=white)
![JWT](https://img.shields.io/badge/Auth-JWT-black)

Полноценное клиент-серверное приложение для управления студентами, разработанное в рамках лабораторной работы №3 по веб-программированию в ИТМО.

Проект включает:

- REST API на Jakarta EE;
- PostgreSQL;
- JPA / EclipseLink;
- JWT-аутентификацию;
- access и refresh tokens;
- роли `USER` и `ADMIN`;
- серверную валидацию;
- фильтрацию, поиск, сортировку и пагинацию;
- HTML/CSS/JavaScript frontend без фреймворков;
- Swagger / OpenAPI;
- коллекцию Bruno;
- централизованную обработку ошибок;
- серверное логирование;
- Docker Compose для PostgreSQL.

---

## 📌 Содержание

- [Возможности](#-возможности)
- [Архитектура](#-архитектура)
- [Технологии](#-технологии)
- [Структура проекта](#-структура-проекта)
- [Модель данных](#-модель-данных)
- [Аутентификация](#-аутентификация)
- [Роли и права доступа](#-роли-и-права-доступа)
- [REST API](#-rest-api)
- [Фильтрация и сортировка](#-фильтрация-и-сортировка)
- [Валидация](#-валидация)
- [Обработка ошибок](#-обработка-ошибок)
- [Frontend](#-frontend)
- [Swagger / OpenAPI](#-swagger--openapi)
- [Bruno](#-bruno)
- [Логирование](#-логирование)
- [Запуск проекта](#-запуск-проекта)
- [Настройка GlassFish](#-настройка-glassfish)
- [Конфигурация приложения](#-конфигурация-приложения)
- [Сборка и deployment](#-сборка-и-deployment)
- [Полезные команды](#-полезные-команды)
- [Типичные проблемы](#-типичные-проблемы)

---

# ✨ Возможности

## Управление студентами

Приложение поддерживает полный CRUD:

- создание студента;
- получение списка студентов;
- получение студента по ID;
- полное обновление через `PUT`;
- частичное обновление через `PATCH`;
- удаление.

Дополнительно реализованы:

- поиск по ФИО;
- фильтрация по группе;
- фильтрация по ИСУ ID;
- фильтрация по общежитию;
- фильтрация по комнате;
- фильтрация по дате заселения;
- фильтрация по признаку иностранного студента;
- поиск по заметкам;
- сортировка;
- пагинация.

---

## Пользователи

Реализованы:

- регистрация;
- вход по username или email;
- хранение паролей в виде BCrypt-хеша;
- выдача access token;
- выдача refresh token;
- обновление токенов;
- refresh token rotation;
- роли пользователей.

---

# 🏗 Архитектура

Приложение построено по слоистой архитектуре.

```text
Browser
   │
   │ HTTP / JSON
   ▼
Frontend
HTML + CSS + JavaScript
   │
   │ fetch()
   ▼
JAX-RS Resources
   │
   ▼
Services
   │
   ▼
Repositories
   │
   ▼
JPA / EntityManager
   │
   ▼
EclipseLink
   │
   ▼
GlassFish JDBC Resource
jdbc/StudentDS
   │
   ▼
StudentPool
   │
   ▼
PostgreSQL JDBC Driver
   │
   ▼
PostgreSQL 16
Docker
```

### Основные слои

**Resource**

Принимает HTTP-запросы и формирует HTTP-ответы.

Примеры:

```text
StudentResource
AuthResource
```

**Service**

Содержит бизнес-логику.

```text
StudentService
UserService
AuthService
JwtService
PasswordService
```

**Repository**

Работает с базой данных через `EntityManager`.

```text
StudentRepository
UserRepository
RefreshTokenRepository
```

**Entity**

JPA-сущности.

```text
Student
User
RefreshToken
```

**DTO**

Используются для обмена данными между клиентом и API.

Это позволяет не передавать JPA Entity напрямую наружу.

---

# 🛠 Технологии

| Компонент | Технология |
|---|---|
| Язык | Java 21+ |
| Application Server | GlassFish 8 |
| Platform | Jakarta EE 11 |
| REST API | Jakarta REST / JAX-RS |
| DI | CDI |
| Persistence | Jakarta Persistence |
| ORM | EclipseLink |
| Database | PostgreSQL 16 |
| DB container | Docker Compose |
| Authentication | JWT |
| Password hashing | BCrypt |
| JWT library | JJWT |
| JSON | Jackson |
| API documentation | Swagger Core / OpenAPI 3 |
| API UI | Swagger UI |
| API testing | Bruno |
| Build | Maven |
| Packaging | WAR |
| Frontend | HTML / CSS / Vanilla JavaScript |

---

# 📁 Структура проекта

```text
web_lab_3/
│
├── bruno/
│   └── Web Lab 3/
│       ├── Auth/
│       ├── Students/
│       └── Errors/
│
├── src/
│   └── main/
│       ├── java/
│       │   └── ru/itmo/weblab3/
│       │       │
│       │       ├── bootstrap/
│       │       │   └── AdminSeeder.java
│       │       │
│       │       ├── config/
│       │       │   ├── ConfigKeys.java
│       │       │   ├── ConfigProvider.java
│       │       │   ├── JacksonConfig.java
│       │       │   └── RestApplication.java
│       │       │
│       │       ├── dto/
│       │       │   ├── auth/
│       │       │   ├── ErrorResponse.java
│       │       │   ├── StudentCreateDto.java
│       │       │   ├── StudentPatchDto.java
│       │       │   ├── StudentResponseDto.java
│       │       │   └── StudentUpdateDto.java
│       │       │
│       │       ├── entity/
│       │       │   ├── Student.java
│       │       │   ├── User.java
│       │       │   ├── RefreshToken.java
│       │       │   └── Role.java
│       │       │
│       │       ├── exception/
│       │       │   └── mapper/
│       │       │
│       │       ├── repository/
│       │       │   ├── StudentRepository.java
│       │       │   ├── UserRepository.java
│       │       │   └── RefreshTokenRepository.java
│       │       │
│       │       ├── resource/
│       │       │   ├── AuthResource.java
│       │       │   └── StudentResource.java
│       │       │
│       │       ├── security/
│       │       │   ├── AuthorizationFilter.java
│       │       │   ├── JwtAuthenticationFilter.java
│       │       │   ├── JwtPayload.java
│       │       │   ├── JwtSecurityContext.java
│       │       │   └── Public.java
│       │       │
│       │       ├── service/
│       │       │   ├── AuthService.java
│       │       │   ├── JwtService.java
│       │       │   ├── PasswordService.java
│       │       │   ├── StudentService.java
│       │       │   └── UserService.java
│       │       │
│       │       └── util/
│       │           └── Logs.java
│       │
│       ├── resources/
│       │   └── META-INF/
│       │       └── persistence.xml
│       │
│       └── webapp/
│           ├── index.html
│           ├── login.html
│           ├── student-details.html
│           ├── student-form.html
│           ├── swagger.html
│           │
│           └── static/
│               ├── css/
│               │   └── style.css
│               └── js/
│                   ├── auth.js
│                   ├── db.js
│                   ├── details.js
│                   ├── form.js
│                   ├── login.js
│                   └── students.js
│
├── compose.yaml
├── pom.xml
└── README.md
```

---

# 👨‍🎓 Модель данных

## Student

Студент содержит следующие поля:

| Поле | Тип | Описание |
|---|---|---|
| `id` | Long | ID записи |
| `fullName` | String | ФИО |
| `isuId` | Integer | Уникальный ID ИСУ |
| `group` | String | Учебная группа |
| `dormitoryNumber` | Integer | Номер общежития |
| `roomNumber` | Integer | Номер комнаты |
| `moveInDate` | LocalDate | Дата заселения |
| `isForeign` | Boolean | Иностранный студент |
| `notes` | String | Заметки |

---

## User

```text
id
username
email
passwordHash
role
createdAt
```

Пароль в открытом виде в базе данных не хранится.

---

## RefreshToken

```text
id
tokenHash
user
expiresAt
revoked
```

Сам refresh token в базе не хранится.

Перед сохранением он хешируется через SHA-256.

---

# 🔐 Аутентификация

Используется JWT-аутентификация.

После успешного входа сервер возвращает:

```json
{
  "accessToken": "...",
  "refreshToken": "...",
  "expiresIn": 900
}
```

## Access token

Используется для доступа к API.

Передаётся в HTTP-заголовке:

```http
Authorization: Bearer <access-token>
```

JWT содержит:

```text
sub   — username
role  — USER или ADMIN
type  — access / refresh
iat   — время создания
exp   — время окончания
jti   — уникальный идентификатор
```

---

## Refresh token

Refresh token используется для получения новой пары токенов.

При refresh применяется **rotation**:

```text
старый refresh token
        │
        ▼
проверка
        │
        ▼
старый token помечается revoked
        │
        ▼
создаётся новый access token
        +
создаётся новый refresh token
```

Это не позволяет многократно использовать один и тот же refresh token.

---

# 👥 Роли и права доступа

Поддерживаются две роли:

```text
USER
ADMIN
```

| Действие | USER | ADMIN |
|---|:---:|:---:|
| Просмотр списка | ✅ | ✅ |
| Просмотр студента | ✅ | ✅ |
| Фильтрация | ✅ | ✅ |
| Сортировка | ✅ | ✅ |
| Пагинация | ✅ | ✅ |
| Создание | ❌ | ✅ |
| Изменение | ❌ | ✅ |
| Удаление | ❌ | ✅ |

Важно:

> Скрытие кнопок на frontend является только частью UI. Настоящая проверка прав выполняется на backend в `AuthorizationFilter`.

Поэтому USER не сможет создать студента даже если вручную отправит POST-запрос.

---

# 🌐 REST API

Базовый адрес:

```text
http://localhost:8080/web-lab-3/api
```

---

## Authentication API

### Регистрация

```http
POST /api/auth/register
```

Пример:

```json
{
  "username": "student",
  "email": "student@example.com",
  "password": "Password123"
}
```

Успешный ответ:

```http
201 Created
```

---

### Вход

```http
POST /api/auth/login
```

```json
{
  "usernameOrEmail": "student",
  "password": "Password123"
}
```

Можно использовать как username, так и email.

---

### Обновление токенов

```http
POST /api/auth/refresh
```

```json
{
  "refreshToken": "<refresh-token>"
}
```

---

# 🎓 Students API

## Получить студентов

```http
GET /api/students
```

Требуется:

```http
Authorization: Bearer <access-token>
```

---

## Получить студента

```http
GET /api/students/{id}
```

---

## Создать студента

Только `ADMIN`.

```http
POST /api/students
```

Пример:

```json
{
  "fullName": "Иванов Иван Иванович",
  "isuId": 505115,
  "group": "P3120",
  "dormitoryNumber": 8,
  "roomNumber": 412,
  "moveInDate": "2026-09-01",
  "isForeign": false,
  "notes": "Тестовый студент"
}
```

Успешный ответ:

```http
201 Created
```

---

## Полностью обновить студента

```http
PUT /api/students/{id}
```

`PUT` предполагает передачу полной модели.

---

## Частично обновить студента

```http
PATCH /api/students/{id}
```

Пример:

```json
{
  "roomNumber": 420,
  "notes": "Комната изменена"
}
```

Передаются только изменяемые поля.

---

## Удалить студента

```http
DELETE /api/students/{id}
```

Успешный ответ:

```http
204 No Content
```

---

# 🔎 Фильтрация и сортировка

Endpoint:

```http
GET /api/students
```

поддерживает query parameters.

## Фильтры

| Параметр | Тип | Поведение |
|---|---|---|
| `fullName` | String | поиск по части строки |
| `isuId` | Integer | точное совпадение |
| `group` | String | поиск по части строки |
| `dormitoryNumber` | Integer | точное совпадение |
| `roomNumber` | Integer | точное совпадение |
| `moveInDate` | String | дата `DD-MM-YYYY` |
| `isForeign` | Boolean | `true` / `false` |
| `notes` | String | поиск по части текста |

Пример:

```http
GET /api/students?group=P3120
```

Поиск по имени:

```http
GET /api/students?fullName=иван
```

---

## Сортировка

```text
sortBy
sortDirection
```

Поддерживаемые поля:

```text
id
fullName
isuId
group
dormitoryNumber
roomNumber
moveInDate
isForeign
notes
```

Направления:

```text
asc
desc
```

Пример:

```http
GET /api/students?sortBy=fullName&sortDirection=desc
```

---

## Пагинация

Используются параметры:

```text
page
size
```

Нумерация страниц начинается с `0`.

Пример:

```http
GET /api/students?page=0&size=10
```

Ограничение:

```text
1 <= size <= 100
```

---

# ✅ Валидация

Валидация выполняется на сервере через Jakarta Bean Validation.

## Student

### ФИО

Не может быть пустым.

```java
@NotBlank
```

---

### ИСУ ID

```text
100000 — 999999
```

ИСУ ID также должен быть уникальным.

---

### Группа

Формат:

```text
[A-Z][1-9][1-4][0-9]{2}
```

Пример:

```text
P3120
```

---

### Общежитие

```text
1 — 99
```

---

### Комната

```text
1 — 9999
```

---

### Дата заселения

Дата должна находиться в прошлом.

JSON:

```text
YYYY-MM-DD
```

Пример:

```text
2026-09-01
```

Фильтр API:

```text
DD-MM-YYYY
```

---

## Регистрация пользователя

### Username

```text
3 — 64 символа
```

---

### Email

Проверяется через:

```java
@Email
```

---

### Password

```text
8 — 72 символа
```

Пароль должен содержать:

- минимум одну букву;
- минимум одну цифру.

---

# ⚠️ Обработка ошибок

Приложение использует централизованные `ExceptionMapper`.

Пример ошибки:

```json
{
  "timestamp": "2026-10-27T09:25:06.157Z",
  "status": 401,
  "error": "Unauthorized",
  "message": "Missing or malformed Authorization header"
}
```

При ошибке валидации также возвращается объект `details`.

---

## HTTP statuses

| Код | Значение |
|---:|---|
| `200` | успешный запрос |
| `201` | объект создан |
| `204` | объект удалён |
| `400` | некорректные параметры запроса |
| `401` | отсутствует или недействителен JWT |
| `403` | недостаточно прав |
| `404` | объект не найден |
| `409` | конфликт уникальности |
| `422` | ошибка Bean Validation |

---

# 🖥 Frontend

Frontend написан без JavaScript-фреймворков.

Используются:

```text
HTML
CSS
Vanilla JavaScript
Fetch API
LocalStorage
```

Главная страница:

```text
http://localhost:8080/web-lab-3/
```

Страница входа:

```text
http://localhost:8080/web-lab-3/login.html
```

---

## Работа frontend с JWT

После login:

```text
access token
refresh token
```

сохраняются в браузере.

При запросах к API `auth.js` автоматически добавляет:

```http
Authorization: Bearer <access-token>
```

Если access token истёк:

```text
API возвращает 401
        │
        ▼
frontend отправляет refresh token
        │
        ▼
получает новую пару токенов
        │
        ▼
повторяет исходный запрос
```

Для предотвращения нескольких одновременных refresh-запросов используется общий `refreshPromise`.

---

# 📚 Swagger / OpenAPI

Проект автоматически генерирует OpenAPI 3 specification.

Swagger UI:

```text
http://localhost:8080/web-lab-3/swagger.html
```

OpenAPI JSON:

```text
http://localhost:8080/web-lab-3/api/openapi.json
```

OpenAPI YAML:

```text
http://localhost:8080/web-lab-3/api/openapi.yaml
```

Swagger UI позволяет:

- посмотреть все endpoint'ы;
- посмотреть DTO;
- увидеть Bean Validation constraints;
- выполнять HTTP-запросы;
- авторизоваться через JWT.

Для защищённых endpoint'ов используется:

```text
bearerAuth
```

После получения access token необходимо нажать:

```text
Authorize
```

и вставить JWT.

---

# 🧪 Bruno

В директории:

```text
bruno/Web Lab 3
```

находится коллекция запросов для проверки API.

Она включает сценарии:

### Authentication

- registration;
- USER login;
- ADMIN login;
- token refresh;
- запрос с новым access token.

### Students

- получение списка;
- получение по ID;
- создание;
- PUT;
- PATCH;
- DELETE;
- фильтрация;
- поиск;
- сортировка;
- пагинация.

### Errors

- запрос без JWT;
- USER пытается создать студента;
- несуществующий студент;
- duplicate ISU;
- validation error;
- wrong credentials.

---

# 📝 Логирование

Используется:

```java
java.util.logging.Logger
```

Логируются важные действия приложения.

Примеры:

```text
Login successful: user=admin, role=ADMIN
Refresh token rotated: user=admin
Student created: id=5, by=admin
Student updated (PATCH): id=5, by=admin
Student deleted: id=5, by=admin
```

Ошибки:

```text
Unauthorized (401): Invalid or expired token
Forbidden (403): ADMIN role required
Bad request (400): ...
Conflict (409): ...
Not found (404): ...
Validation failed (422): ...
```

Пароли и JWT-токены в лог не записываются.

Пользовательские значения перед логированием проходят через:

```java
Logs.clean()
```

чтобы пользователь не мог внедрить символы перевода строки и подделать структуру журнала.

GlassFish log:

```text
glassfish/domains/domain1/logs/server.log
```

---

# 🚀 Запуск проекта

## Требования

Для запуска понадобятся:

```text
Java 21+
Maven
Docker
Docker Compose
GlassFish 8
```

Проверка Java:

```bash
java -version
```

Проверка Maven:

```bash
mvn -version
```

Проверка Docker:

```bash
docker --version
docker compose version
```

---

# 🐘 PostgreSQL

PostgreSQL запускается через Docker Compose.

```bash
docker compose up -d
```

Проверить:

```bash
docker ps
```

Должен быть контейнер:

```text
web-lab-3-postgres
```

Параметры базы из `compose.yaml`:

```text
database: student_db
user:     student
port:     5432
```

---

## Проверка PostgreSQL

```bash
docker exec -it web-lab-3-postgres \
  psql -U student -d student_db
```

---

## Важно про volume

PostgreSQL использует:

```text
postgres_data
```

Поэтому обычный:

```bash
docker compose down
```

не удаляет данные.

Не используйте:

```bash
docker compose down -v
```

если не хотите удалить базу.

---

# 🐟 Настройка GlassFish

## JDBC Driver

PostgreSQL JDBC driver должен быть доступен GlassFish.

Пример:

```powershell
asadmin add-library --type common `
  "C:\Users\<USER>\.m2\repository\org\postgresql\postgresql\42.7.14\postgresql-42.7.14.jar"
```

После добавления библиотеки GlassFish необходимо перезапустить.

---

## JDBC Connection Pool

Создание пула:

```powershell
asadmin create-jdbc-connection-pool `
  --datasourceclassname org.postgresql.ds.PGSimpleDataSource `
  --restype javax.sql.DataSource `
  --property "user=student:password=student_pass:databaseName=student_db:serverName=localhost:portNumber=5432" `
  StudentPool
```

Проверка:

```powershell
asadmin ping-connection-pool StudentPool
```

Ожидаемый результат:

```text
Command ping-connection-pool executed successfully.
```

---

## JDBC Resource

```powershell
asadmin create-jdbc-resource `
  --connectionpoolid StudentPool `
  jdbc/StudentDS
```

Этот JNDI resource используется в:

```text
persistence.xml
```

```xml
<jta-data-source>jdbc/StudentDS</jta-data-source>
```

---

# ⚙️ Конфигурация приложения

Приложение читает параметры сначала из environment variables, а затем из Java system properties.

```text
Environment variable
        ↓
System property
        ↓
ошибка, если обязательный параметр отсутствует
```

---

## JWT

Обязательные параметры:

```text
JWT_SECRET
JWT_ACCESS_TTL_SECONDS
JWT_REFRESH_TTL_SECONDS
```

`JWT_SECRET` должен быть минимум 32 байта.

Пример:

```text
JWT_SECRET=<long-random-secret-at-least-32-bytes>
JWT_ACCESS_TTL_SECONDS=900
JWT_REFRESH_TTL_SECONDS=604800
```

Это соответствует:

```text
access token  = 15 минут
refresh token = 7 дней
```

---

## ADMIN

Опционально:

```text
ADMIN_USERNAME
ADMIN_PASSWORD
ADMIN_EMAIL
```

Если все три значения присутствуют, при запуске приложения:

```text
AdminSeeder
```

проверяет наличие администратора и создаёт его при необходимости.

Если значения не заданы:

```text
Admin seeding skipped: ADMIN_* not set
```

---

# 🔧 Настройка параметров GlassFish

Пример через JVM options / system properties:

```powershell
asadmin create-system-properties `
  JWT_SECRET="<secret>":JWT_ACCESS_TTL_SECONDS="900":JWT_REFRESH_TTL_SECONDS="604800"
```

Для admin:

```powershell
asadmin create-system-properties `
  ADMIN_USERNAME="admin":ADMIN_PASSWORD="<password>":ADMIN_EMAIL="admin@example.com"
```

После изменения параметров может потребоваться restart GlassFish.

> Не коммитьте реальные пароли и JWT secret в Git.

---

# 🗃 Persistence

Persistence unit:

```text
studentPU
```

Тип:

```text
JTA
```

DataSource:

```text
jdbc/StudentDS
```

Сущности:

```text
Student
User
RefreshToken
```

В текущей конфигурации:

```xml
<property
    name="jakarta.persistence.schema-generation.database.action"
    value="none"
/>
```

Это сделано специально, чтобы EclipseLink не пересоздавал таблицы при каждом redeploy.

---

## Первый запуск на пустой БД

Если база полностью новая и таблиц ещё нет, для первоначального создания схемы можно временно заменить:

```xml
value="none"
```

на:

```xml
value="create"
```

выполнить один deploy, а затем вернуть:

```xml
value="none"
```

Это предотвращает потерю данных на последующих redeploy.

---

# 📦 Сборка

В корне проекта:

```bash
mvn clean package
```

При успешной сборке:

```text
BUILD SUCCESS
```

WAR будет создан:

```text
target/web-lab-3-1.0-SNAPSHOT.war
```

---

# 🚢 Deployment

Пример:

```powershell
asadmin deploy `
  --name web-lab-3-1.0-SNAPSHOT `
  --contextroot web-lab-3 `
  target/web-lab-3-1.0-SNAPSHOT.war
```

Повторный deploy:

```powershell
asadmin redeploy `
  --name web-lab-3-1.0-SNAPSHOT `
  --contextroot web-lab-3 `
  target/web-lab-3-1.0-SNAPSHOT.war
```

---

## Проверка deployment

```powershell
asadmin list-applications
```

После успешного запуска приложение доступно по адресу:

```text
http://localhost:8080/web-lab-3/
```

---

# 🔄 Полный сценарий запуска

```text
1. Запустить PostgreSQL
        ↓
2. Проверить StudentPool
        ↓
3. Проверить JWT configuration
        ↓
4. mvn clean package
        ↓
5. deploy / redeploy WAR
        ↓
6. открыть frontend
        ↓
7. войти в систему
```

Команды:

```bash
docker compose up -d
```

```powershell
asadmin ping-connection-pool StudentPool
```

```bash
mvn clean package
```

```powershell
asadmin redeploy `
  --name web-lab-3-1.0-SNAPSHOT `
  --contextroot web-lab-3 `
  target/web-lab-3-1.0-SNAPSHOT.war
```

---

# 🔗 Полезные адреса

| Назначение | URL |
|---|---|
| Frontend | `http://localhost:8080/web-lab-3/` |
| Login | `http://localhost:8080/web-lab-3/login.html` |
| REST API | `http://localhost:8080/web-lab-3/api` |
| Swagger UI | `http://localhost:8080/web-lab-3/swagger.html` |
| OpenAPI JSON | `http://localhost:8080/web-lab-3/api/openapi.json` |
| OpenAPI YAML | `http://localhost:8080/web-lab-3/api/openapi.yaml` |

---

# 🧰 Полезные команды

## Docker

```bash
docker ps
```

```bash
docker compose up -d
```

```bash
docker compose down
```

Логи PostgreSQL:

```bash
docker logs web-lab-3-postgres --tail 100
```

---

## GlassFish

Проверить datasource:

```powershell
asadmin ping-connection-pool StudentPool
```

Приложения:

```powershell
asadmin list-applications
```

Redeploy:

```powershell
asadmin redeploy `
  --name web-lab-3-1.0-SNAPSHOT `
  --contextroot web-lab-3 `
  target/web-lab-3-1.0-SNAPSHOT.war
```

---

## Logs

Git Bash:

```bash
tail -n 100 \
/x/Java/glassfish8/glassfish/domains/domain1/logs/server.log
```

Только основные application logs:

```bash
grep -E \
"Login successful|Login failed|User registered|Student created|Student updated|Student deleted|Unauthorized|Forbidden|Bad request|Conflict|Not found|Validation failed" \
/x/Java/glassfish8/glassfish/domains/domain1/logs/server.log
```

---

# 🧯 Типичные проблемы

## `401 Unauthorized`

Пример:

```json
{
  "error": "Unauthorized",
  "message": "Missing or malformed Authorization header",
  "status": 401
}
```

Причина:

```text
Authorization header отсутствует
```

Нужно:

```http
Authorization: Bearer <access-token>
```

---

## `403 Forbidden`

Пользователь авторизован, но у него недостаточно прав.

Например:

```text
USER → POST /students
```

запрещён.

Для модификации студентов требуется:

```text
ADMIN
```

---

## Database connection failed

Ошибка:

```text
Failed to obtain/create connection from connection pool [StudentPool]
```

Проверить:

```bash
docker ps
```

затем:

```powershell
asadmin ping-connection-pool StudentPool
```

---

## Таблицы отсутствуют

Если используется:

```xml
value="none"
```

а база новая, JPA не создаст таблицы.

Для первого запуска необходимо предварительно создать схему или временно использовать:

```xml
value="create"
```

---

## После redeploy исчезли данные

Проверьте `persistence.xml`.

Нельзя оставлять:

```xml
value="create"
```

для постоянной работы, так как при повторном создании схемы данные могут быть потеряны.

Используется:

```xml
value="none"
```

---

## `LocalDate not supported by default`

Для сериализации Java Time используется:

```text
jackson-datatype-jsr310
```

и:

```java
JavaTimeModule
```

в `JacksonConfig`.

---

# 🔒 Security notes

Проект реализует несколько уровней защиты.

### Passwords

Пароли хешируются через:

```text
BCrypt
```

Открытые пароли не сохраняются.

---

### JWT

JWT подписываются secret key.

Минимальная длина:

```text
32 bytes
```

---

### Refresh tokens

Refresh token:

- имеет expiration;
- хранится в БД только в виде SHA-256 hash;
- может быть отозван;
- после refresh старый token становится `revoked`;
- выдаётся новая пара токенов.

---

### Authorization

Проверка прав выполняется backend'ом через:

```text
JwtAuthenticationFilter
AuthorizationFilter
JwtSecurityContext
```

Frontend не является security boundary.

---

### Logs

Пароли и JWT не записываются в журнал.

Пользовательские строки очищаются через:

```text
Logs.clean()
```

для защиты от log forging.

---

# 🎯 Итог

Проект демонстрирует работу полноценного web-приложения:

```text
Frontend
+
REST API
+
Authentication
+
Authorization
+
Database
+
Validation
+
Documentation
+
Logging
```

Основной поток:

```text
Пользователь
    │
    ▼
Frontend
    │
    ▼
JWT Authentication
    │
    ▼
Role Authorization
    │
    ▼
REST Resource
    │
    ▼
Service
    │
    ▼
Repository
    │
    ▼
PostgreSQL
```

Приложение поддерживает безопасную регистрацию и авторизацию пользователей, разграничение прав доступа, полный CRUD студентов, поиск, фильтрацию, сортировку, пагинацию, документирование API через Swagger и тестирование через Bruno.

---

## 🎓 ITMO University

**Web Programming — Laboratory Work №3**

Student Management System  
Jakarta EE · PostgreSQL · JWT · GlassFish · OpenAPI