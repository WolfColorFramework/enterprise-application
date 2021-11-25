package com.mit.mission.netty.tcp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultPromise;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
public class NettyClient {
    private Bootstrap bootstrap;
    private NioEventLoopGroup group;
    private Channel channel;
    private String remoteIP;
    private String remotePort;

    public NettyClient(String remoteIP, String remotePort) {
        this.remoteIP = remoteIP;
        this.remotePort = remotePort;
        bootstrap = new Bootstrap();
        connect();
    }

    public TcpMessage sendMessage(TcpMessage message) throws InterruptedException {
        if (channel == null && !channel.isActive()) {
            reconnect();
        }
        channel.writeAndFlush(message);

        DefaultPromise<TcpMessage> promise = new DefaultPromise<>(channel.eventLoop());
        NettyClientHandler.PROMISES.put(message.getUuid(), promise);

        promise.await();
        if (promise.isSuccess()) {
            // 调用正常
            return promise.getNow();
        } else {
            // 调用失败
            throw new RuntimeException(promise.cause());
        }
    }

    private void connect() {
        //创建EventLoopGroup
        group = new NioEventLoopGroup();
        NettyClientHandler handler = new NettyClientHandler();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                             @Override
                             protected void initChannel(SocketChannel ch) throws Exception {
                                 ChannelPipeline pipeline = ch.pipeline();
                                 // 编码器、解码器，自行编写
//                                 pipeline.addLast(new MessageEncoder());
//                                 pipeline.addLast(new MessageDecoder());
                                 pipeline.addLast(new IdleStateHandler(0, 5, 0));
                                 pipeline.addLast(handler);
                             }
                         }
                );
        try {
            channel = bootstrap.connect(remoteIP, Integer.parseInt(remotePort)).sync().channel();
            NettyClientManager.getInstance().bindClient(this);
            channel.closeFuture().addListener(future -> group.shutdownGracefully());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reconnect() {
        if (bootstrap != null) {
            try {
                ChannelFuture channelFuture = bootstrap.connect(remoteIP, Integer.parseInt(remotePort)).sync();
                channelFuture.addListener(future -> {
                    if (future.isSuccess()) {
                        log.info("remoteIP:" + remoteIP + ",Port:" + remotePort + "，连接成功");
                    }
                });
                channel = channelFuture.channel();

                NettyClientManager.getInstance().bindClient(this);

                channel.closeFuture().addListener(future -> group.shutdownGracefully());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            connect();
        }
    }
}
