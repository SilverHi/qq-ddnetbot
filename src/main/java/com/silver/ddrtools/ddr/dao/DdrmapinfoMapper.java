package com.silver.ddrtools.ddr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.ddr.entity.DdrMapinfo;
import org.apache.ibatis.annotations.Update;

public interface DdrmapinfoMapper extends BaseMapper<DdrMapinfo> {
    int deleteByPrimaryKey(Long id);

    int insert(DdrMapinfo record);

    int insertSelective(DdrMapinfo record);

    DdrMapinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrMapinfo record);

    int updateByPrimaryKey(DdrMapinfo record);

    @Update("TRUNCATE TABLE ddr_mapinfo")
    void deleteAllData();
}