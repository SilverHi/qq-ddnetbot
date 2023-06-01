package com.silver.ddrtools.ddr.service;

import com.silver.ddrtools.common.manager.MapManager;
import com.silver.ddrtools.common.util.DDrQueryUtil;
import com.silver.ddrtools.ddr.entity.CmdParseBean;
import com.silver.ddrtools.ddr.exception.InterruptCmdNoReplyException;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName DDrBeasService
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/30 16:57
 * @Version 1.0
 **/
@Slf4j
public abstract class DDrBeasService extends CmdBeasService {
    public void checkMapNameAndReply(String userInputMapName, CmdParseBean cmdParseBean,String appendreplyMsg) throws InterruptCmdNoReplyException {

        Map<String, Double> similarity = DDrQueryUtil.getSimilarity(userInputMapName, MapManager.getInstance().getList());
        String mapName = similarity.keySet().iterator().next();
        Double similaritynum = similarity.get(mapName);
        //判断地图是否存在
        if (similaritynum == 100) {
            cmdParseBean.setMapName(mapName);
        }else {
            //相似度不为1
            reply(cmdParseBean,"未找到【"+userInputMapName+"】地图，你要找的是不是【" + mapName + "】？"+appendreplyMsg);
            throw new InterruptCmdNoReplyException("中断命令执行");
        }
    }

    public boolean checkMapName(String userInputMapName, CmdParseBean cmdParseBean) throws InterruptCmdNoReplyException {

        Map<String, Double> similarity = DDrQueryUtil.getSimilarity(userInputMapName, MapManager.getInstance().getList());
        String mapName = similarity.keySet().iterator().next();
        Double similaritynum = similarity.get(mapName);
        //判断地图是否存在
        if (similaritynum == 100) {
            cmdParseBean.setMapName(mapName);
            return true;
        }else {
            return false;
        }
    }

}
