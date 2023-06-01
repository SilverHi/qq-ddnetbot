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


public class UserJsonManager {

    private static UserJsonManager instance;
    private Cache<String,String> map;

    private UserJsonManager() {
        map = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(Duration.ofSeconds(180)).build();
    }

    public static UserJsonManager getInstance() {
        if (instance == null) {
            synchronized (UserJsonManager.class) {
                if (instance == null) {
                    instance = new UserJsonManager();
                }
            }
        }
        return instance;
    }


    public Cache<String,String> getMap() {
        return map;
    }

}