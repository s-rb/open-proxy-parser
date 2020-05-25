package ru.list.surkovr.services.interfaces;

import ru.list.surkovr.model.Proxy;

import java.util.Set;

public interface ProxyParser {

    Set<Proxy> parseProxies(String url);
}
