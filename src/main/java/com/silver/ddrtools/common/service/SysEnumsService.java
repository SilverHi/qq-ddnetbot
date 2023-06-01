package com.silver.ddrtools.common.service;

import com.silver.ddrtools.common.entity.SysEnums;
import com.silver.ddrtools.common.entity.SysSetting;

import java.util.List;

public interface SysEnumsService{


    int deleteByPrimaryKey(Long id);

    int insert(SysEnums record);

    int insertSelective(SysEnums record);

    SysEnums selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysEnums record);

    int updateByPrimaryKey(SysEnums record);

    List<SysEnums> getEnumListByType(String name);
}
