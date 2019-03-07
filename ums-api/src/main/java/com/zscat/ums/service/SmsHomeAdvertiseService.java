package com.zscat.ums.service;

import com.zscat.ums.model.SmsHomeAdvertise;
import com.zscat.ums.model.SmsHomeAdvertiseExample;

import java.util.List;

/**
 * 首页广告管理Service
 * Created by zscat on 2018/11/7.
 */
public interface SmsHomeAdvertiseService {
    /**
     * 添加广告
     */
    int create(SmsHomeAdvertise advertise);

    /**
     * 批量删除广告
     */
    int delete(List<Long> ids);

    /**
     * 修改上、下线状态
     */
    int updateStatus(Long id, Integer status);

    /**
     * 获取广告详情
     */
    SmsHomeAdvertise getItem(Long id);

    /**
     * 更新广告
     */
    int update(Long id, SmsHomeAdvertise advertise);

    List<SmsHomeAdvertise> selectByExample(SmsHomeAdvertiseExample example);


    /**
     * 分页查询广告
     */
    List<SmsHomeAdvertise> list(String name, Integer type, String endTime, Integer pageSize, Integer pageNum);
}
