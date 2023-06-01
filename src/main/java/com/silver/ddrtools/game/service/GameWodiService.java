package com.silver.ddrtools.game.service;

import com.silver.ddrtools.game.entity.GameWodi;
public interface GameWodiService{


    int deleteByPrimaryKey(Long id);

    int insert(GameWodi record);

    int insertSelective(GameWodi record);

    GameWodi selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(GameWodi record);

    int updateByPrimaryKey(GameWodi record);

}
