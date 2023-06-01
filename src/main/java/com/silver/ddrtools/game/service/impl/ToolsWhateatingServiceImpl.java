package com.silver.ddrtools.game.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.enums.ResultCode;
import com.silver.ddrtools.common.service.SysSettingService;
import com.silver.ddrtools.common.util.ResultUtil;
import com.silver.ddrtools.ddr.entity.EatingRequset;
import com.silver.ddrtools.game.dao.ToolsWhateatingMapper;
import com.silver.ddrtools.game.entity.ToolsWhateating;
import com.silver.ddrtools.game.service.ToolsWhateatingService;

import java.io.File;
import java.io.IOException;

@Service
public class ToolsWhateatingServiceImpl implements ToolsWhateatingService{

    @Resource
    private SysSettingService sysSettingService;

    @Resource
    private ToolsWhateatingMapper toolsWhateatingMapper;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return toolsWhateatingMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(ToolsWhateating record) {
        return toolsWhateatingMapper.insert(record);
    }

    @Override
    public int insertSelective(ToolsWhateating record) {
        return toolsWhateatingMapper.insertSelective(record);
    }

    @Override
    public ToolsWhateating selectByPrimaryKey(Long id) {
        return toolsWhateatingMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(ToolsWhateating record) {
        return toolsWhateatingMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(ToolsWhateating record) {
        return toolsWhateatingMapper.updateByPrimaryKey(record);
    }

    @Override
    public ResultBody uploadEating(MultipartFile file, HttpServletRequest request) {
        //判断系统是windos还是linux
        String systemName = SystemRunTimeParam.systemName;
        String pathtype = "foodimgSavePath_linux";
        if (systemName.equals("windows")){
            pathtype = "foodimgSavePath_windows";
        }
        // 获取Path
        String path = sysSettingService.getSettingValue(pathtype);

        // 获取原始文件名
        String fileName = file.getOriginalFilename();
        // // 获取文件后缀
        String suffixName = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf(".")) : null;
        //判断格式是否为图片
        if (!suffixName.equals(".jpg") && !suffixName.equals(".png") && !suffixName.equals(".jpeg")){
            return ResultUtil.fail(ResultCode.IMG_TYPE_ERROR);
        }
        //判断文件大小是否超过5m
        if (file.getSize() > 5242880){
            return ResultUtil.fail(ResultCode.IMG_SIZE_ERROR);
        }

        //文件的保存重新按照时间戳命名
        String newFileName = String.valueOf(System.currentTimeMillis()) + suffixName;
        // 最终保存的文件
        File filePath = new File(path + File.separator + newFileName);
        // 判断文件夹是否存在
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        // 保存文件
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 如果成功，会返回文件的绝对路径，方便下载
        return ResultUtil.success(path + File.separator + fileName);

    }

    @Override
    public ResultBody uploadEatingData(EatingRequset formdata) {

        //判断数据库中是否已经存在该食物
        QueryWrapper<ToolsWhateating> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("foodName",formdata.getFoodName());
        ToolsWhateating toolsWhateating = toolsWhateatingMapper.selectOne(queryWrapper);
        if (toolsWhateating != null){
            return ResultUtil.fail(ResultCode.DATA_ALREADY_EXIST);
        }
        //保存数据
        ToolsWhateating toolsWhateating1 = new ToolsWhateating();
        toolsWhateating1.setFoodname(formdata.getFoodName());
        toolsWhateating1.setImgpath(formdata.getFilePath());
        toolsWhateatingMapper.insert(toolsWhateating1);
        return ResultUtil.success("已添加！");
    }

}
