package com.silver.ddrtools.common.manager;
import java.util.List;
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


public class CmdListManager {

    private static CmdListManager instance;
    private Map<String, String> map;

    private CmdListManager() {
        map = new ConcurrentHashMap<>();
    }

    public static CmdListManager getInstance() {
        if (instance == null) {
            synchronized (CmdListManager.class) {
                if (instance == null) {
                    instance = new CmdListManager();
                }
            }
        }
        return instance;
    }

    public void put(String key, String value) {
        map.put(key, value);
    }

    public String get(String key) {
        return map.get(key);
    }

    public Set<String> getKeys(){
        return map.keySet();
    }
}