package com.zscat.ums.impl;

import com.zscat.ums.mapper.UmsIntegrationConsumeSettingMapper;
import com.zscat.ums.model.UmsIntegrationConsumeSetting;
import com.zscat.ums.service.UmsIntegrationConsumeSettingService;
import org.springframework.stereotype.Service;

@Service("umsIntegrationConsumeSettingService")
public class UmsIntegrationConsumeSettingServiceImpl implements UmsIntegrationConsumeSettingService {

    private UmsIntegrationConsumeSettingMapper integrationConsumeSettingMapper;
    @Override
    public  UmsIntegrationConsumeSetting selectById(Long id){
        return integrationConsumeSettingMapper.selectByPrimaryKey(id);
    }

}