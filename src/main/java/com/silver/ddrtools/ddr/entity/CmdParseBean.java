package com.silver.ddrtools.ddr.entity;

import com.silver.ddrtools.ddr.enums.DDrMapType;

import lombok.Data;

/**
 * @ClassName CmdParseBean
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 18:12
 * @Version 1.0
 **/
@Data
public class CmdParseBean {
    private String cmd;

    private String messageType;

    private Boolean queryUnfinishedMap;

    private Boolean isGoodMap;

    private Boolean isSpacePlayerName;
    private String groupId;
    private String userId;

    private String userInputWithoutCmd;
    private String playerName;

    private String mapName;

    private Boolean teamFlag = false;

    private Boolean showAllFlag = false;

    private Boolean queryOwner = true;

    private DDrMapType mapType;

    private Boolean chnFlag= false;
    private Object data;
}
