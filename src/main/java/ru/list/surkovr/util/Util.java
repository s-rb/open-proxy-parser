package ru.list.surkovr.util;

public class Util {

    public static final int TIMEOUT_TO_REACH_PROXY = 1000;
    public static final String ADVANCED_NAME_URL = "https://advanced.name/ru/freeproxy";
    public static final String HIDE_MY_NAME_URL = "https://hidemy.name/ru/proxy-list";
    public static final int CHECK_PROXY_TIMEOUT_MS = 3000;
    public static final int TIMEOUT_REFRESH_PROXIES_MS = 3_600_000;
    public static final int TIMEOUT_BEFORE_START_PING = 300_000;
    public static final int TIMEOUT_AFTER_PING_MS = 900_000;
    public static final String[] USER_AGENT_LIST = {
            "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36" +
                    " (KHTML, like Gecko) Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.68,gzip(gfe)",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.99",
            "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/66.0.3359.170 Safari/537.36 OPR/53.0.2907.58",
            "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko)" +
                    " Chrome/66.0.3359.139 Safari/537.36 OPR/53.0.2907.37 (Edition avira)"};
    public static final String[] REFERER_LIST = {
            "http://www.google.ru", "http://www.google.com", "http://www.yandex.ru", "http://www.bing.com"};
}
