# open-proxy-parser
Parser for open proxies.

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