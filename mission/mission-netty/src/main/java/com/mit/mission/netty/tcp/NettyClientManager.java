package com.mit.mission.netty.tcp;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyClientManager {
    private static volatile NettyClientManager clientManager;
    private ConcurrentHashMap<Channel, NettyClient> clients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, Channel> channels = new ConcurrentHashMap<>();
    private static final Object LOCK = new Object();

    public static NettyClientManager getInstance() {
        if (clientManager != null)
            return clientManager;

        if (clientManager == null) {
            synchronized (NettyClientManager.class) {
                if (clientManager == null) {
                    clientManager = new NettyClientManager();
                }
            }
        }
        return clientManager;
    }

    private NettyClientManager() {
    }

    public NettyClient getNettyClient(String serverIP, String serverPort) {
        Channel channel = getChannel(serverIP, serverPort);
        if (channel == null) {
            synchronized (LOCK) {
                if (channel == null) {
                    NettyClient nettyClient = new NettyClient(serverIP, serverPort);
                    return nettyClient;
                }
            }
        }
        return clients.get(channel);
    }

    public void removeClient(Channel channel) {
        clients.remove(channel);
        channels.values().removeIf(next -> next.compareTo(channel) == 0);
    }

    public void bindClient(NettyClient client) {
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
