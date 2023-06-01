package com.silver.ddrtools.common.service;

import com.silver.ddrtools.common.entity.SysSetting;
public interface SysSettingService{


    int deleteByPrimaryKey(Long id);

    int insert(SysSetting record);

    int insertSelective(SysSetting record);

    SysSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysSetting record);

    int updateByPrimaryKey(SysSetting record);

    String getSettingValue(String name);

    SysSetting getSettingByName(String name);

}
