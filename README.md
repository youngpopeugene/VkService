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


Рекомендуемые фреймворки и библиотеки:
* Spring Boot
* Apache Camel
* Jackson


## Что реализовано: ##

 
:white_check_mark: Валидация запросов на предмет корректности формата передаваемых полей 

:white_check_mark: Обработка ошибок от VK с выдачей информативного ответа

:white_check_mark: JSON Web Token (JWB) авторизация для API приложения ***(шифрование пароля - BCrypt)***

:white_check_mark: База данных PostgreSQL в контейнере Docker 

:white_check_mark: Кэширование ответов на стороне приложения 

:white_check_mark: Swagger документация 

:white_check_mark: JUnit тесты 

:x: Сборка и деплой приложения в облако ***(minikube/minishift)***


## Как запустить?


```
git clone https://github.com/youngpopeugene/VkService.git

cd VkService

docker-compose build && docker-compose up

mvn spring-boot:run
```


Запуск JUnit тестов 


```
mvn clean test
```


Swagger документация будет доступна по ссылке:


```
http://localhost:8080/swagger-ui/index.html
```

# P.S

Перед началом работы с приложением необходимо зарегистрироваться, и в качестве Response, вернется JWT токен. 
В последующие запросы добавьте следующий заголовок "Authorization: Bearer **...токен...**"
