package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.OpDeviceRegisterMapper;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceRegister;
import com.geeker.marketing.dao.micro.generator.model.OpDeviceRegisterExample;
import com.geeker.marketing.service.AuthenticateService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Lubin.Xuan on 2018-03-28.
 * {desc}
 */
@Service
public class AuthenticateServiceImpl implements AuthenticateService {

    //private Map<String, String> ticketMap = new ConcurrentHashMap<>();

    @Resource
    private OpDeviceRegisterMapper opDeviceRegisterMapper;

/*    @PostConstruct
    private void init() {
        ticketMap.put("4c116ee17d14", "5DFQSJZJflO50ioGbaPtdMcuLP1YNeHjznF6WACg8WI4mwLLauATvlr3WdOdjhsLchQmRP3usez7SUrUKUz5MhcJ2r1rCdy6XT8");
    }*/

    @Override
    public boolean authTicket(String deviceId, String ticket) {
        OpDeviceRegisterExample example = new OpDeviceRegisterExample();
        example.createCriteria().andDeviceIdEqualTo(deviceId);
        example.createCriteria().andRegisterCodeEqualTo(ticket);
        List<OpDeviceRegister> opDeviceRegisters = opDeviceRegisterMapper.selectByExample(example);
        return null != opDeviceRegisters&&opDeviceRegisters.size()>0;
    }
}
