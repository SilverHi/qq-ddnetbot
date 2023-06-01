package com.silver.ddrtools.common.util;

import com.alibaba.druid.sql.visitor.functions.If;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @ClassName TimeUtil
 * @Description TODO
 * @Author silver
 * @Date 2022/12/1 14:51
 * @Version 1.0
 **/

public class TimeUtil {

    public static String secondToTime(Double time) {
        BigDecimal bd = new BigDecimal(time);
        bd = bd.setScale(3, RoundingMode.HALF_UP);
        int hour = bd.intValue() / 3600;
        bd = bd.subtract(new BigDecimal(hour * 3600));
        int minute = bd.intValue() / 60;
        bd = bd.subtract(new BigDecimal(minute * 60));
        int second = bd.intValue();
        BigDecimal milliBd = bd.multiply(new BigDecimal(1000));
        milliBd = milliBd.setScale(0, RoundingMode.HALF_UP);
        String hourStr = "";
        if(hour !=0){
            hourStr = hour < 10 ? "0" + hour : "" + hour;
            hourStr += ":";
        }
        String minuteStr = minute < 10 ? "0" + minute : "" + minute;
        String secondStr = second < 10 ? "0" + second : "" + second;
        String milliStr = milliBd.toPlainString();
        milliStr = milliStr.replaceAll("0*$", "");
        milliStr = milliStr.replaceAll("\\.$", "");
        return hourStr  + minuteStr + ":" + secondStr + "." + milliStr;
    }

    public static String integerToDate(Integer time){
        long millis = Long.valueOf(time) * 1000;
        Date date = new Date(millis);
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}
