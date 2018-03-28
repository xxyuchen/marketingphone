package com.geeker.marketing.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;

/**
 * @Author TangZhen
 * @Date 2018/3/13 0013 14:52
 * @Description 连接心跳
 */
@Slf4j
public abstract class CusHeartBeatHandler extends SimpleChannelInboundHandler<ByteBuf> {

    public static final byte PING_MSG = 1;
    public static final byte PONG_MSG = 2;
    public static final byte CUSTOM_MSG = 3;
    public static final byte AUTH_MSG = 4;
    protected String name;
    private boolean sendPong = true;

    public CusHeartBeatHandler(boolean sendPong) {
        this.sendPong = sendPong;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        msg.markReaderIndex();
        String deviceId = ctx.channel().attr(Attributes.DEVICE_ID_ATTR).get();
        try {
            byte type = msg.getByte(4);
            switch (type) {
                case PING_MSG:
                    log.debug("The client {} is online and send a 'ping'...", deviceId);
                    sendPongMsg(ctx);
                    break;
                case PONG_MSG:
                    log.debug(name + " get pong msg from " + ctx.channel().remoteAddress());
                    break;
                case AUTH_MSG:
                    handleAuth(ctx, msg);
                    break;
                case CUSTOM_MSG:
                    handleData(ctx, msg);
                    break;
            }
        } finally {
            msg.resetReaderIndex();
        }
    }

    protected abstract void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception;

    protected void handleAuth(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
    }

    protected byte[] read(ByteBuf msg, int skip) {
        byte[] data = new byte[msg.readableBytes() - 5];
        msg.skipBytes(skip);
        msg.readBytes(data);
        return data;
    }

    protected void sendAuth(ChannelHandlerContext channelHandlerContext, String authData, GenericFutureListener<? extends Future<? super Void>> listener) {
        ByteBuf byteBuf = null;
        try {
            byte[] data = authData.getBytes(Charset.forName("utf-8"));
            byteBuf = channelHandlerContext.alloc().buffer();
            byteBuf.retain();
            byteBuf.writeInt(5 + data.length);
            byteBuf.writeByte(CusHeartBeatHandler.AUTH_MSG);
            byteBuf.writeBytes(data, 0, data.length);
            ChannelFuture channelFuture = channelHandlerContext.writeAndFlush(byteBuf);
            if (null != listener) {
                channelFuture.addListener(listener);
            }
        } finally {
            if (null != byteBuf) {
                byteBuf.release();
            }
        }
    }

    protected void sendPingMsg(ChannelHandlerContext context) {
        ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PING_MSG);
        context.channel().writeAndFlush(buf);
        log.debug(name + " sent ping msg to " + context.channel().remoteAddress());
    }

    private void sendPongMsg(ChannelHandlerContext context) {
        if (!sendPong) {
            return;
        }
        ByteBuf buf = context.alloc().buffer(5);
        buf.writeInt(5);
        buf.writeByte(PONG_MSG);
        context.channel().writeAndFlush(buf);
        log.debug(name + " sent pong msg to " + context.channel().remoteAddress());
    }

    private void sendBuf(ChannelHandlerContext context, ByteBuf byteBuf) {
        try {
            context.channel().writeAndFlush(byteBuf);
        } finally {
            byteBuf.release();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case READER_IDLE:
                    handleReaderIdle(ctx);
                    break;
                case WRITER_IDLE:
                    handleWriterIdle(ctx);
                    break;
                case ALL_IDLE:
                    handleAllIdle(ctx);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("---" + ctx.channel().remoteAddress() + " is active---");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.debug("---" + ctx.channel().remoteAddress() + " is inactive---");
    }

    protected void handleReaderIdle(ChannelHandlerContext ctx) {
        log.debug("---READER_IDLE---");
    }

    protected void handleWriterIdle(ChannelHandlerContext ctx) {
        log.debug("---WRITER_IDLE---");
    }

    protected void handleAllIdle(ChannelHandlerContext ctx) {
        log.debug("---ALL_IDLE---");
    }
}
