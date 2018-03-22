package com.geeker.marketing.service.impl;

import com.geeker.marketing.dao.micro.generator.mapper.MicroDeviceMapper;
import com.geeker.marketing.dao.micro.generator.model.MicroDevice;
import com.geeker.marketing.dao.micro.generator.model.MicroDeviceExample;
import com.geeker.marketing.service.MicroDeviceService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @Author TangZhen
* @Date 2018/2/2 0002 13:38
* @Description  快速发送设备业务
*/
@Service
public class MicroDeviceServiceImpl implements MicroDeviceService {
    @Resource
    private MicroDeviceMapper microDeviceMapper;
    /**
     * 设备列表
     * @param comId
     * @return
     */
    @Override
    public List<MicroDevice> getDeviceList(Integer comId) throws Exception {
        if(null==comId){
            throw new Exception("组织id不能为空！");
        }
        MicroDeviceExample example = new MicroDeviceExample();
        example.createCriteria().andComIdEqualTo(comId);
        example.setOrderByClause(" id desc");
        return microDeviceMapper.selectByExample(example);
    }

    @Override
    public MicroDevice getbyDeviceId(String deviceId) {
        return microDeviceMapper.selectByPrimaryKey(deviceId);
    }

    @Override
    public boolean isMicroDevice(String clientId) {
        return null != microDeviceMapper.selectByPrimaryKey(clientId);
    }
}
