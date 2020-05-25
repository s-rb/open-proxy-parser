package ru.list.surkovr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.list.surkovr.model.Proxy;
import ru.list.surkovr.model.ProxyType;
import ru.list.surkovr.services.interfaces.ProxyParser;
import ru.list.surkovr.services.interfaces.ProxyPinger;
import ru.list.surkovr.services.interfaces.ProxyService;
import ru.list.surkovr.util.Util;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProxyServiceImpl implements ProxyService {

    private final Set<Proxy> proxies = new HashSet<>();
    private final ProxyPinger proxyPinger;
    private final ProxyParser anProxyParser;

    @Autowired
    public ProxyServiceImpl(ProxyPinger proxyPinger,
                            @Qualifier("advancedNameParser") ProxyParser anProxyParser) {
        this.proxyPinger = proxyPinger;
        this.anProxyParser = anProxyParser;
    }

    @Override
    public Set<Proxy> getAllProxies() {
        return proxies;
    }

    @Override
    public Set<Proxy> getSocksProxies() {
        return getFilteredProxies(p -> (p.getTypes().contains(ProxyType.SOCKS)
                || p.getTypes().contains(ProxyType.SOCKS4)
                || p.getTypes().contains(ProxyType.SOCKS5)));
    }

    @Override
    public Set<Proxy> getHttpProxies() {
        return getFilteredProxies(p -> p.getTypes().contains(ProxyType.HTTP));
    }

    @Override
    public Set<Proxy> getHttpsProxies() {
        return getFilteredProxies(p -> p.getTypes().contains(ProxyType.HTTPS));
    }

    private Set<Proxy> getFilteredProxies(Predicate<? super Proxy> predicate) {
        return Collections.unmodifiableSet(proxies).stream()
                .filter(predicate)
                .sorted((p1, p2) -> p2.getTimeout() != 0 ? p1.getTimeout() - p2.getTimeout() : -1)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @PostConstruct
    public void start() {
        synchronized (proxies) {
            startParserDaemon();
            startPingerDaemon();
        }
    }

    private void startPingerDaemon() {
        Thread pingerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(Util.TIMEOUT_BEFORE_START_PING);
                    Set<Proxy> temp = proxyPinger.checkProxies(proxies);
                    proxies.clear();
                    proxies.addAll(temp);
                    Thread.sleep(Util.TIMEOUT_AFTER_PING_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        pingerThread.setDaemon(true);
        pingerThread.start();
    }

    private void startParserDaemon() {
        Thread anParserThread = new Thread(() -> {
            while (true) {
                proxies.addAll(anProxyParser.parseProxies(Util.ADVANCED_NAME_URL));
                try {
                    Thread.sleep(Util.TIMEOUT_REFRESH_PROXIES_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        anParserThread.setDaemon(true);
        anParserThread.start();
    }
}
