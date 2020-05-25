package ru.list.surkovr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// TODO: проверка на валидность и анонимность через myip https://forum.antichat.ru/threads/350821/#post-3271758
// TODO: добавить парсеры других сайтов
// TODO: вынести параметры во внешний файл
// TODO: сделать десктоп версию

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
