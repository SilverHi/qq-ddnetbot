package com.silver.ddrtools.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.common.entity.SysEnums;

public interface SysEnumsMapper extends BaseMapper<SysEnums> {
    int deleteByPrimaryKey(Long id);

    int insert(SysEnums record);

    int insertSelective(SysEnums record);

    SysEnums selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysEnums record);

    int updateByPrimaryKey(SysEnums record);
}