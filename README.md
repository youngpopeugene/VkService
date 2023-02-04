# Tech task

### Необходимо разработать интеграционный RESTful cloud-native сервис на Java для получения ФИО пользователя VK, а также признака участника группы VK. Для работы с VK следует использовать API Вконтакте.

**Техническое описание сервиса:** Сервис должен реализовывать REST-метод на вход принимающий HTTP заголовок vk_service_token и тело запроса в формате JSON вида:

```json
{
"user_id": 239,
"group_id": 239
}
```

Где:
* user_id - идентификатор пользователя VK
* group_id - идентификатор группы VK
* vk_service_token - сервисный ключ приложения VK используемый для вызовов API VK

В ответ должно приходить тело в формате JSON вида:

```json
{
"last_name": "youngpope",
"first_name": "eugene",
"member": true
}
```

Где:
* last_name - фамилия пользователя user_id
* first_name - имя пользователя user_id
* member - признак того, что пользователь состоит в группе group_id

### Что реализовано: ### 

:white_check_mark: Валидация запросов на предмет корректности формата и передаваемых полей 

:white_check_mark: Обработка ошибок от VK с выдачей информативного ответа

:white_check_mark: Swagger документация 

:white_check_mark: JUnit тесты 

:white_check_mark: Кэширование ответов на стороне приложения 

:white_check_mark: База данных PostgreSQL в контейнере Docker 

:white_check_mark: JSON Web Token (JWB) аутентификация 

:x: Сборка и деплой приложения в облако (minikube/minishift)
