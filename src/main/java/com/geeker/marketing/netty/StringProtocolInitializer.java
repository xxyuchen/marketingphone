package com.geeker.marketing.netty;

import com.alibaba.fastjson.JSON;
import com.geeker.marketing.utils.DeviceNotFindException;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

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
        p.addLast("registerFailHandler", new ChannelInboundHandlerAdapter() {
            @Override
            public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                if (cause instanceof DeviceNotFindException) {
                    Map<String, Object> data = new HashMap<>();
                    String id = ctx.channel().attr(Attributes.DEVICE_ID_ATTR).get();
                    data.put("serial", id);
                    data.put("type", "cnf");
                    data.put("status", false);
                    data.put("action", "aaa");
                    NettyUtil.sendMessage(ctx, JSON.toJSONString(data));
                } else {
                    ctx.fireExceptionCaught(cause);
                }
            }
        });
    }

    public ServerHandler getServerHandler() {
        return serverHandler;
    }

    public void setServerHandler(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

}
