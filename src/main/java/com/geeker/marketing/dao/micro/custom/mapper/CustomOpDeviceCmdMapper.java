package com.geeker.marketing.dao.micro.custom.mapper;

import com.geeker.marketing.dao.micro.generator.model.OpDeviceCmd;

import java.util.List;

public interface CustomOpDeviceCmdMapper {
    List<OpDeviceCmd> getUnDoCmd(OpDeviceCmd opDeviceCmd);
}