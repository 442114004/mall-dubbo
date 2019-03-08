package com.zscat.oms.impl;


import com.zscat.oms.mapper.OmsOrderSettingMapper;
import com.zscat.oms.model.OmsOrderSetting;
import com.zscat.oms.service.OmsOrderSettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单设置管理Service实现类
 * Created by zscat on 2018/10/16.
 */
@Service("omsOrderSettingService")
public class OmsOrderSettingServiceImpl implements OmsOrderSettingService {
    @Resource
    private OmsOrderSettingMapper orderSettingMapper;

    @Override
    public OmsOrderSetting getItem(Long id) {
        return orderSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int update(Long id, OmsOrderSetting orderSetting) {
        orderSetting.setId(id);
        return orderSettingMapper.updateByPrimaryKey(orderSetting);
    }
}
