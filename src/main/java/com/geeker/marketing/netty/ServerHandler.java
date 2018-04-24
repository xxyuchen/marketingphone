package com.geeker.marketing.netty;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.geeker.marketing.handler.DeviceRspHandler;
import com.geeker.marketing.service.AuthenticateService;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017-05-03.
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class ServerHandler extends CusHeartBeatHandler {

    public ServerHandler() {
        super(false);
    }

    @Resource
    private ClientHolder clientHolder;

    @Resource
    private AuthenticateService authenticateService;

    private Map<String, DeviceRspHandler> deviceRspHandlerMap = new ConcurrentHashMap<>();

    @Override
    protected void handleData(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        boolean authenticated = channelHandlerContext.channel().attr(Attributes.AUTHENTICATED_ATTR).get();
        if (!authenticated) {
            log.info("接收到未认证的数据请求,连接将被强制关闭");
            channelHandlerContext.channel().close();
            return;
        }
        JSONObject meta = JSON.parseObject(new String(read(byteBuf, 5), Charset.forName("utf-8")));
        String clientId = channelHandlerContext.channel().attr(Attributes.DEVICE_ID_ATTR).get();
        String rspAction = meta.getString("rspAction");
        if (StringUtils.equals("batch", rspAction)) {
            JSONArray list = meta.getJSONArray("list");
            for (int i = 0; i < list.size(); i++) {
                JSONObject item = JSON.parseObject(list.getString(i));
                String _rspAction = item.getString("rspAction");
                try {
                    processRequestData(channelHandlerContext, item, clientId, _rspAction);
                } catch (Throwable e) {
                    log.warn("数据处理异常", e);
                }
            }
        } else {
            processRequestData(channelHandlerContext, meta, clientId, rspAction);
        }
    }

    private void processRequestData(ChannelHandlerContext channelHandlerContext, JSONObject meta, String clientId, String rspAction) {
        DeviceRspHandler deviceRspHandler = this.deviceRspHandlerMap.get(rspAction);
        if (null == deviceRspHandler) {
            log.warn("没有对应类型的处理器:{} {} {}", rspAction, clientId, meta);
        } else {
            deviceRspHandler.process(channelHandlerContext.channel(), clientId, meta);
        }
    }

    /**
     * 设备认证处理
     *
     * @param channelHandlerContext
     * @param byteBuf
     * @throws Exception
     */
    @Override
    protected void handleAuth(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        JSONObject meta = JSON.parseObject(new String(read(byteBuf, 5), Charset.forName("utf-8")));
        String ticket = meta.getString("requestCode");
        String deviceId = meta.getString("serial");
        if (authenticateService.authTicket(deviceId, ticket)) {
            channelHandlerContext.channel().attr(Attributes.DEVICE_ID_ATTR).set(deviceId);
            channelHandlerContext.channel().attr(Attributes.AUTHENTICATED_ATTR).set(true);
            sendAuth(channelHandlerContext, "success", null);
            clientHolder.addClient(channelHandlerContext.channel());
        } else {
            sendAuth(channelHandlerContext, "fail", null);
        }
    }

    public void addHandler(String action, DeviceRspHandler deviceRspHandler) {
        this.deviceRspHandlerMap.put(action, deviceRspHandler);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(Attributes.AUTHENTICATED_ATTR).set(false);
        sendAuth(ctx, "auth", null);
        ctx.fireChannelActive();
    }
}
