# open-proxy-parser
Parser for open proxies.

![Java](https://img.shields.io/badge/-Java-05122A?style=flat&logo=Java&logoColor=FFA518) ![WebService](https://img.shields.io/badge/-WebService-05122A?style=flat) ![Parser](https://img.shields.io/badge/-Parser-05122A?style=flat) ![Spring](https://img.shields.io/badge/-Spring-05122A?style=flat&logo=Spring&logoColor=71b23c) ![Springboot](https://img.shields.io/badge/-SpringBoot-05122A?style=flat&logo=Springboot&logoColor=71b23c) ![HTMLunit](https://img.shields.io/badge/-HtmlUnit-05122A?style=flat) ![Maven](https://img.shields.io/badge/-Maven-05122A?style=flat&logo=apachemaven&logoColor=fffffb) ![JSOUP](https://img.shields.io/badge/-JSOUP-05122A?style=flat) ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-05122A?style=flat&logo=Thymeleaf) ![REST](https://img.shields.io/badge/-REST-05122A?style=flat)

Парсит открытые прокси со всех страниц сайта Advanced.name (формируются JS).

Применены:
* HtmlUnit
* Jsoup
* Springboot
* Thymeleaf

Приложение проверяет прокси на доступность, пинг, удаляет недоступные, фильтрует по типу и выдает по API:

* */index - все прокси
* */socks - SOCKS прокси
* */http - HTTP прокси
* */https - HTTPS прокси

Проверка и парсинг запускаются периодически в отдельных потоках-демонах.

Задачи проверки добавляются в пул с фиксированным количеством потоков (кол-во процессоров - 2).

Есть таймаут на проверку каждого прокси, по истечении которого переход к следующей задаче.

TODO:
* Добавить парсеры других сайтов.
* Сделать десктоп версию.
* Добавить экспорт в .csv
* Сделать сортировку по Ping
* Проверка на валидность и анонимность через myip
* Вынести параметры настройки во внешний файл