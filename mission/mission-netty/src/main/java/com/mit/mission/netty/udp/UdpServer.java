package com.mit.mission.netty.udp;

import com.mit.mission.netty.tcp.TcpClientManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UdpServer {
    private Integer port;
    private Channel channel;

    private UdpServer(Integer serverPort) {
        this.port = serverPort;
        connect();
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
                        pipeline.addLast(new UdpServerHandler());

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
