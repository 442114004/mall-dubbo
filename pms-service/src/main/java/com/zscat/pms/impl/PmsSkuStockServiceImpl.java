package com.zscat.pms.impl;


import com.zscat.pms.mapper.PmsSkuStockDao;
import com.zscat.pms.mapper.PmsSkuStockMapper;
import com.zscat.pms.model.PmsSkuStock;
import com.zscat.pms.model.PmsSkuStockExample;
import com.zscat.pms.service.PmsSkuStockService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * 商品sku库存管理Service实现类
 * Created by macro on 2018/4/27.
 */
@Service
public class PmsSkuStockServiceImpl implements PmsSkuStockService {
    @Resource
    private PmsSkuStockMapper skuStockMapper;
    @Resource
    private PmsSkuStockDao skuStockDao;

    @Override
    public List<PmsSkuStock> getList(Long pid, String keyword) {
        PmsSkuStockExample example = new PmsSkuStockExample();
        PmsSkuStockExample.Criteria criteria = example.createCriteria().andProductIdEqualTo(pid);
        if (!StringUtils.isEmpty(keyword)) {
            criteria.andSkuCodeLike("%" + keyword + "%");
        }
        return skuStockMapper.selectByExample(example);
    }

    @Override
    public int update(Long pid, List<PmsSkuStock> skuStockList) {
        return skuStockDao.replaceList(skuStockList);
    }

    @Override
    public  PmsSkuStock selectById(Long id){
        return skuStockMapper.selectByPrimaryKey(id);
    }
}
