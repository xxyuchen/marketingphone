package com.geeker.marketing;

import com.geeker.marketing.netty.ClientHolder;
import com.geeker.marketing.netty.NettyUtil;
import io.netty.channel.Channel;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.annotation.Resource;

@RestController
@SpringBootApplication
public class MarketingphoneApplication {

    @Resource
    private ClientHolder clientHolder;

    public static void main(String[] args) {
        SpringApplication.run(MarketingphoneApplication.class, args);
    }

    @RequestMapping("/hello")
    public String hello() {
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
