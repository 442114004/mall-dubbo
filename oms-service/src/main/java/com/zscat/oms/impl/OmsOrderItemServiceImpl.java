package com.zscat.oms.impl;

import com.zscat.oms.mapper.OmsOrderItemMapper;
import com.zscat.oms.mapper.PortalOrderItemDao;
import com.zscat.oms.model.OmsOrderItem;
import com.zscat.oms.model.OmsOrderItemExample;
import com.zscat.oms.service.OmsOrderItemService;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("omsOrderItemService")
public class OmsOrderItemServiceImpl implements OmsOrderItemService {
    @Resource
    private OmsOrderItemMapper orderItemMapper;
    @Resource
    private PortalOrderItemDao portalOrderItemDao;
    @Override
    public int countByExample(OmsOrderItemExample example){
        return orderItemMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(OmsOrderItemExample example){
        return orderItemMapper.countByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id){
        return orderItemMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(OmsOrderItem record){
        return orderItemMapper.insert(record);
    }

    @Override
    public int insertSelective(OmsOrderItem record){
        return orderItemMapper.insertSelective(record);
    }

    @Override
    public List<OmsOrderItem> selectByExample(OmsOrderItemExample example){
        return orderItemMapper.selectByExample(example);
    }

    @Override
    public OmsOrderItem selectByPrimaryKey(Long id){
        return orderItemMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(@Param("record") OmsOrderItem record, @Param("example") OmsOrderItemExample example){
        return orderItemMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(@Param("record") OmsOrderItem record, @Param("example") OmsOrderItemExample example){
        return orderItemMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(OmsOrderItem record){
        return orderItemMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(OmsOrderItem record){
        return orderItemMapper.updateByPrimaryKey(record);
    }
    @Override
    public int insertList(@Param("list") List<OmsOrderItem> list){
        return portalOrderItemDao.insertList(list);
    }
}