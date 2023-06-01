package com.silver.ddrtools.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.common.entity.SysSetting;

public interface SysSettingMapper extends BaseMapper<SysSetting> {
    int deleteByPrimaryKey(Long id);

    int insert(SysSetting record);

    int insertSelective(SysSetting record);

    SysSetting selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysSetting record);

    int updateByPrimaryKey(SysSetting record);

}