package com.zscat.ums.service;

import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberReceiveAddress;

import java.util.List;

/**
 * 用户地址管理Service
 * Created by zscat on 2018/8/28.
 */
public interface UmsMemberReceiveAddressService {
    /**
     * 添加收货地址
     */
    int add(UmsMemberReceiveAddress address,UmsMember currentMember);

    /**
     * 删除收货地址
     *
     * @param id 地址表的id
     */
    int delete(Long id,UmsMember currentMember);

    /**
     * 修改收货地址
     *
     * @param id      地址表的id
     * @param address 修改的收货地址信息
     */
    int update(Long id, UmsMemberReceiveAddress address,UmsMember currentMember);

    /**
     * 返回当前用户的收货地址
     */
    List<UmsMemberReceiveAddress> list(UmsMember currentMember);

    /**
     * 获取地址详情
     *
     * @param id 地址id
     */
    UmsMemberReceiveAddress getItem(Long id,UmsMember currentMember);

    UmsMemberReceiveAddress getDefaultItem(UmsMember currentMember);

    int setDefault(Long id,UmsMember currentMember);
}
