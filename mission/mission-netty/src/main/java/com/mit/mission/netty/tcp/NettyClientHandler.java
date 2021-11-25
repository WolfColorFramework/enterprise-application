package com.mit.mission.netty.tcp;

import com.mit.mission.common.exception.CustomException;
import com.mit.mission.common.exception.CustomExceptionType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class NettyClientHandler extends SimpleChannelInboundHandler<TcpMessage> {
    // 用来接收结果的 promise 对象
    public static final Map<String, Promise<TcpMessage>> PROMISES = new ConcurrentHashMap<>();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        NettyClientManager.getInstance().removeClient(ctx.channel());
        ctx.channel().close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        IdleStateEvent event = (IdleStateEvent) evt;
        // 触发了写空闲事件
        if (event.state() == IdleState.WRITER_IDLE) {
            // "3s 没有写数据了，发送一个心跳包");
            ctx.writeAndFlush(TcpMessage.createHeartBeatMessage());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyClientManager.getInstance().removeClient(ctx.channel());
        log.error("{}", cause.getCause());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TcpMessage tcpMessage) {
        if (tcpMessage != null) {
            Promise<TcpMessage> promise = PROMISES.remove(tcpMessage.getUuid());
            if (tcpMessage.getResponseCode() == 0) {
                promise.setSuccess(tcpMessage);
            } else {
                promise.setFailure(new CustomException(CustomExceptionType.SYSTEM_ERROR, "netty异常"));
            }
        }
    }
}
