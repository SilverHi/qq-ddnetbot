package com.silver.ddrtools.ddr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.ddr.entity.DdrMapevaluate;

public interface DdrMapevaluateMapper extends BaseMapper<DdrMapevaluate> {
    int deleteByPrimaryKey(Long id);

    int insert(DdrMapevaluate record);

    int insertSelective(DdrMapevaluate record);

    DdrMapevaluate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrMapevaluate record);

    int updateByPrimaryKey(DdrMapevaluate record);
}