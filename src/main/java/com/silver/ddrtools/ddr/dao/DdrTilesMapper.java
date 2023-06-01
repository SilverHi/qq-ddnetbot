package com.silver.ddrtools.ddr.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.ddr.entity.DdrTiles;

import org.apache.ibatis.annotations.Update;

public interface DdrTilesMapper extends BaseMapper<DdrTiles> {
    @Update("truncate table ddr_tiles")
    void deleteAllData();

    int deleteByPrimaryKey(Long id);

    int insert(DdrTiles record);

    int insertSelective(DdrTiles record);

    DdrTiles selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrTiles record);

    int updateByPrimaryKey(DdrTiles record);
}