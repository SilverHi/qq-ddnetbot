package com.silver.ddrtools.game.service.impl;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import com.silver.ddrtools.game.entity.GameWodi;
import com.silver.ddrtools.game.dao.GameWodiMapper;
import com.silver.ddrtools.game.service.GameWodiService;
@Service
public class GameWodiServiceImpl implements GameWodiService{

    @Resource
    private GameWodiMapper gameWodiMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return gameWodiMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(GameWodi record) {
        return gameWodiMapper.insert(record);
    }

    @Override
    public int insertSelective(GameWodi record) {
        return gameWodiMapper.insertSelective(record);
    }

    @Override
    public GameWodi selectByPrimaryKey(Long id) {
        return gameWodiMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(GameWodi record) {
        return gameWodiMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(GameWodi record) {
        return gameWodiMapper.updateByPrimaryKey(record);
    }

}
