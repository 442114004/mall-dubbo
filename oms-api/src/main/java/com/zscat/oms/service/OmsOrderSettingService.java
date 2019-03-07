package com.zscat.oms.service;

import com.zscat.oms.model.OmsOrderSetting;

/**
 * 订单设置Service
 * Created by zscat on 2018/10/16.
 */
public interface OmsOrderSettingService {
    /**
     * 获取指定订单设置
     */
    OmsOrderSetting getItem(Long id);

    /**
     * 修改指定订单设置
     */
    int update(Long id, OmsOrderSetting orderSetting);
}
