package com.zscat.mall.portal.service;


import com.zscat.common.result.CommonResult;
import com.zscat.oms.dto.ConfirmOrderResult;
import com.zscat.oms.dto.OrderParam;
import com.zscat.oms.dto.TbThanks;
import com.zscat.ums.model.UmsMember;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * 前台订单管理Service
 * Created by zscat on 2018/8/30.
 */
public interface OmsPortalOrderService {
    /**
     * 根据用户购物车信息生成确认单信息
     */
    ConfirmOrderResult generateConfirmOrder(UmsMember currentMember);

    /**
     * 根据提交信息生成订单
     */
    @Transactional
    CommonResult generateOrder(OrderParam orderParam,UmsMember currentMember);

    /**
     * 支付成功后的回调
     */
    @Transactional
    CommonResult paySuccess(Long orderId);

    /**
     * 自动取消超时订单
     */
    @Transactional
    CommonResult cancelTimeOutOrder();

    /**
     * 取消单个超时订单
     */
    @Transactional
    void cancelOrder(Long orderId);

    /**
     * 发送延迟消息取消订单
     */
    void sendDelayMessageCancelOrder(Long orderId);


    ConfirmOrderResult submitPreview(Map<String, Object> params,UmsMember currentMember);

    int payOrder(TbThanks tbThanks);
}
