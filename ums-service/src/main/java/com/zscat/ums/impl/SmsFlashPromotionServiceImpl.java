package com.zscat.ums.impl;

import com.github.pagehelper.PageHelper;
import com.zscat.ums.mapper.SmsFlashPromotionMapper;
import com.zscat.ums.model.SmsFlashPromotion;
import com.zscat.ums.model.SmsFlashPromotionExample;
import com.zscat.ums.service.SmsFlashPromotionService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 限时购活动管理Service实现类
 * Created by zscat on 2018/11/16.
 */
@Service("smsFlashPromotionService")
public class SmsFlashPromotionServiceImpl implements SmsFlashPromotionService {
    @Resource
    private SmsFlashPromotionMapper flashPromotionMapper;

    @Override
    public int create(SmsFlashPromotion flashPromotion) {
        flashPromotion.setCreateTime(new Date());
        return flashPromotionMapper.insert(flashPromotion);
    }

    @Override
    public int update(Long id, SmsFlashPromotion flashPromotion) {
        flashPromotion.setId(id);
        return flashPromotionMapper.updateByPrimaryKey(flashPromotion);
    }

    @Override
    public int delete(Long id) {
        return flashPromotionMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        SmsFlashPromotion flashPromotion = new SmsFlashPromotion();
        flashPromotion.setId(id);
        flashPromotion.setStatus(status);
        return flashPromotionMapper.updateByPrimaryKeySelective(flashPromotion);
    }

    @Override
    public SmsFlashPromotion getItem(Long id) {
        return flashPromotionMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SmsFlashPromotion> list(String keyword, Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        SmsFlashPromotionExample example = new SmsFlashPromotionExample();
        if (!StringUtils.isEmpty(keyword)) {
            example.createCriteria().andTitleLike("%" + keyword + "%");
        }
        return flashPromotionMapper.selectByExample(example);
    }

    @Override
    public List<SmsFlashPromotion> selectByExample(SmsFlashPromotionExample example){
        return flashPromotionMapper.selectByExample(example);
    }
}
