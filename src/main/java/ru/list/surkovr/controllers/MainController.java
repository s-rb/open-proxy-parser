package ru.list.surkovr.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.list.surkovr.model.Proxy;
import ru.list.surkovr.services.interfaces.ProxyService;

import java.util.Set;

@Controller
public class MainController {

    private final ProxyService proxyService;

    @Autowired
    public MainController(ProxyService proxyService) {
        this.proxyService = proxyService;
    }

    @GetMapping("/index")
    public String homePage(Model model) {
        addProxiesToModel("all", model);
        return "index";
    }

    @GetMapping("/socks")
    public String socksPage(Model model) {
        addProxiesToModel("socks", model);
        return "socks";
    }

    @GetMapping("/http")
    public String httpPage(Model model) {
        addProxiesToModel("http", model);
        return "http";
    }

    @GetMapping("/https")
    public String httpsPage(Model model) {
        addProxiesToModel("https", model);
        return "https";
    }

    private void addProxiesToModel(String attributeName, Model model) {
        Set<Proxy> proxies;
        switch (attributeName) {
            case ("socks"):
                proxies = proxyService.getSocksProxies();
                model.addAttribute("socksProxies", proxies);
                break;
            case ("http"):
                proxies = proxyService.getHttpProxies();
                model.addAttribute("httpProxies", proxies);
                break;
            case ("https"):
                proxies = proxyService.getHttpsProxies();
                model.addAttribute("httpsProxies", proxies);
                break;
            case ("all"):
                proxies = proxyService.getAllProxies();
                model.addAttribute("allProxies", proxies);
                break;
        }
    }

    @RequestMapping("/")
    public String indexPage(Model model) {
        addProxiesToModel("all", model);
        return "index"; // имя страницы, которую возвращаем
    }
}
