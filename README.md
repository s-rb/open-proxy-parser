# open-proxy-parser

Parser for open proxies.
Warning! Project is outdated, because of major changes of the source website (advanced.name)

![Java](https://img.shields.io/badge/-Java-05122A?style=flat&logo=Java&logoColor=FFA518) ![WebService](https://img.shields.io/badge/-WebService-05122A?style=flat) ![Parser](https://img.shields.io/badge/-Parser-05122A?style=flat) ![Spring](https://img.shields.io/badge/-Spring-05122A?style=flat&logo=Spring&logoColor=71b23c) ![Springboot](https://img.shields.io/badge/-SpringBoot-05122A?style=flat&logo=Springboot&logoColor=71b23c) ![HTMLunit](https://img.shields.io/badge/-HtmlUnit-05122A?style=flat) ![Maven](https://img.shields.io/badge/-Maven-05122A?style=flat&logo=apachemaven&logoColor=fffffb) ![JSOUP](https://img.shields.io/badge/-JSOUP-05122A?style=flat) ![Thymeleaf](https://img.shields.io/badge/-Thymeleaf-05122A?style=flat&logo=Thymeleaf) ![REST](https://img.shields.io/badge/-REST-05122A?style=flat)

Parses open proxies from all pages of the Advanced.name website (generated by JS).

Applied:
* HtmlUnit
* Jsoup
* Springboot
* Thymeleaf

The application checks proxies for availability, ping, removes inaccessible ones, filters by type and returns by API:

* */index - all proxies
* */socks - SOCKS proxy
* */http - HTTP proxy
* */https - HTTPS proxy

Validation and parsing are run periodically in separate daemon threads.

Scan tasks are added to a pool with a fixed number of threads (number of processors - 2).

There is a timeout for checking each proxy, after which the transition to the next task.