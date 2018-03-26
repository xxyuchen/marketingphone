package com.geeker.marketing;

import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
@SpringBootApplication
@Slf4j
public class MarketingphoneApplication {

    @Resource
    private ClientHolder clientHolder;

    @Resource(name = "cmdProducer")
    private DefaultMQProducer cmdProducer;

    public static void main(String[] args) {
        SpringApplication.run(MarketingphoneApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
       /* for(int i=0;i<=10;i++){
            String a = i+"baby baby baby...."+i;
            log.info("-------------{}",i);
            cmdProducer.send(new Message("op_device_cmd",i+"send"+i,i+"key"+i,a.getBytes()));
            TimeUnit.MILLISECONDS.sleep(3000);
        }*/
        return "this is Marketing mobile phone!";
    }

    @PostMapping("executeOperation/{id}")
    public DeferredResult<String> executeOperation(@PathVariable("id") String id, @RequestBody String body) {
        DeferredResult<String> deferredResult = new DeferredResult<>();
        Channel channel = clientHolder.getClient(id);
        if (null != channel) {
            NettyUtil.sendMessage(channel, body).addListener(future -> {
                if (future.isSuccess()) {
                    deferredResult.setResult("消息成功发送至" + id);
                } else {
                    deferredResult.setResult("消息发送失败:" + id + " 原因:" + future.cause().getLocalizedMessage());
                }
            });
        } else {
            deferredResult.setResult("客户端不在线");
        }
        return deferredResult;
    }
}
