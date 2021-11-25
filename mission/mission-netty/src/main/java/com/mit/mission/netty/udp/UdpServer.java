package com.mit.mission.netty.udp;

import com.mit.mission.netty.tcp.NettyClientManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;

@Slf4j
public class UdpServer {
    private Integer port;
    private InetSocketAddress remoteAddress;
    private static volatile UdpServer udpServer;
    private Channel channel;

    private UdpServer(Integer serverPort, String clientIP, Integer clientPort) {
        this.port = serverPort;
        remoteAddress = new InetSocketAddress(clientIP, clientPort);
        connect();
    }

    public static UdpServer getInstance(Integer serverPort, String clientIP, Integer clientPort) {
        if (udpServer != null)
            return udpServer;

        if (udpServer == null) {
            synchronized (NettyClientManager.class) {
                if (udpServer == null) {
                    udpServer = new UdpServer(serverPort, clientIP, clientPort);
                }
            }
        }
        return udpServer;
    }

    public void sendMessage(byte[] message) {
        UdpMessage udpMessage = new UdpMessage(message, remoteAddress);
        channel.writeAndFlush(udpMessage);
    }

    private void connect() {
        //1.NioEventLoopGroup是执行者
        NioEventLoopGroup group = new NioEventLoopGroup();
        //2.启动器
        Bootstrap bootstrap = new Bootstrap();
        //3.配置启动器
        bootstrap.group(group)//3.1指定group
                .channel(NioDatagramChannel.class)//3.2指定channel
                .option(ChannelOption.SO_BROADCAST, true)//3.3指定为广播模式
                .handler(new ChannelInitializer<NioDatagramChannel>() {
                    @Override
                    protected void initChannel(NioDatagramChannel nioDatagramChannel) throws Exception {
                        //3.4在pipeline中加入解码器，和编码器（用来发送UDP）
                        ChannelPipeline pipeline = nioDatagramChannel.pipeline();
                        pipeline.addLast(new UdpDecoder())
                                .addLast(new UdpEncoder());
                    }
                });
        try {
            //4.bind到指定端口，并返回一个channel，该端口就是监听UDP报文的端口
            channel = bootstrap.bind(port).sync().channel();
            //5.等待channel的close，异步关闭group
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
