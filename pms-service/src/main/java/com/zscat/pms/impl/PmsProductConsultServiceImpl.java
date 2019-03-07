package com.zscat.pms.impl;

import com.github.pagehelper.PageHelper;
import com.zscat.pms.mapper.PmsProductConsultMapper;
import com.zscat.pms.model.PmsProductConsult;
import com.zscat.pms.service.PmsProductConsultService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品品牌Service
 * Created by macro on 2018/4/26.
 */
@Service
public class PmsProductConsultServiceImpl implements PmsProductConsultService {

    @Resource
    private PmsProductConsultMapper productConsultMapper;
    @Override
    public  int create(PmsProductConsult record){
        return productConsultMapper.insert(record);
    }
    @Override
    @Transactional
    public int update(Long id, PmsProductConsult record){
        record.setId(id);
        return productConsultMapper.updateByPrimaryKeySelective(record);
    }
    @Override
    public int delete(Long id){
        return productConsultMapper.deleteByPrimaryKey(id);
    }
    @Override
    public int delete(List<Long> ids){
        return 0;
    }

    @Override
    public List<PmsProductConsult> list(PmsProductConsult record, int pageNum, int pageSize){
        PageHelper.startPage(pageNum, pageSize);
        return productConsultMapper.list(record);
    }

    @Override
    public PmsProductConsult get(Long id){
        return productConsultMapper.selectByPrimaryKey(id);
    }


}
