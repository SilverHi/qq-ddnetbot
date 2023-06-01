package com.silver.ddrtools.common.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.common.dao.SysSettingMapper;
import com.silver.ddrtools.common.entity.SysSetting;
import com.silver.ddrtools.common.service.SysSettingService;
@Service
public class SysSettingServiceImpl implements SysSettingService {

    @Resource
    private SysSettingMapper sysSettingMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return sysSettingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(SysSetting record) {
        return sysSettingMapper.insert(record);
    }

    @Override
    public int insertSelective(SysSetting record) {
        return sysSettingMapper.insertSelective(record);
    }

    @Override
    public SysSetting selectByPrimaryKey(Long id) {
        return sysSettingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(SysSetting record) {
        return sysSettingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(SysSetting record) {
        return sysSettingMapper.updateByPrimaryKey(record);
    }

    @Override
    public String getSettingValue(String name) {
        QueryWrapper<SysSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        SysSetting sysSetting = sysSettingMapper.selectOne(queryWrapper);
        return sysSetting.getValue();
    }

    @Override
    public SysSetting getSettingByName(String name) {
        QueryWrapper<SysSetting> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);
        SysSetting sysSetting = sysSettingMapper.selectOne(queryWrapper);
        return sysSetting;
    }

}
