package com.silver.ddrtools.ddr.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.silver.ddrtools.ddr.dao.DdrMapevaluateMapper;
import com.silver.ddrtools.ddr.entity.DdrMapevaluate;
import com.silver.ddrtools.ddr.service.DdrMapevaluateService;
@Service
public class DdrMapevaluateServiceImpl implements DdrMapevaluateService{

    @Resource
    private DdrMapevaluateMapper ddrMapevaluateMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ddrMapevaluateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DdrMapevaluate record) {
        return ddrMapevaluateMapper.insert(record);
    }

    @Override
    public int insertSelective(DdrMapevaluate record) {
        return ddrMapevaluateMapper.insertSelective(record);
    }

    @Override
    public DdrMapevaluate selectByPrimaryKey(Long id) {
        return ddrMapevaluateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DdrMapevaluate record) {
        return ddrMapevaluateMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DdrMapevaluate record) {
        return ddrMapevaluateMapper.updateByPrimaryKey(record);
    }

}
