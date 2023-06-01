package com.silver.ddrtools.common.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.common.entity.SysEnums;
import com.silver.ddrtools.common.dao.SysEnumsMapper;
import com.silver.ddrtools.common.service.SysEnumsService;

import java.util.List;

@Service
public class SysEnumsServiceImpl implements SysEnumsService{

    @Resource
    private SysEnumsMapper sysEnumsMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysEnumsMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysEnums record) {
        return sysEnumsMapper.insert(record);
    }

    @Override
    public int insertSelective(SysEnums record) {
        return sysEnumsMapper.insertSelective(record);
    }

    @Override
    public SysEnums selectByPrimaryKey(Long id) {
        return sysEnumsMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysEnums record) {
        return sysEnumsMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysEnums record) {
        return sysEnumsMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<SysEnums> getEnumListByType(String type) {
        QueryWrapper<SysEnums> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("type", type);

        List<SysEnums> sysEnums = sysEnumsMapper.selectList(queryWrapper);

        return sysEnums;
    }

}
