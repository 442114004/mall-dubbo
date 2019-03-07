package com.zscat.ums.impl;

import com.zscat.ums.mapper.UmsMemberMapper;
import com.zscat.ums.model.UmsMember;
import com.zscat.ums.model.UmsMemberExample;
import com.zscat.ums.service.UmsMemberService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 会员管理Service实现类
 * Created by zscat on 2018/8/3.
 */
@Service("umsMemberService")
public class UmsMemberServiceImpl implements UmsMemberService {

    @Resource
    private UmsMemberMapper memberMapper;

    @Override
    public  int countByExample(UmsMemberExample example){
        return memberMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(UmsMemberExample example){
        return memberMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id){
        return memberMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(UmsMember record){
        return memberMapper.insert(record);
    }

    @Override
    public int insertSelective(UmsMember record){
        return memberMapper.insertSelective(record);
    }

    @Override
    public List<UmsMember> selectByExample(UmsMemberExample example){
        return memberMapper.selectByExample(example);
    }

    @Override
    public UmsMember selectByPrimaryKey(Long id){
        return memberMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective( UmsMember record,UmsMemberExample example){
        return memberMapper.updateByExampleSelective(record, example);
    }

    @Override
    public int updateByExample(UmsMember record,  UmsMemberExample example){
        return memberMapper.updateByExample(record, example);
    }

    @Override
    public int updateByPrimaryKeySelective(UmsMember record){
        return memberMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(UmsMember record){
        return memberMapper.updateByPrimaryKey(record);
    }

    @Override
    public UmsMember queryByOpenId(String openId){
        return memberMapper.queryByOpenId(openId);
    }

}
