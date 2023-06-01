package com.silver.ddrtools.ddr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silver.ddrtools.ddr.entity.DdrTiles;
public interface DdrTilesService extends IService<DdrTiles> {


    int deleteByPrimaryKey(Long id);

    int insert(DdrTiles record);

    int insertSelective(DdrTiles record);

    DdrTiles selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrTiles record);

    int updateByPrimaryKey(DdrTiles record);

    void deleteAllData();

}
