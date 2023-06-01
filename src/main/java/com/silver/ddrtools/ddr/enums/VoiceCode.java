package com.silver.ddrtools.ddr.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@Getter
public enum VoiceCode {
    HiuGaai("HiuGaai", "HiuGaai - 粤语 (香港)", "zh-HK-HiuGaaiNeural"),
    HiuMaan("HiuMaan", "HiuMaan - 中文 (香港)", "zh-HK-HiuMaanNeural"),
    WanLung("WanLung", "WanLung - 中文 (香港)", "zh-HK-WanLungNeural"),
    Xiaoxiao("Xiaoxiao", "Xiaoxiao - 中文 (中国大陆)", "zh-CN-XiaoxiaoNeural"),
    Xiaoyi("Xiaoyi", "Xiaoyi - 中文 (中国大陆)", "zh-CN-XiaoyiNeural"),
    Yunjian("Yunjian", "Yunjian - 中文 (中国大陆)", "zh-CN-YunjianNeural"),
    Yunxi("Yunxi", "Yunxi - 中文 (中国大陆)", "zh-CN-YunxiNeural"),
    Yunxia("Yunxia", "Yunxia - 中文 (中国大陆)", "zh-CN-YunxiaNeural"),
    Yunyang("Yunyang", "Yunyang - 中文 (中国大陆)", "zh-CN-YunyangNeural"),
    Xiaobei("Xiaobei", "Xiaobei - 东北话 (中国大陆)", "zh-CN-liaoning-XiaobeiNeural"),
    HsiaoChen("HsiaoChen", "HsiaoChen - 中文 (台湾)", "zh-TW-HsiaoChenNeural"),
    YunJhe("YunJhe", "YunJhe - 中文 (台湾)", "zh-TW-YunJheNeural"),
    HsiaoYu("HsiaoYu", "HsiaoYu - 台湾普通话 (台湾)", "zh-TW-HsiaoYuNeural"),
    Xiaoni("Xiaoni", "Xiaoni - 陕西话 (中国大陆)", "zh-CN-shaanxi-XiaoniNeural"),
    Natasha("Natasha", "Natasha - 英语（澳大利亚）", "en-AU-NatashaNeural"),
    William("William", "William - 英语（澳大利亚）", "en-AU-WilliamNeural"),
    Clara("Clara", "Clara - 英语（加拿大）", "en-CA-ClaraNeural"),
    Liam("Liam", "Liam - 英语（加拿大）", "en-CA-LiamNeural"),
    Sam("Sam", "Sam - 英语（香港）", "en-HK-SamNeural"),
    Yan("Yan", "Yan - 英语（香港）", "en-HK-YanNeural"),
    NeerjaExpressive("NeerjaExpressive", "Neerja - 英语（印度）（预览版）", "en-IN-NeerjaExpressiveNeural"),
    Neerja("Neerja", "Neerja - 英语（印度）", "en-IN-NeerjaNeural"),
    Prabhat("Prabhat", "Prabhat - 英语（印度）", "en-IN-PrabhatNeural"),
    Connor("Connor", "Connor - 英语（爱尔兰）", "en-IE-ConnorNeural"),
    Emily("Emily", "Emily - 英语（爱尔兰）", "en-IE-EmilyNeural"),
    Asilia("Asilia", "Asilia - 英语（肯尼亚）", "en-KE-AsiliaNeural"),
    Chilemba("Chilemba", "Chilemba - 英语（肯尼亚）", "en-KE-ChilembaNeural"),
    Mitchell("Mitchell", "Mitchell - 英语（新西兰）", "en-NZ-MitchellNeural"),
    Molly("Molly", "Molly - 英语（新西兰）", "en-NZ-MollyNeural"),
    Abeo("Abeo", "Abeo - 英语（尼日利亚）", "en-NG-AbeoNeural"),
    Ezinne("Ezinne", "Ezinne - 英语（尼日利亚）", "en-NG-EzinneNeural"),
    James("James", "James - 英语（菲律宾）", "en-PH-JamesNeural"),
    Rosa("Rosa", "Rosa - 英语（菲律宾）", "en-PH-RosaNeural"),
    Luna("Luna", "Luna - 英语（新加坡）", "en-SG-LunaNeural"),
    Wayne("Wayne", "Wayne - 英语（新加坡）", "en-SG-WayneNeural"),
    Leah("Leah", "Leah - 英语（南非）", "en-ZA-LeahNeural"),
    Luke("Luke", "Luke - 英语（南非）", "en-ZA-LukeNeural"),
    Elimu("Elimu", "Elimu - 英语（坦桑尼亚）", "en-TZ-ElimuNeural"),
    Imani("Imani", "Imani - 英语（坦桑尼亚）", "en-TZ-ImaniNeural"),
    Libby("Libby", "Libby - 英语（英国）", "en-GB-LibbyNeural"),
    Maisie("Maisie", "Maisie - 英语（英国）", "en-GB-MaisieNeural"),
    Ryan("Ryan", "Ryan - 英语（英国）", "en-GB-RyanNeural"),
    Sonia("Sonia", "Sonia - 英语（英国）", "en-GB-SoniaNeural"),
    Thomas("Thomas", "Thomas - 英语（英国）", "en-GB-ThomasNeural"),
    Aria("Aria", "Aria - 英语（美国）", "en-US-AriaNeural"),
    Ana("Ana", "Ana - 英语（美国）", "en-US-AnaNeural"),
    Christopher("Christopher", "Christopher - 英语（美国）", "en-US-ChristopherNeural"),
    Eric("Eric", "Eric - 英语（美国）", "en-US-EricNeural"),
    Guy("Guy", "Guy - 英语（美国）", "en-US-GuyNeural"),
    Jenny("Jenny", "Jenny - 英语（美国）", "en-US-JennyNeural"),
    Michelle("Michelle", "Michelle - 英语（美国）", "en-US-MichelleNeural"),
    Roger("Roger", "Roger - 英语（美国）", "en-US-RogerNeural"),
    Steffan("Steffan", "Steffan - 英语（美国）", "en-US-SteffanNeural"),
    Keita("Keita", "Keita - 日语（日本）", "ja-JP-KeitaNeural"),
    Nanami("Nanami", "Nanami - 日语（日本）", "ja-JP-NanamiNeural");

    private final String name;
    private final String description;
    private final String code;

    VoiceCode(String name, String description, String code) {
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCode() {
        return code;
    }
    public static Optional<VoiceCode> getVoiceByName(String name) {
        for (VoiceCode voiceCode : VoiceCode.values()) {
            if (voiceCode.getName().equals(name)) {
                return Optional.of(voiceCode);
            }
        }
        return Optional.empty();
    }
}