package com.zscat.pms.mapper;


import com.zscat.pms.model.PmsProductConsult;

import java.util.List;

public interface PmsProductConsultMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PmsProductConsult record);

    int insertSelective(PmsProductConsult record);

    PmsProductConsult selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(PmsProductConsult record);

    int updateByPrimaryKey(PmsProductConsult record);

    List<PmsProductConsult> list(PmsProductConsult record);
}