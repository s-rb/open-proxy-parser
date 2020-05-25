package ru.list.surkovr.services;

import org.springframework.stereotype.Component;
import ru.list.surkovr.model.Proxy;
import ru.list.surkovr.services.interfaces.ProxyPinger;
import ru.list.surkovr.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.util.HashSet;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

@Component
public class ProxyPingerImpl implements ProxyPinger {

    private static Queue<Proxy> proxyQueue;
    private static Queue<Proxy> resultProxies;

    @Override
    public Set<Proxy> checkProxies(Set<Proxy> proxies) {
        ExecutorService executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() - 2);
        proxyQueue = new ConcurrentLinkedQueue<>();
        resultProxies = new ConcurrentLinkedQueue<>();
        proxyQueue.addAll(proxies);
        while (!proxyQueue.isEmpty()) {
            try {
                Callable<Void> task = checkPolledProxy();
                Future<Void> f = executorService.submit(task);
                f.get(Util.CHECK_PROXY_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        executorService.shutdown();
        return new HashSet<>(resultProxies);
    }

    private Callable<Void> checkPolledProxy() {
        return () -> {
            Proxy currentProxy = proxyQueue.poll();
            assert currentProxy != null;
            if (!isReachable(currentProxy)) throw new InterruptedException("Proxy isn't reachable");
            currentProxy.setValid(true);
            try {
                int pingMs = Integer.parseInt(Objects.requireNonNull(
                        pingProxy(currentProxy)));
                if (pingMs <= 0) throw new InterruptedException("Ping <= 0");
                currentProxy.setTimeout(pingMs);
            } catch (NumberFormatException | NullPointerException e) {
                throw new InterruptedException("Couldn't ping proxy");
            }
            if (!Thread.currentThread().isInterrupted()) resultProxies.add(currentProxy);
            return null;
        };
    }

    private String pingProxy(Proxy currentProxy) {
        try {
            Process p = Runtime.getRuntime().exec("ping " + currentProxy.getHost());
            BufferedReader inputStream = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String s = "";
            while ((s = inputStream.readLine()) != null) builder.append(s);
            String res = builder.toString().trim();
            return res.substring(res.length() - 7, res.length() - 2)
                    .replaceAll("=", "").trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isReachable(Proxy proxy) {
        try {
            InetAddress address = InetAddress.getByName(proxy.getHost());
            return address.isReachable(Util.TIMEOUT_TO_REACH_PROXY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
