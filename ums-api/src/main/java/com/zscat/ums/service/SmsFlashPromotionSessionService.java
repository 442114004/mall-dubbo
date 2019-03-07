package com.zscat.ums.service;


import com.zscat.ums.model.SmsFlashPromotionSession;
import com.zscat.ums.model.SmsFlashPromotionSessionExample;

import java.util.List;

public interface SmsFlashPromotionSessionService {
    int countByExample(SmsFlashPromotionSessionExample example);

    int deleteByExample(SmsFlashPromotionSessionExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SmsFlashPromotionSession record);

    int insertSelective(SmsFlashPromotionSession record);

    List<SmsFlashPromotionSession> selectByExample(SmsFlashPromotionSessionExample example);

    SmsFlashPromotionSession selectByPrimaryKey(Long id);

    int updateByExampleSelective(SmsFlashPromotionSession record, SmsFlashPromotionSessionExample example);

    int updateByExample(SmsFlashPromotionSession record, SmsFlashPromotionSessionExample example);

    int updateByPrimaryKeySelective(SmsFlashPromotionSession record);

    int updateByPrimaryKey(SmsFlashPromotionSession record);
}