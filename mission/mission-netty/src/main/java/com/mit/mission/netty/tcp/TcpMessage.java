package com.mit.mission.netty.tcp;

import lombok.Data;

@Data
public class TcpMessage {

    /**
     * 会话流水号 请求消息：发起方分配的，唯一标识本会话的流水号,应答消息：内容与请求消息相同 LONG 4字节
     */
    private String uuid;

    /**
     * 应答码 对于请求消息，默认为0； 应答消息填应答码 00:成功 01:报文格式错误 02:重复应答 03:目标结点不可达 04:通讯超时
     * 00表示收到并已处理成功，非00表示失败
     */
    private int responseCode;

    // 建一个心跳tcpMessage
    public static TcpMessage createHeartBeatMessage() {
        TcpMessage tcpMessage = new TcpMessage();
        // 属性值自行封装
//        tcpMessage.setOperaCode(Command.HEARTBEAT_MSG);
//        tcpMessage.setPath("");
//        tcpMessage.setResponseCode(0);
//        tcpMessage.setUuid(UuidUtil.shortUuid());
//        tcpMessage.setFlag(0x00);
//        tcpMessage.setVersion(0x01);
//        tcpMessage.setPackageContent("");
        return tcpMessage;
    }

}
