package com.zscat.ums.service;

import com.zscat.ums.model.UmsMemberLevel;
import com.zscat.ums.model.UmsMemberLevelExample;

import java.util.List;

/**
 * 会员等级管理Service
 * Created by zscat on 2018/4/26.
 */
public interface UmsMemberLevelService {
    /**
     * 获取所有会员登录
     *
     * @param defaultStatus 是否为默认会员
     */
    List<UmsMemberLevel> list(Integer defaultStatus);
    List<UmsMemberLevel> selectByExample(UmsMemberLevelExample example);
}
