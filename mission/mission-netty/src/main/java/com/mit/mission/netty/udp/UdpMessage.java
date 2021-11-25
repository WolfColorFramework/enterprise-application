package com.mit.mission.netty.udp;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.net.InetSocketAddress;

@Data
@AllArgsConstructor
public class UdpMessage {
    private byte[] content;
    private InetSocketAddress remoteAddress;

}
