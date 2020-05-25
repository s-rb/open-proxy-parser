package ru.list.surkovr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// TODO: проверка на валидность и анонимность через myip https://forum.antichat.ru/threads/350821/#post-3271758
// TODO: добавить парсеры других сайтов
// TODO: вынести параметры во внешний файл
// TODO: сделать десктоп версию
// TODO: оптимизировать сбор ссылок (дважды получаем страницы - для заполнения очереди ссылок и для парсинга
// TODO: Заменить библиотеку HtmlUnit на менее строгую
// TODO: Сделать фильтр пинга - добавлять только после первичного замера пинга, "0" заменить

@SpringBootApplication()
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
