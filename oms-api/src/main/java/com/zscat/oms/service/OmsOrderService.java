package com.zscat.oms.service;

import com.zscat.oms.dto.*;
import com.zscat.oms.model.OmsOrder;
import com.zscat.oms.model.OmsOrderExample;
import com.zscat.oms.model.OmsOrderItem;

import java.util.List;

/**
 * 订单管理Service
 * Created by zscat on 2018/10/11.
 */
public interface OmsOrderService {
    /**
     * 订单查询
     */
    List<OmsOrder> list(OmsOrderQueryParam queryParam, Integer pageSize, Integer pageNum);

    /**
     * 批量发货
     */

    int delivery(List<OmsOrderDeliveryParam> deliveryParamList);

    /**
     * 批量关闭订单
     */

    int close(List<Long> ids, String note);

    /**
     * 批量删除订单
     */
    int delete(List<Long> ids);

    /**
     * 获取指定订单详情
     */
    OmsOrderDetail detail(Long id);

    /**
     * 修改订单收货人信息
     */

    int updateReceiverInfo(OmsReceiverInfoParam receiverInfoParam);

    /**
     * 修改订单费用信息
     */

    int updateMoneyInfo(OmsMoneyInfoParam moneyInfoParam);

    /**
     * 修改订单备注
     */

    int updateNote(Long id, String note, Integer status);
    int updateAll(OmsOrder omsOrder);

    List<OmsOrder> selectByExample(OmsOrderExample example);

    OmsOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(OmsOrder record);

    int updateSkuStock(List<OmsOrderItem> orderItemList);

    /**
     * 获取超时订单
     *
     * @param minute 超时时间（分）
     */
    List<OmsOrderDetail> getTimeOutOrders(Integer minute);

    /**
     * 批量修改订单状态
     */
    int updateOrderStatus(List<Long> ids, Integer status);

    /**
     * 解除取消订单的库存锁定
     */
    int releaseSkuStockLock( List<OmsOrderItem> orderItemList);

    int insert(OmsOrder order);
}
