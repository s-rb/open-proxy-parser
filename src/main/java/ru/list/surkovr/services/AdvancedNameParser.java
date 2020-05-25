package ru.list.surkovr.services;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import ru.list.surkovr.util.Util;
import ru.list.surkovr.model.Proxy;
import ru.list.surkovr.model.ProxyType;
import ru.list.surkovr.services.interfaces.ProxyParser;

import java.io.IOException;
import java.util.*;

// https://advanced.name/ru/freeproxy?page=1
@Component
public class AdvancedNameParser implements ProxyParser {

    private Queue<String> urlsToParse = new LinkedList<>();

    public Set<Proxy> parseProxies(String url) {
        findAllUrls(url);
        Set<Proxy> res = start();
        if (res != null) return res;
        System.err.println("Прокси получить не удалось");
        return null;
    }

    private void findAllUrls(String rootUrl) {
        try {
        int i = 1;
        while (true) {
            String url = rootUrl + "?page=" + i++;
            System.out.println("=====> ПАРСИМ " + url);
            Thread.sleep((int) (Math.random() * 2000) + 500);
            Document doc = getDocument(url);
            Elements tableRows = getTableRows(doc);
            if (tableRows.isEmpty()) break;
            urlsToParse.add(url);
        }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    private Set<Proxy> start() {
        try {
            Set<Proxy> proxies = new HashSet<>();
            while (!urlsToParse.isEmpty()) {
                Thread.sleep((int) (Math.random() * 2000) + 500);
                Document doc = getDocument(urlsToParse.poll());
                Elements tableRows = getTableRows(doc);
                Set<Proxy> temp = getProxies(tableRows);
                proxies.addAll(temp);
            }
            return proxies;
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private Document getDocument(String url) throws IOException {
        WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        HtmlPage htmlPage = webClient.getPage(url);
        return Jsoup.parse(htmlPage.asXml());
    }

    private Set<Proxy> getProxies(Elements tableRows) {
        Set<Proxy> res = new HashSet<>();
        for (int i = 0; i < tableRows.size(); i++) {
            Element row = tableRows.get(i);
            Elements columns = row.select("td");
            Proxy currentProxy = new AnnotationConfigApplicationContext().getBean(Proxy.class);
            int columnNumber = 0;
            for (Element column : columns) {
                if (columnNumber == 1) {
                    currentProxy.setHost(column.text().trim());
                }
                if (columnNumber == 2) {
                    currentProxy.setPort(Integer.parseInt(column.text().trim()));
                }
                if (columnNumber == 3) {
                    Elements links = column.select("a");
                    addTypesToProxy(currentProxy, links);
                }
            columnNumber++;
            }
            res.add(currentProxy);
        }
        return res;
    }

    private void addTypesToProxy(Proxy currentProxy, Elements links) {
        for (Element link : links) {
            String linkText = link.text().trim();
            if (linkText.equalsIgnoreCase("HTTPS"))
                currentProxy.addProxyType(ProxyType.HTTPS);
            if (linkText.equalsIgnoreCase("HTTP"))
                currentProxy.addProxyType(ProxyType.HTTP);
            if (linkText.equalsIgnoreCase("SOCKS4"))
                currentProxy.addProxyType(ProxyType.SOCKS4);
            if (linkText.equalsIgnoreCase("SOCKS5"))
                currentProxy.addProxyType(ProxyType.SOCKS5);
            if (linkText.equalsIgnoreCase("SOCKS"))
                currentProxy.addProxyType(ProxyType.SOCKS);
        }
    }

    private Elements getTableRows(Document document) {
        Element table = document.getElementById("table_proxies");
        Element tableBody = table.select("tbody").first();
        return tableBody.getElementsByTag("tr");
    }

    private String[] generateRandomUserAgentReferrer() {
        int randomUserAgent = (int) (Math.random() * 3);
        int randomReferrer = (int) (Math.random() * 3);
        return new String[]{Util.USER_AGENT_LIST[randomUserAgent], Util.REFERER_LIST[randomReferrer]};
    }
}
