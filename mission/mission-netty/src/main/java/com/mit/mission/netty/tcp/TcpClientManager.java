package com.mit.mission.netty.tcp;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TcpClientManager {
    private static volatile TcpClientManager clientManager;
    private ConcurrentHashMap<Channel, TcpClient> clients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    public static TcpClientManager getInstance() {
        if (clientManager != null)
            return clientManager;

        if (clientManager == null) {
            synchronized (TcpClientManager.class) {
                if (clientManager == null) {
                    clientManager = new TcpClientManager();
                }
            }
        }
        return clientManager;
    }

    private TcpClientManager() {
    }

    public TcpClient getNettyClient(String serverIP, String serverPort) {
        Channel channel = getChannel(serverIP, serverPort);
        if (channel == null) {
            synchronized (LOCK) {
                if (channel == null) {
                    TcpClient tcpClient = new TcpClient(serverIP, serverPort);
                    return tcpClient;
                }
            }
        }
        return clients.get(channel);
    }

    public void removeClient(Channel channel) {
        clients.remove(channel);
        channels.values().removeIf(next -> next.compareTo(channel) == 0);
    }

    public void bindClient(TcpClient client) {
        clients.put(client.getChannel(), client);
        channels.put(getKey(client.getRemoteIP(), client.getRemotePort()), client.getChannel());
    }

    private Channel getChannel(String serverIP, String serverPort) {
        return channels.get(getKey(serverIP, serverPort));
    }

    private String getKey(String serverIP, String serverPort) {
        return String.format("%s:%s", serverIP, serverPort);
    }
}
