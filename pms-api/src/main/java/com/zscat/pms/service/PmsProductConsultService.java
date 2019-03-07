package com.zscat.pms.service;

import com.zscat.pms.model.PmsProductConsult;

import java.util.List;

/**
 * 商品品牌Service
 * Created by macro on 2018/4/26.
 */
public interface PmsProductConsultService {

    int create(PmsProductConsult record);


    int update(Long id, PmsProductConsult record);

    int delete(Long id);

    int delete(List<Long> ids);

    List<PmsProductConsult> list(PmsProductConsult record, int pageNum, int pageSize);

    PmsProductConsult get(Long id);


}
