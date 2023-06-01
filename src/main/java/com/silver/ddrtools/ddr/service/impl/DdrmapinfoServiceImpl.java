package com.silver.ddrtools.ddr.service.impl;

import com.silver.ddrtools.ddr.entity.DdrMapinfo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.silver.ddrtools.common.entity.SysEnums;
import com.silver.ddrtools.common.manager.MapManager;
import com.silver.ddrtools.common.service.SysEnumsService;
import com.silver.ddrtools.ddr.dao.DdrmapinfoMapper;
import com.silver.ddrtools.ddr.enums.DDrMapType;
import com.silver.ddrtools.ddr.service.DdrmapinfoService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class DdrmapinfoServiceImpl extends ServiceImpl<DdrmapinfoMapper, DdrMapinfo> implements DdrmapinfoService {

    @Resource
    private DdrmapinfoMapper ddrmapinfoMapper;


    @Resource
    private SysEnumsService sysEnumsService;

    @Override
    public int deleteByPrimaryKey(Long id) {
        return ddrmapinfoMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(DdrMapinfo record) {
        return ddrmapinfoMapper.insert(record);
    }

    @Override
    public int insertSelective(DdrMapinfo record) {
        return ddrmapinfoMapper.insertSelective(record);
    }

    @Override
    public DdrMapinfo selectByPrimaryKey(Long id) {
        return ddrmapinfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(DdrMapinfo record) {
        return ddrmapinfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(DdrMapinfo record) {
        return ddrmapinfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public void deleteAllData() {
        ddrmapinfoMapper.deleteAllData();
    }

    @Override
    public List<DdrMapinfo> getMaps(String type, String name) {
        AtomicReference<List<DdrMapinfo>> atomicDdrmapinfos = new AtomicReference<>();
        //是否有name
        if (name != null && !name.equals("")) {
            QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("mapname", name);
            atomicDdrmapinfos.set(ddrmapinfoMapper.selectList(queryWrapper));
        } else {
            //根据type获取枚举值
            DDrMapType.getEnumByType(type).ifPresent(ddrMapType -> {
                QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("maptype", ddrMapType.getEnName());
                queryWrapper.orderByAsc("difficulty");
                queryWrapper.orderByDesc("goodnum");
                queryWrapper.orderByAsc("mapname");
                atomicDdrmapinfos.set(ddrmapinfoMapper.selectList(queryWrapper));
            });
        }
        return atomicDdrmapinfos.get();
    }

    @Override
    public int fastPingJia(String type, String value, String name) {
        List<DdrMapinfo> ddrMapinfos = new ArrayList<>();
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("mapname", name);
        ddrMapinfos = ddrmapinfoMapper.selectList(queryWrapper);
        if (ddrMapinfos.size() > 0) {
            DdrMapinfo ddrmapinfo = ddrMapinfos.get(0);
            if (type.equals("love")) {
                ddrmapinfo.setGoodnum(ddrmapinfo.getGoodnum() + Integer.parseInt(value));
            } else {
                ddrmapinfo.setBadnum(ddrmapinfo.getBadnum() + Integer.parseInt(value));
            }
            return ddrMapinfos.size();
        }
        return 0;
    }



    @Override
    public List<SysEnums> getSkills() {
        List<SysEnums> ddr_skills = sysEnumsService.getEnumListByType("ddr_skills");
        return ddr_skills;
    }

    public void InitMapList() {
        List<DdrMapinfo> ddrMapinfos = new ArrayList<>();
        QueryWrapper<DdrMapinfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("mapname");
        ddrMapinfos = ddrmapinfoMapper.selectList(queryWrapper);

        for (DdrMapinfo ddrmapinfo : ddrMapinfos) {
            MapManager.getInstance().add(ddrmapinfo);
        }
    }
}

