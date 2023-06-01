package com.silver.ddrtools.common.manager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;


/**
 * @ClassName CmdListManager
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 16:36
 * @Version 1.0
 **/


public class MapinfoManager {

    private static MapinfoManager instance;
    private Cache<String,String> map;

    private MapinfoManager() {
        map = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(Duration.ofSeconds(180)).build();
    }

    public static MapinfoManager getInstance() {
        if (instance == null) {
            synchronized (MapinfoManager.class) {
                if (instance == null) {
                    instance = new MapinfoManager();
                }
            }
        }
        return instance;
    }


    public Cache<String,String> getMap() {
        return map;
    }

}