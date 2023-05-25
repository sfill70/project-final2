## [REST API](http://localhost:8080/doc)

## Концепция:
- Spring Modulith
  - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
  - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
  - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```
- Есть 2 общие таблицы, на которых не fk
  - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
  - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем проверять

## Аналоги
- https://java-source.net/open-source/issue-trackers

## Тестирование
- https://habr.com/ru/articles/259055/

Список выполненных задач:

2. Удалить социальные сети: vk, yandex
   удалены кнопки login.html
   удалены настройки для в application.yaml
   handler  VkOAuth2UserDataHandler and YandexOAuth2UserDataHandler удалена анотация компонент классы оставлены
   Изменены настройки liquibase

3. перенесение из application.yaml datasource, OAuth и mail (user, password) (секция security)  в secret.yaml  
   replace datasource, OAuth and mail (user, password) from application.yaml in secret.yaml.
- данные считываются из переменной окружения машины - %SECRET_PROPERTY%, в ней указан путь к файлу secret.yaml
  с property
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/secret_property.png)
4.  Переделать тесты так, чтоб во время тестов использовалась in memory БД (H2), а не PostgreSQL. 
    Для этого нужно определить 2 бина, и выборка какой из них использовать должно определяться активным профилем Spring.

5.  Написать тесты для всех публичных методов контроллера ProfileRestController.
    ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/test.png)
    ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/test2.png)

6. Добавить новый функционал: добавления тегов к задаче. Фронт делать необязательно.
- в  TaskController добавлен POST метод addTagToTask, в TaskService метод addTagToTask
  маппинг "/{id}/tag", принимает данные формата JSON и данные форм.
- в TaskControllerRest добавлен PUT метод addTagToTask "/{id}/tags" принимает данные (array) формата JSON
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/point_6.png)
  ![Image alt](https://raw.githubusercontent.com/sfill70/project-final/blob/master/resources/static/img/point_6.png)

7. Добавить возможность подписываться на задачи, которые не назначены на текущего пользователя.
- в  DashboardUIController добавлен метод addUserToTask, в TaskService метод addUserToTask
  маппинг "/task/{id}/user/{userId}", где {id} id - task, {userId} - id user
  пример (/task/2/user/1} добавляет task id=2, user id=1)
  маппинг "/task/{id}/user", принимает данные формата JSON, text, html, xml в виде String (Integer поддерживает только JSON).

8. Добавить автоматический подсчет времени сколько задача находилась в работе и тестировании.
   Написать 2 метода на уровни сервиса, который параметром принимает задачу и возвращают затраченное время.
- в TaskService добавлен один метод public Map<String, String> getTaskSummary(Long taskId) который возвращает Map с
  требуемыми результатами
- TaskControllerRest добавлен POST  metod  getSummary мапинг "/summary/{id}", возвращает  Map c результатом подсчета.
  Fix StackOverflowError in interface TaskMapper.
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/login.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/summary_8.png)

9. Написать Dockerfile для основного сервера
  Написан

10. Написать docker-compose файл для запуска контейнера сервера вместе с БД и nginx. Для nginx используй конфиг-файл config/nginx.conf.
-  При необходимости файл конфига можно редактировать. Hard task
- Написан
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/run_in_docker.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/run_in_docker_2.png)


11. Добавить локализацию минимум на двух языках для шаблонов писем и стартовой страницы index.htm
- Добавлено с использованием Internationalization (i18n)
- https://javastudy.ru/spring-mvc/localization/
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/local_ru.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/local_en.png)

Перевел тесты на Testcontainers
!!!!!!!!!!!-> в Windows 10 обязательно должен быть запущен Docker !!!!!!!!!!!!!!!!!!!!!!!!!!!!


  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/testcontainer_test.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/testcontainer_test2.png)

  Проверка - тестов, сборки и запуска на Ubuntu 22.04
  Сборку и тестирование проходить под Super User - sudo su.


  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/bilding_and_test_project.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/bilding_and_test_project_2.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/ubuntu_run.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/ubuntu_run2.png)
  ![Image alt](https://github.com/sfill70/project-final/blob/master/resources/static/img/ubuntu_run3.png)

12.а
13.к

для зпуска проекта ввести в терминале команду->     docker-compose up

!!!!!!!!!!!!!!!!!-Важно изменен порт Nginx-!!!!!!!!!!!!!!!!!!!!!!!!!
После запуска проекта в Docker  - проект доступен по адресу- localhost:88 !!!!!

При залогинивании и разлогинивании, происходит переадресация на localhost:80 ???
надо вручную перейти на localhost:88




