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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class ProxyServiceImpl implements ProxyService {

    public static final String BACKUP_PROXIES_TXT = "proxies.txt";
    public static final String SEPARATOR = ",";
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
        return getFilteredProxies(p -> true);
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
        loadLastProxies();
        synchronized (proxies) {
            startParserDaemon();
            startPingerDaemon();
        }
    }

    private void loadLastProxies() {
        File srcFile = new File(BACKUP_PROXIES_TXT);
        try {
            Files.readAllLines(srcFile.toPath()).forEach(l -> {
                String[] split = l.split(SEPARATOR);
                String host = split[0];
                String portString = split[1];
                Proxy tempProxy = new Proxy(host, Integer.parseInt(portString));
                String timeoutString = split[2];
                tempProxy.setTimeout(Integer.parseInt(timeoutString));
                tempProxy.setValid(true);
                String typeString = split[3];
                ProxyType proxyType = getProxyType(typeString);
                tempProxy.addProxyType(proxyType);
                proxies.add(tempProxy);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ProxyType getProxyType(String proxyType) {
        ProxyType type;
        switch (proxyType) {
            case ("HTTP"):
                type = ProxyType.HTTP;
                break;
            case ("HTTPS"):
                type = ProxyType.HTTPS;
                break;
            case ("SOCKS"):
                type = ProxyType.SOCKS;
                break;
            case ("SOCK4"):
                type = ProxyType.SOCKS4;
                break;
            case ("SOCKS5"):
                type = ProxyType.SOCKS5;
                break;
            default:
                type = ProxyType.HTTP;
                break;
        }
        return type;
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

    private void writeProxiesToFile() {
        try {
            if (Files.notExists(Path.of(BACKUP_PROXIES_TXT)))
                Files.createFile(Path.of(BACKUP_PROXIES_TXT));
        } catch (IOException e) {
            e.printStackTrace();
        }
        File srcFile = new File(BACKUP_PROXIES_TXT);
        try (FileWriter fileWriter = new FileWriter(srcFile);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            for (Proxy p : proxies) {
                StringBuilder builder = new StringBuilder();
                builder.append(p.getHost()).append(SEPARATOR).append(p.getPort())
                        .append(SEPARATOR).append(p.getTimeout()).append(SEPARATOR)
                        .append(p.getTypes().get(0)).append("\n");
                String data = builder.toString();
                bufferedWriter.write(data);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startParserDaemon() {
        Thread anParserThread = new Thread(() -> {
            while (true) {
                proxies.addAll(anProxyParser.parseProxies(Util.ADVANCED_NAME_URL));
                writeProxiesToFile();
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
