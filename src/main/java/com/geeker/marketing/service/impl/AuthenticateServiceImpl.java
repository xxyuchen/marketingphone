package com.geeker.marketing.service.impl;

import com.geeker.marketing.service.AuthenticateService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Lubin.Xuan on 2018-03-28.
 * {desc}
 */
@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    private Map<String, String> ticketMap = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        ticketMap.put("4c116ee17d14", "5DFQSJZJflO50ioGbaPtdMcuLP1YNeHjznF6WACg8WI4mwLLauATvlr3WdOdjhsLchQmRP3usez7SUrUKUz5MhcJ2r1rCdy6XT8");
    }

    @Override
    public boolean authTicket(String deviceId, String ticket) {
        return StringUtils.equals(ticketMap.get(deviceId), ticket);
    }
}
