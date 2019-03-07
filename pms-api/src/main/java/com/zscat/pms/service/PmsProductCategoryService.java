package com.zscat.pms.service;

import com.zscat.pms.dto.PmsProductCategoryParam;
import com.zscat.pms.dto.PmsProductCategoryWithChildrenItem;
import com.zscat.pms.model.PmsProductCategory;
import com.zscat.pms.model.PmsProductCategoryExample;

import java.util.List;

/**
 * 产品分类Service
 * Created by zscat on 2018/4/26.
 */
public interface PmsProductCategoryService {

    int create(PmsProductCategoryParam pmsProductCategoryParam);


    int update(Long id, PmsProductCategoryParam pmsProductCategoryParam);

    List<PmsProductCategory> getList(Long parentId, Integer pageSize, Integer pageNum);

    int delete(Long id);

    PmsProductCategory getItem(Long id);

    int updateNavStatus(List<Long> ids, Integer navStatus);

    int updateShowStatus(List<Long> ids, Integer showStatus);

    List<PmsProductCategoryWithChildrenItem> listWithChildren();

    List<PmsProductCategory> selectByExample(PmsProductCategoryExample example);
}
