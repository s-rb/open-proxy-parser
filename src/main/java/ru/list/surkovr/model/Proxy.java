package ru.list.surkovr.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Proxy {

    private List<ProxyType> types = new LinkedList<ProxyType>();
    private String host;
    private int port;
    private int timeout;
    private boolean isValid;

    public Proxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void addProxyType(ProxyType type) {
        types.add(type);
    }

    public List<ProxyType> getTypes() {
        return types;
    }

    public void setTypes(List<ProxyType> types) {
        this.types = types;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Proxy() {
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "types=" + types +
                ", host='" + host + '\'' +
                ", port=" + port +
                ", timeout=" + timeout +
                ", isValid=" + isValid +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proxy proxy = (Proxy) o;
        return port == proxy.port &&
                host.equals(proxy.host);
    }

    @Override
    public int hashCode() {
        return Objects.hash(host, port);
    }
}
