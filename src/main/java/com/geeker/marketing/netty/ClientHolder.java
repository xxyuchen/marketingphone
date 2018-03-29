package com.geeker.marketing.netty;

import com.geeker.marketing.listener.PublicEvent;
import com.geeker.marketing.service.MicroDeviceService;
import com.geeker.marketing.service.OpConnectPoolService;
import com.geeker.marketing.utils.DeviceNotFindException;
import com.geeker.marketing.vo.DeviceVO;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lubin.Xuan on 2018-02-08.
 * {desc}
 */
/**
* @Author TangZhen
* @Date 2018/3/13 0013 14:50
* @Description  长连接维系业务
*/
@Component
@ChannelHandler.Sharable
@Slf4j
public class ClientHolder extends ChannelInboundHandlerAdapter {
    private Map<String, Channel> channelMap = new ConcurrentHashMap<>();

    @Resource
    private OpConnectPoolService opConnectPoolService;

    @Resource
    private MicroDeviceService microDeviceService;

    @Resource
    private ApplicationEventPublisher eventPublisher;

    public void addClient(Channel channel) throws Exception {
        String deviceId = channel.attr(Attributes.DEVICE_ID_ATTR).get();
        log.info("微营销设备【{}】请求连接",deviceId);
        if (null == microDeviceService.getbyDeviceId(deviceId)) {
            log.info("微营销设备【{}】不存在",deviceId);
            throw new DeviceNotFindException("微营销设备【{"+deviceId+"}】不存在",804);
        }
        //更新连接信息
        opConnectPoolService.registerConnect(deviceId);
        ChannelHandler channelHandler = channel.pipeline().get(this.getClass().getSimpleName());
        if (null == channelHandler) {
            channel.pipeline().addFirst(this.getClass().getSimpleName(), this);
            channelMap.put(channel.attr(Attributes.DEVICE_ID_ATTR).get(), channel);
        }
        eventPublisher.publishEvent(new PublicEvent.AddClientEvent("",deviceId));
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String deviceId = ctx.channel().attr(Attributes.DEVICE_ID_ATTR).get();
        this.channelMap.remove(deviceId);
        //注销连接
        opConnectPoolService.cancellation(deviceId);
        ctx.fireChannelInactive();
    }

    public Channel getClient(String deviceId) {
        return channelMap.get(deviceId);
    }

    public List<DeviceVO> allDevice(){
        Collection<Channel> channels = channelMap.values();
        List<DeviceVO> deviceVOS = new ArrayList<>();
        for (Channel channel:channels){
            DeviceVO device = new DeviceVO();
            device.setDeviceId(channel.attr(Attributes.DEVICE_ID_ATTR).get());
            device.setDeviceType(channel.attr(Attributes.DEVICE_TYPE).get());
            deviceVOS.add(device);
        }
        return deviceVOS;
    }
}
