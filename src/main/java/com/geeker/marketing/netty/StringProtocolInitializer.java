package com.geeker.marketing.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2017-05-03.
 */
@Component
public class StringProtocolInitializer extends ChannelInitializer<SocketChannel> {
    @Resource
    ServerHandler serverHandler;

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new IdleStateHandler(20, 0, 0));
        p.addLast(new LengthFieldBasedFrameDecoder(512 * 1024, 0, 4, -4, 0));
        p.addLast(serverHandler);
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

}
