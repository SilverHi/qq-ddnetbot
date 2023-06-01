package com.silver.ddrtools.common.manager;

import com.silver.ddrtools.ddr.entity.DdrMapinfo;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @ClassName MapManager
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 16:42
 * @Version 1.0
 **/

public class MapManager {

    private static MapManager instance;
    private List<DdrMapinfo> list;

    private MapManager() {
        list = new CopyOnWriteArrayList<>();
    }

    public static MapManager getInstance() {
        if (instance == null) {
            synchronized (MapManager.class) {
                if (instance == null) {
                    instance = new MapManager();
                }
            }
        }
        return instance;
    }

    public void add(DdrMapinfo value) {
        list.add(value);
    }

    public Object get(int index) {
        return list.get(index);
    }

    public List<DdrMapinfo> getList() {
        return list;
    }
}