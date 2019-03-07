package com.zscat.oms.service;

import com.zscat.oms.model.OmsOrderItem;
import com.zscat.oms.model.OmsOrderItemExample;

import java.util.List;

public interface OmsOrderItemService {
    int countByExample(OmsOrderItemExample example);

    int deleteByExample(OmsOrderItemExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OmsOrderItem record);

    int insertSelective(OmsOrderItem record);

    List<OmsOrderItem> selectByExample(OmsOrderItemExample example);

    OmsOrderItem selectByPrimaryKey(Long id);

    int updateByExampleSelective(OmsOrderItem record, OmsOrderItemExample example);

    int updateByExample(OmsOrderItem record, OmsOrderItemExample example);

    int updateByPrimaryKeySelective(OmsOrderItem record);

    int updateByPrimaryKey(OmsOrderItem record);

    int insertList(List<OmsOrderItem> orderItemList);
}