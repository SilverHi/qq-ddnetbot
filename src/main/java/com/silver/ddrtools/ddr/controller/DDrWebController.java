package com.silver.ddrtools.ddr.controller;


import com.silver.ddrtools.common.entity.ResultBody;
import com.silver.ddrtools.common.entity.SysEnums;
import com.silver.ddrtools.common.enums.ResultCode;
import com.silver.ddrtools.common.util.ResultUtil;
import com.silver.ddrtools.ddr.entity.DdrMapinfo;
import com.silver.ddrtools.ddr.entity.EatingRequset;
import com.silver.ddrtools.ddr.entity.EvaluateRequest;
import com.silver.ddrtools.ddr.service.DDrService;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;
import com.silver.ddrtools.game.service.ToolsWhateatingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * 控制层
 *
 * @author xxxxx
 */
@RestController
@RequestMapping("/ddr_maps")
public class DDrWebController {

    @Autowired
    private DdrmapinfoService ddrmapinfoService;

    @Autowired
    private ToolsWhateatingService toolsWhateatingService;

    @Autowired
    private DDrService dDrService;

    /**
     * 获取地图列表
     *
     * @return
     */
    @GetMapping("/getMaps")
    public ResultBody getMaps(@RequestParam("mapType") String type,@RequestParam("mapName") String name, HttpServletRequest request) {
        List<DdrMapinfo> maps = ddrmapinfoService.getMaps(type, name);
        return ResultUtil.success(maps);
    }

    @GetMapping("/getMapsInfo")
    public ResultBody getMapsInfo(@RequestParam("mapName") String name, HttpServletRequest request) {
        Map<String,String> maps = dDrService.getMapsInfoByName(name);
        //是否为空
        if(maps.isEmpty()){
            ResultUtil.fail(ResultCode.NOT_THIS_MAP);
        }
        return ResultUtil.success(maps);
    }

    @GetMapping("/getSkills")
    public ResultBody getSkills( HttpServletRequest request) {
        List<SysEnums> skills = ddrmapinfoService.getSkills();
        //是否为空
        if(skills.isEmpty()){
            return ResultUtil.fail(ResultCode.SYSTEM_ERROR);
        }
        return ResultUtil.success(skills);
    }

    @GetMapping("/queryplayer")
    public ResultBody queryplayer(@RequestParam("playername") String playername, HttpServletRequest request) {
        Map<String,String> playerInfo= null;
        try {
            playerInfo = dDrService.queryplayer(playername);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //是否为空
        if(playerInfo.isEmpty()){
            return ResultUtil.fail(ResultCode.NOT_THIS_PLAYER);
        }
        return ResultUtil.success(playerInfo);
    }



    @GetMapping("/fastpingjia")
    public ResultBody fastPingJia(@RequestParam("mapType") String type,@RequestParam("value") String value,@RequestParam("mapName") String name, HttpServletRequest request) {
        int i = ddrmapinfoService.fastPingJia(type,value, name);
        return ResultUtil.success("success");
    }


    /**
     * 上传吃什么图片
     *
     * @return 单条数据
     */
    @PostMapping( "/uploadEatingimg")
    public ResultBody uploadEating(@RequestBody MultipartFile file, HttpServletRequest request) {
        return toolsWhateatingService.uploadEating(file, request);
    }

    /**
     * 上传吃什么图片
     *
     * @return 单条数据
     */
    @PostMapping( "/uploadEating")
    public ResultBody uploadEatingdata(@RequestBody EatingRequset formdata, HttpServletRequest request) {
        return toolsWhateatingService.uploadEatingData(formdata);
    }

    /**
     * 评价
     *
     * @return 单条数据
     */
    @PostMapping( "/evaluateMap")
    public ResultBody evaluateData(@RequestBody EvaluateRequest evaluate, HttpServletRequest request) {
        return dDrService.evaluateMapFromWeb(evaluate);
    }


}
