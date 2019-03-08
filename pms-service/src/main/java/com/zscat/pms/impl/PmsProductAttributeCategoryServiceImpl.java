package com.zscat.pms.impl;

import com.github.pagehelper.PageHelper;
import com.zscat.pms.dto.PmsProductAttributeCategoryItem;
import com.zscat.pms.mapper.PmsProductAttributeCategoryDao;
import com.zscat.pms.mapper.PmsProductAttributeCategoryMapper;
import com.zscat.pms.model.PmsProductAttributeCategory;
import com.zscat.pms.model.PmsProductAttributeCategoryExample;
import com.zscat.pms.service.PmsProductAttributeCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PmsProductAttributeCategoryService实现类
 * Created by zscat on 2018/4/26.
 */
@Service("pmsProductAttributeCategoryService")
public class PmsProductAttributeCategoryServiceImpl implements PmsProductAttributeCategoryService {
    @Resource
    private PmsProductAttributeCategoryMapper productAttributeCategoryMapper;
    @Resource
    private PmsProductAttributeCategoryDao productAttributeCategoryDao;

    @Override
    public int create(String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        return productAttributeCategoryMapper.insertSelective(productAttributeCategory);
    }

    @Override
    public int update(Long id, String name) {
        PmsProductAttributeCategory productAttributeCategory = new PmsProductAttributeCategory();
        productAttributeCategory.setName(name);
        productAttributeCategory.setId(id);
        return productAttributeCategoryMapper.updateByPrimaryKeySelective(productAttributeCategory);
    }

    @Override
    public int delete(Long id) {
        return productAttributeCategoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public PmsProductAttributeCategory getItem(Long id) {
        return productAttributeCategoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<PmsProductAttributeCategory> getList(Integer pageSize, Integer pageNum) {
        PageHelper.startPage(pageNum, pageSize);
        return productAttributeCategoryMapper.selectByExample(new PmsProductAttributeCategoryExample());
    }

    @Override
    public List<PmsProductAttributeCategoryItem> getListWithAttr() {
        return productAttributeCategoryDao.getListWithAttr();
    }
}
