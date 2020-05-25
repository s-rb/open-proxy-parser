package ru.list.surkovr.services.interfaces;

import ru.list.surkovr.model.Proxy;

import java.util.Set;

public interface ProxyPinger {

    Set<Proxy> checkProxies(Set<Proxy> proxies);
}
