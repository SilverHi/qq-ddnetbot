package com.silver.ddrtools.common.entity;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;

@Data
public class RequestBody {
    private String model;
    private double temperature;
    private JSONArray messages;

    public RequestBody(String model, double temperature, JSONArray messages) {
        this.model = model;
        this.temperature = temperature;
        this.messages = messages;
    }

    // getter and setter methods
}
