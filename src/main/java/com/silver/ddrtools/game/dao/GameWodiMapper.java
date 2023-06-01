package com.silver.ddrtools.game.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.silver.ddrtools.game.entity.GameWodi;

public interface GameWodiMapper  extends BaseMapper<GameWodi> {
    int deleteByPrimaryKey(Long id);

    int insert(GameWodi record);

    int insertSelective(GameWodi record);

    GameWodi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameWodi record);

    int updateByPrimaryKey(GameWodi record);
}