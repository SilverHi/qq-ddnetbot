package com.silver.ddrtools.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.common.entity.SysCmd;

public interface SysCmdDao extends BaseMapper<SysCmd> {
    int deleteByPrimaryKey(Long id);

    int insert(SysCmd record);

    int insertSelective(SysCmd record);

    SysCmd selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysCmd record);

    int updateByPrimaryKey(SysCmd record);
}