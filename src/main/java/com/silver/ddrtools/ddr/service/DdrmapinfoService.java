package com.silver.ddrtools.ddr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.silver.ddrtools.common.entity.SysEnums;
import com.silver.ddrtools.ddr.entity.DdrMapinfo;

import java.util.List;

public interface DdrmapinfoService extends IService<DdrMapinfo> {


    int deleteByPrimaryKey(Long id);

    int insert(DdrMapinfo record);

    int insertSelective(DdrMapinfo record);

    DdrMapinfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DdrMapinfo record);

    int updateByPrimaryKey(DdrMapinfo record);

    void deleteAllData();

    List<DdrMapinfo> getMaps(String type, String name);

    int fastPingJia(String type, String value, String name);



    List<SysEnums> getSkills();
}

