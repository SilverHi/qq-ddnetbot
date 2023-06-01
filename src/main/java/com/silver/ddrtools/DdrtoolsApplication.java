package com.silver.ddrtools;

import com.silver.ddrtools.common.entity.SystemRunTimeParam;
import com.silver.ddrtools.common.service.impl.SysCmdServiceImpl;
import com.silver.ddrtools.common.util.SpringUtil;
import com.silver.ddrtools.ddr.service.impl.DdrmapinfoServiceImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.silver.ddrtools.*.dao")
public class DdrtoolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(DdrtoolsApplication.class, args);
        //判断系统是否是windows
        if (System.getProperty("os.name").toLowerCase().startsWith("win")) {
            SystemRunTimeParam.systemName = "windows";
        } else {
            SystemRunTimeParam.systemName = "linux";
        }

        //从数据库加载各种静态变量

        //加载地图名称
        SpringUtil.getBean(DdrmapinfoServiceImpl.class).InitMapList();
        //加载所有指令
        SpringUtil.getBean(SysCmdServiceImpl.class).initCmd();
    }

}
