package com.silver.ddrtools.common.service.impl;

import com.silver.ddrtools.common.dao.SysCmdDao;
import com.silver.ddrtools.common.manager.CmdListManager;
import com.silver.ddrtools.common.service.SysCmdService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * (sys_cmd)表服务实现类
 *
 * @author sirmteam
 * @date 2022-12-28 17:05:19
 */
@Service
public class SysCmdServiceImpl implements SysCmdService {

    @Resource
    private SysCmdDao sysCmdDao;

    @Override
    public void initCmd() {
        sysCmdDao.selectList(null).forEach(cmd -> {
            CmdListManager.getInstance().put(cmd.getCmdName(), cmd.getClassName());
        });
    }
}

