package com.geeker.marketing.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2017-05-03.
 */
@Component
@Lazy(false)
public class TaskServer {

    private static final Logger logger = LoggerFactory.getLogger(TaskServer.class);

    @Resource
    private ServerBootstrap b;

    @Resource
    private InetSocketAddress tcpPort;

    private ChannelFuture serverChannelFuture;

    @PostConstruct
    private void init() throws InterruptedException {
        this.serverChannelFuture = b.bind(tcpPort).sync();
        logger.info("NettyServer start listen:{}", this.tcpPort.getPort());
    }

    @PreDestroy
    private void destroy() throws InterruptedException {
        serverChannelFuture.channel().closeFuture().sync();
    }

}
