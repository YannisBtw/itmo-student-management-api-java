# web-lab-3: управление студентами

Jakarta EE 11 (JAX-RS, JPA/EclipseLink), PostgreSQL, JWT-аутентификация. Фронтенд лежит в `src/main/webapp`
и раздаётся из того же WAR.

## Конфигурация

Все параметры перечислены в `.env.example` (только имена, без значений). Реальные значения хранятся
в локальном `.env` или в настройках сервера и в репозиторий не попадают.

| Параметр | Кто читает | Назначение |
|---|---|---|
| `JWT_SECRET` | приложение | ключ подписи JWT, не короче 32 байт; без него приложение не стартует |
| `JWT_ACCESS_TTL_SECONDS`, `JWT_REFRESH_TTL_SECONDS` | приложение | время жизни access- и refresh-токенов |
| `ADMIN_USERNAME`, `ADMIN_EMAIL`, `ADMIN_PASSWORD` | приложение | администратор при старте (если заданы все три) |
| `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | docker compose | параметры контейнера с базой |

Приложение берёт значения из переменных окружения процесса или системных свойств JVM (`-DJWT_SECRET=...`);
файл `.env` оно само не читает.

## Запуск

1. `cp .env.example .env` и заполнить значения (`openssl rand -hex 32` для `JWT_SECRET`).
2. `docker compose up -d`: поднимает PostgreSQL.
3. В GlassFish создать JDBC-ресурс `jdbc/StudentDS` на эту базу и положить драйвер PostgreSQL в `lib` домена.
4. Передать `JWT_*` и `ADMIN_*` серверу, например `asadmin create-jvm-options "-DJWT_SECRET=..."`, и перезапустить домен.
5. `mvn clean package`, затем деплой `target/weblab3.war`.

## API

| Метод | Путь | Доступ |
|---|---|---|
| POST | `/api/auth/register`, `/api/auth/login`, `/api/auth/refresh` | без токена |
| GET | `/api/students`, `/api/students/{id}` | USER, ADMIN |
| POST, PUT, PATCH, DELETE | `/api/students[/{id}]` | ADMIN |

`GET /api/students` принимает фильтры (`fullName`, `isuId`, `group`, `dormitoryNumber`, `roomNumber`,
`moveInDate` в формате `DD-MM-YYYY`, `isForeign`, `notes`), сортировку (`sortBy`, `sortDirection`)
и пагинацию (`page`, `size`).

## Журнал сервера

Пишутся в `server.log` GlassFish: регистрация, вход (успешный и неудачный), создание, изменение и удаление
студентов (с указанием пользователя), а также все обработанные ошибки (400, 401, 403, 404, 409, 422).
Пароли и токены в журнал не попадают.
