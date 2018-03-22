package com.geeker.marketing.netty;


import com.geeker.marketing.handler.DeviceRspHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017-05-03.
 */
@Component
@ChannelHandler.Sharable
public class ServerHandler extends CusHeartBeatHandler {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    public ServerHandler() {
        super(false);
    }

    @Resource
    private ClientHolder clientHolder;

    private Map<String, DeviceRspHandler> deviceRspHandlerMap = new ConcurrentHashMap<>();

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        byte[] data = new byte[byteBuf.readableBytes() - 5];
        logger.info("内容长度:{} 内容类型:{} ByteLength:{}", byteBuf.readInt(), byteBuf.readByte(), byteBuf.readableBytes());
        byteBuf.readBytes(data);
        String content = new String(data);
        String[] arr = content.split("\n");
        String action = arr[0];
        String clientId = arr[1];
        channelHandlerContext.channel().attr(Attributes.DEVICE_ID_ATTR).set(clientId);
        DeviceRspHandler deviceRspHandler = this.deviceRspHandlerMap.get(action);
        clientHolder.addClient(channelHandlerContext.channel());
        if (null == deviceRspHandler) {
            logger.warn("没有对应类型的处理器:{} {} {}", action, clientId, arr[2]);
        } else {
            deviceRspHandler.process(channelHandlerContext.channel(), clientId, arr[2]);
        }
    }
    public void addHandler(String action, DeviceRspHandler deviceRspHandler) {
        this.deviceRspHandlerMap.put(action, deviceRspHandler);
    }
}
