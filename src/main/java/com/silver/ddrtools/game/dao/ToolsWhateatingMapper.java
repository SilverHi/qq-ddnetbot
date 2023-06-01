package com.silver.ddrtools.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.game.entity.ToolsWhateating;

public interface ToolsWhateatingMapper extends BaseMapper<ToolsWhateating> {
    int deleteByPrimaryKey(Long id);

    int insert(ToolsWhateating record);

    int insertSelective(ToolsWhateating record);

    ToolsWhateating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ToolsWhateating record);

    int updateByPrimaryKey(ToolsWhateating record);

}