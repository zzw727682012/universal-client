package com.universal.client.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ProtocolFactory {
    private Map<String, Protocol> protocolMap = new ConcurrentHashMap<>();
    private static volatile ProtocolFactory instance;
    private static Object lock = new Object();

    private ProtocolFactory() {

    }

    public static ProtocolFactory getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new ProtocolFactory();
                    return instance;
                }
            }
        }
        return instance;
    }

    public void getConnector(String protocolName) {
        protocolMap.get(protocolName);
    }

    public void addConnector(Protocol protocol) {
        protocolMap.put(protocol.getProtocolName(), protocol);
    }

    public void removeConnector(Protocol protocol) {
        protocolMap.remove(protocol.getProtocolName());
    }

}
