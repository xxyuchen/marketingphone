package com.geeker.marketing.netty;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.handler.DeviceRspHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
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
        JSONObject meta = JSON.parseObject(new String(data));
        String clientId = meta.getString("serial");
        channelHandlerContext.channel().attr(Attributes.DEVICE_ID_ATTR).set(clientId);
        clientHolder.addClient(channelHandlerContext.channel());

        String rspAction = meta.getString("rspAction");
        if (StringUtils.equals("batch", rspAction)) {
            JSONArray list = meta.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject item = JSON.parseObject(list.getString(i));
                String _rspAction = item.getString("rspAction");
                try {
                    processRequestData(channelHandlerContext, item, clientId, _rspAction);
                } catch (Throwable e) {
                    logger.warn("数据处理异常", e);
                }
            }
        } else {
            processRequestData(channelHandlerContext, meta, clientId, rspAction);
        }


    }

    private void processRequestData(ChannelHandlerContext channelHandlerContext, JSONObject meta, String clientId, String rspAction) {
        DeviceRspHandler deviceRspHandler = this.deviceRspHandlerMap.get(rspAction);
        if (null == deviceRspHandler) {
            logger.warn("没有对应类型的处理器:{} {} {}", rspAction, clientId, meta);
        } else {
            deviceRspHandler.process(channelHandlerContext.channel(), clientId, meta);
        }
    }

    public void addHandler(String action, DeviceRspHandler deviceRspHandler) {
        this.deviceRspHandlerMap.put(action, deviceRspHandler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}
