package com.geeker.marketing.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2017-05-04.
 */
public class NettyUtil {

    public static ChannelFuture sendMessage(ChannelHandlerContext ctx, String message) {
        return sendMessage(ctx.channel(), message);
    }

    public static ChannelFuture sendMessage(Channel channel, String action, String id, String data) {
        data = String.format("%s\n%s\n%s", action, id, data);
        return sendMessage(channel, data);
    }

    public static ChannelFuture sendMessage(Channel channel, String message) {
        ByteBuf byteBuf = null;
        try {
            byte[] data = null;
            if (StringUtils.isNotBlank(message)) {
                data = message.getBytes(Charset.forName("utf-8"));
            }

            int contentLength = null != data ? data.length : 0;
            byteBuf = channel.alloc().buffer(5 + contentLength);
            byteBuf.retain();
            byteBuf.writeInt(5 + contentLength);
            byteBuf.writeByte(CusHeartBeatHandler.CUSTOM_MSG);
            if (null != data) {
                byteBuf.writeBytes(data, 0, data.length);
            }
            return channel.writeAndFlush(byteBuf);
        } finally {
            if (null != byteBuf) {
                byteBuf.release();
            }
        }
    }
}
