package com.silver.ddrtools.ddr.enums;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public enum DDrMapType{
    Novice("Novice","简单",1),
    Moderate("Moderate","中阶",2),
    Brutal("Brutal","高阶",3),
    Insane("Insane","疯狂",4),
    Dummy("Dummy","分身",5),
    DDmaX_Easy("DDmaX.Easy","古典Easy",6),
    DDmaX_Next("DDmaX.Next","古典Next",7),
    DDmaX_Pro("DDmaX.Pro","古典Pro",8),
    DDmaX_Nut("DDmaX.Nut","古典Nut",9),
    Oldschool("Oldschool","传统",10),
    Solo("Solo","solo",11),
    Race("Race","竞速",12),
    Fun("Fun","娱乐",13);

    private String enName;
    private String zhName;
    private int sort;

    public static Optional<DDrMapType> getEnumByType(String type) {
        for (DDrMapType dDrMapType : DDrMapType.values()) {
            if (dDrMapType.getSort()==(Integer.parseInt(type))) {
                return Optional.of(dDrMapType);
            }
        }
        return Optional.empty();
    }

    public static String getZhNameByEnName(String enName) {
        for (DDrMapType dDrMapType : DDrMapType.values()) {
            if (dDrMapType.getEnName().equals(enName)) {
                return dDrMapType.getZhName();
            }
        }
        return null;
    }
    public static Optional<DDrMapType> getEnumByEnName(String enName) {
        for (DDrMapType dDrMapType : DDrMapType.values()) {
            if (dDrMapType.getEnName().equals(enName)) {
                return Optional.of(dDrMapType);
            }
        }
        return Optional.empty();
    }
    public static Optional<DDrMapType> getEnumByZhName(String zhName) {
        for (DDrMapType dDrMapType : DDrMapType.values()) {
            if (dDrMapType.getZhName().equals(zhName)) {
                return Optional.of(dDrMapType);
            }
        }
        return Optional.empty();
    }

}