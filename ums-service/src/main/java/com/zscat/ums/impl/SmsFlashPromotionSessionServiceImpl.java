package com.zscat.ums.impl;


import com.zscat.ums.mapper.SmsFlashPromotionSessionMapper;
import com.zscat.ums.model.SmsFlashPromotionSession;
import com.zscat.ums.model.SmsFlashPromotionSessionExample;
import com.zscat.ums.service.SmsFlashPromotionSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SmsFlashPromotionSessionServiceImpl implements SmsFlashPromotionSessionService{
    @Resource
    private SmsFlashPromotionSessionMapper flashPromotionSessionMapper;
    @Override
    public  int countByExample(SmsFlashPromotionSessionExample example){
        return flashPromotionSessionMapper.countByExample(example);
    }

    @Override
    public int deleteByExample(SmsFlashPromotionSessionExample example){
        return flashPromotionSessionMapper.deleteByExample(example);
    }

    @Override
    public int deleteByPrimaryKey(Long id){
        return flashPromotionSessionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SmsFlashPromotionSession record){
        return flashPromotionSessionMapper.insert(record);
    }

    @Override
    public int insertSelective(SmsFlashPromotionSession record){
        return flashPromotionSessionMapper.insertSelective(record);
    }

    @Override
    public List<SmsFlashPromotionSession> selectByExample(SmsFlashPromotionSessionExample example){
        return flashPromotionSessionMapper.selectByExample(example);
    }

    @Override
    public  SmsFlashPromotionSession selectByPrimaryKey(Long id){
        return flashPromotionSessionMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByExampleSelective(SmsFlashPromotionSession record, SmsFlashPromotionSessionExample example){
        return flashPromotionSessionMapper.updateByExampleSelective(record,example);
    }

    @Override
    public int updateByExample(SmsFlashPromotionSession record, SmsFlashPromotionSessionExample example){
        return flashPromotionSessionMapper.updateByExample(record,example);
    }

    @Override
    public int updateByPrimaryKeySelective(SmsFlashPromotionSession record){
        return flashPromotionSessionMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SmsFlashPromotionSession record){
        return flashPromotionSessionMapper.updateByPrimaryKey(record);
    }
}