package com.zscat.mall.portal.service;


import com.zscat.mall.portal.entity.MemberReadHistory;

import java.util.List;

/**
 * 会员浏览记录管理Service
 * Created by zscat on 2018/8/3.
 */
public interface MemberReadHistoryService {
    /**
     * 生成浏览记录
     */
    int create(MemberReadHistory memberReadHistory);

    /**
     * 批量删除浏览记录
     */
    int delete(List<String> ids);

    /**
     * 获取用户浏览历史记录
     */
    List<MemberReadHistory> list(Long memberId);
}
