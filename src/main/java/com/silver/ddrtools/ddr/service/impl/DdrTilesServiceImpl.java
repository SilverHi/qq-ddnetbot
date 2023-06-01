package com.silver.ddrtools.ddr.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silver.ddrtools.ddr.entity.DdrTiles;
import com.silver.ddrtools.ddr.dao.DdrTilesMapper;
import com.silver.ddrtools.ddr.service.DdrTilesService;
@Service
public class DdrTilesServiceImpl extends ServiceImpl<DdrTilesMapper,DdrTiles> implements DdrTilesService{

    @Resource
    private DdrTilesMapper ddrTilesMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ddrTilesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DdrTiles record) {
        return ddrTilesMapper.insert(record);
    }

    @Override
    public int insertSelective(DdrTiles record) {
        return ddrTilesMapper.insertSelective(record);
    }

    @Override
    public DdrTiles selectByPrimaryKey(Long id) {
        return ddrTilesMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DdrTiles record) {
        return ddrTilesMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DdrTiles record) {
        return ddrTilesMapper.updateByPrimaryKey(record);
    }

    public void deleteAllData() {
        ddrTilesMapper.deleteAllData();
    }
}
