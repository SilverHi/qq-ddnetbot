package com.silver.ddrtools.common.manager;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @ClassName CmdListManager
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 16:36
 * @Version 1.0
 **/


public class UserInfoManager {

    private static UserInfoManager instance;
    private Cache<String,String> map;

    private UserInfoManager() {
        map = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(Duration.ofSeconds(180)).build();
    }

    public static UserInfoManager getInstance() {
        if (instance == null) {
            synchronized (UserInfoManager.class) {
                if (instance == null) {
                    instance = new UserInfoManager();
                }
            }
        }
        return instance;
    }


    public Cache<String,String> getMap() {
        return map;
    }

}