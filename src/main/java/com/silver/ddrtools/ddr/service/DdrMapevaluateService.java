package com.silver.ddrtools.ddr.service;

import com.silver.ddrtools.ddr.entity.DdrMapevaluate;
public interface DdrMapevaluateService{


    int deleteByPrimaryKey(Long id);

    int insert(DdrMapevaluate record);

    int insertSelective(DdrMapevaluate record);

    DdrMapevaluate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrMapevaluate record);

    int updateByPrimaryKey(DdrMapevaluate record);

}
