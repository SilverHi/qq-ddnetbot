package com.silver.ddrtools.game.service;

import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.ddr.entity.EatingRequset;
import com.silver.ddrtools.game.entity.ToolsWhateating;

import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ToolsWhateatingService{


    int deleteByPrimaryKey(Long id);

    int insert(ToolsWhateating record);

    int insertSelective(ToolsWhateating record);

    ToolsWhateating selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ToolsWhateating record);

    int updateByPrimaryKey(ToolsWhateating record);

    ResultBody uploadEating(MultipartFile file, HttpServletRequest request);

    ResultBody uploadEatingData(EatingRequset formdata);

}
