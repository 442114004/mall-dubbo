package com.zscat.ums.impl;

import com.zscat.ums.mapper.UmsMemberLevelMapper;
import com.zscat.ums.model.UmsMemberLevel;
import com.zscat.ums.model.UmsMemberLevelExample;
import com.zscat.ums.service.UmsMemberLevelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会员等级管理Service实现类
 * Created by zscat on 2018/4/26.
 */
@Service("umsMemberLevelService")
public class UmsMemberLevelServiceImpl implements UmsMemberLevelService {
    @Resource
    private UmsMemberLevelMapper memberLevelMapper;

    @Override
    public List<UmsMemberLevel> list(Integer defaultStatus) {
        UmsMemberLevelExample example = new UmsMemberLevelExample();
        example.createCriteria().andDefaultStatusEqualTo(defaultStatus);
        return memberLevelMapper.selectByExample(example);
    }

    @Override
    public List<UmsMemberLevel> selectByExample(UmsMemberLevelExample example){
        return memberLevelMapper.selectByExample(example);
    }
}
