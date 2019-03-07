package com.zscat.ums.service;

import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberExample;

import java.util.List;

/**
 * 会员管理Service
 * Created by zscat on 2018/8/3.
 */
public interface UmsMemberService {

    int countByExample(UmsMemberExample example);

    int deleteByExample(UmsMemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UmsMember record);

    int insertSelective(UmsMember record);

    List<UmsMember> selectByExample(UmsMemberExample example);

    UmsMember selectByPrimaryKey(Long id);

    int updateByExampleSelective( UmsMember record,UmsMemberExample example);

    int updateByExample(UmsMember record,  UmsMemberExample example);

    int updateByPrimaryKeySelective(UmsMember record);

    int updateByPrimaryKey(UmsMember record);

    UmsMember queryByOpenId(String openId);
}
