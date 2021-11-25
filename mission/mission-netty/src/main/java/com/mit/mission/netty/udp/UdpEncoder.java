package com.mit.mission.netty.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class UdpEncoder extends MessageToMessageEncoder<UdpMessage> {
    @Override
    protected void encode(ChannelHandlerContext ctx, UdpMessage msg, List<Object> out) throws Exception {
        // 发送音频
        ByteBuf buf = ctx.alloc().buffer(msg.getContent().length);
        buf.writeBytes(msg.getContent());
        DatagramPacket packet = new DatagramPacket(buf, msg.getRemoteAddress());
        out.add(packet);
    }
}
