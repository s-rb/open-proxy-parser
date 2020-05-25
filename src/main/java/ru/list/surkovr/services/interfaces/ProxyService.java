package ru.list.surkovr.services.interfaces;

import ru.list.surkovr.model.Proxy;

import java.util.Set;

public interface ProxyService {

    Set<Proxy> getSocksProxies();

    Set<Proxy> getHttpProxies();

    Set<Proxy> getHttpsProxies();

    Set<Proxy> getAllProxies();
}
