package com.silver.ddrtools.common.util;

import com.alibaba.fastjson.JSONObject;
import com.silver.ddrtools.ddr.entity.*;
import com.silver.ddrtools.ddr.enums.DDrMapType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName DDrQueryUtil
 * @Description TODO
 * @Author Tsui
 * @Date 2022/12/28 20:18
 * @Version 1.0
 **/

public class DDrQueryUtil {
    public static void maps2Array(DDrPlayerInfo dDrPlayerInfo, JSONObject jsonObject) {
        TypesBean typesBean = new TypesBean();
        typesBean.setMaplist(new ArrayList<MapInfoBean>());
        JSONObject types = jsonObject.getJSONObject("types");
        //获取所有的key
        Set<String> keySet = types.keySet();
        //遍历key
        for (String maptype : keySet) {
            MapInfoBean mapInfoBean = new MapInfoBean();

            DDrMapType.getEnumByEnName(maptype).ifPresent(mapType -> {
                mapInfoBean.setSort(mapType.getSort());
            });
            mapInfoBean.setMaps(new ArrayList<>());
            mapInfoBean.setMap_type(maptype);
            JSONObject maptypejson = types.getJSONObject(maptype);

            JSONObject points = maptypejson.getJSONObject("points");
            RankBean pointsBean = new RankBean();
            pointsBean.setPoints(points.getInteger("points"));
            pointsBean.setTotal(points.getInteger("total"));
            pointsBean.setRank(points.getString("rank"));
            mapInfoBean.setPoints(pointsBean);


            JSONObject rank = maptypejson.getJSONObject("rank");
            RankBean rankBean = new RankBean();
            rankBean.setPoints(rank.getInteger("points"));
            rankBean.setTotal(rank.getInteger("total"));
            rankBean.setRank(rank.getString("rank"));
            mapInfoBean.setRank(rankBean);

            JSONObject team_rank = maptypejson.getJSONObject("team_rank");
            RankBean team_rankBean = new RankBean();
            team_rankBean.setPoints(team_rank.getInteger("points"));
            team_rankBean.setTotal(team_rank.getInteger("total"));
            team_rankBean.setRank(team_rank.getString("rank"));
            mapInfoBean.setTeam_rank(team_rankBean);

            JSONObject maps = maptypejson.getJSONObject("maps");

            //获取所有的key
            Set<String> mapkeySet = maps.keySet();
            //遍历key
            for (String mapname : mapkeySet) {
                MapsBean mapBean = new MapsBean();
                mapBean.setMap_name(mapname);
                JSONObject mapjson = maps.getJSONObject(mapname);
                mapBean.setTotal_finishes(mapjson.getInteger("total_finishes"));
                mapBean.setFinishes(mapjson.getInteger("finishes"));
                mapBean.setRank(mapjson.getInteger("rank"));
                mapBean.setTime(mapjson.getDouble("time"));
                mapBean.setTeam_rank(mapjson.getInteger("team_rank"));
                mapBean.setFirst_finish(mapjson.getInteger("first_finish"));
                mapBean.setPoints(mapjson.getInteger("points"));

                mapInfoBean.getMaps().add(mapBean);
            }
            typesBean.getMaplist().add(mapInfoBean);
        }
        Collections.sort(typesBean.getMaplist(), new Comparator<MapInfoBean>() {
            @Override
            public int compare(MapInfoBean o1, MapInfoBean o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        dDrPlayerInfo.setTypes(typesBean);
    }

    public static String translate(String repMessage) {
        List<DDrMapType> dDrMapTypes = Arrays.asList(DDrMapType.values());
        for (DDrMapType dDrMapType : dDrMapTypes) {
            repMessage = repMessage.replace(dDrMapType.getEnName(), dDrMapType.getZhName());
        }
        return repMessage;

    }
    public static String createMapTableHtml(List<DdrMapinfo> ddrMapsinfoList) {
        StringBuilder htmlbuilder = new StringBuilder();
        htmlbuilder.append("<td><table class=\"table\">\n" +
                "                    <thead>\n" +
                "                        <tr>\n" +
                "                            <th>地图名</th>\n" +
                "                            <th>难度</th>\n" +
                "                            <th>分数</th>\n" +
                "                        </tr>\n" +
                "                    </thead>\n" +
                "                    <tbody>\n");
        for (DdrMapinfo ddrMapsinfo : ddrMapsinfoList) {
            htmlbuilder.append("<tr>\n");
            htmlbuilder.append("<td>" + ddrMapsinfo.getMapname() + "</td>\n");

            String start = "";
            Integer difficulty = ddrMapsinfo.getDifficulty();
            for (Integer i = 0; i < difficulty; i++) {
                start += "★";
            }
            htmlbuilder.append("<td class = \"redtd\">" + (start.equals("")?"无星":start)+ "</td>\n");
            htmlbuilder.append("<td>" + ddrMapsinfo.getPoints() + "</td>\n");
            htmlbuilder.append("</tr>\n");
        }
        htmlbuilder.append("</tbody>\n");
        htmlbuilder.append("</table></td>\n");
        return htmlbuilder.toString();
    }

    public static String createFinishMapTableHtml(List<MapsBean> ddrMapsinfoList) {


        StringBuilder htmlbuilder = new StringBuilder();
        htmlbuilder.append("<td><table class=\"table\">\n" +
                "                    <thead>\n" +
                "                        <tr>\n" +
                "                            <th>地图名</th>\n" +
                "                            <th>分数</th>\n" +
                "                            <th>排名</th>\n" +
                "                            <th>队伍排名</th>\n" +
                "                            <th>用时</th>\n" +
                "                            <th>首次过图时间</th>\n" +
                "                            <th>完成次数</th>\n" +
                "                            <th>完成总人数</th>\n" +
                "                        </tr>\n" +
                "                    </thead>\n" +
                "                    <tbody>\n");
        for (MapsBean ddrMapsinfo : ddrMapsinfoList) {
            String finishconutColor = "";
            if (ddrMapsinfo.getFinishes() > 100) {
                finishconutColor = "class = \"bgredtd\"";
            }
            String rankColor = "class = \"trpadding\"";
            if (ddrMapsinfo.getRank() == 1) {
                rankColor = "class = \"trpadding bggoldtd\"";
            }else if (ddrMapsinfo.getRank() < 100){
                rankColor = "class = \"trpadding bgredtd\"";
            }

            String teamrankColor = "class = \"trpadding\"";
            if (ddrMapsinfo.getTeam_rank()!=null){
                if (ddrMapsinfo.getTeam_rank() == 1) {
                    teamrankColor = "class = \"trpadding bggoldtd\"";
                }else if (ddrMapsinfo.getTeam_rank() < 100){
                    teamrankColor = "class = \"trpadding bgredtd\"";
                }
            }
            htmlbuilder.append("<tr>\n");
            htmlbuilder.append("<td>" + ddrMapsinfo.getMap_name() + "</td>\n");
            htmlbuilder.append("<td>" + ddrMapsinfo.getPoints() + "</td>\n");
            htmlbuilder.append("<td "+rankColor+">" + ddrMapsinfo.getRank() + "</td>\n");
            if (ddrMapsinfo.getTeam_rank() == null) {
                htmlbuilder.append("<td>未排名</td>\n");
            } else {
                htmlbuilder.append("<td "+teamrankColor+">" + ddrMapsinfo.getTeam_rank() + "</td>\n");
            }
            htmlbuilder.append("<td class = \"trpadding\">" + TimeUtil.secondToTime(ddrMapsinfo.getTime()) + "</td>\n");
            htmlbuilder.append("<td class = \"trpadding\">" + TimeUtil.integerToDate(ddrMapsinfo.getFirst_finish()) + "</td>\n");
            htmlbuilder.append("<td "+finishconutColor+">"+ ddrMapsinfo.getFinishes() + "</td>\n");
            htmlbuilder.append("<td>" + ddrMapsinfo.getTotal_finishes() + "</td>\n");
            htmlbuilder.append("<td> </td>\n");
            htmlbuilder.append("</tr>\n");
        }
        htmlbuilder.append("</tbody>\n");
        htmlbuilder.append("</table></td>\n");
        return htmlbuilder.toString();
    }

    public static Map<String, Double> getSimilarity(String userInput, List<DdrMapinfo> list) {
        Map<String, Double> map = new HashMap<>();
        double maxSimilarity = 0;
        String maxSimilarityMapName = "";
        for (DdrMapinfo ddrmapinfo : list) {
            String mapName = ddrmapinfo.getMapname();
            double similarity = StringSimilar.getSimilarity(userInput, mapName);
            if (similarity > maxSimilarity) {
                maxSimilarity = similarity;
                maxSimilarityMapName = mapName;
            }
        }
        map.put(maxSimilarityMapName, maxSimilarity);
        return map;
    }

    public static String replaceHtml2LocalHtml(String html) {
        //汉化

        html = html.replaceAll("Global Ranks for", "玩家总览：");
        html = html.replaceAll("Points", "分数");
        html = html.replaceAll("Rank", "排名");
        html = html.replaceAll("Team ", "队伍");
        html = html.replaceAll("Unranked", "未排名");
        html = html.replaceAll("\\. with", "名");
        html = html.replaceAll("points", "分");

        html = html.replaceAll("past 365 days", "过去365天");
        html = html.replaceAll("past 30 days", "过去30天");
        html = html.replaceAll("past 7 days", "过去7天");
        html = html.replaceAll("Favorite Server", "最爱的服务器");
        html = html.replaceAll("First Finish", "首次过图");
        html = html.replaceAll("Last Finishes", "最近完成");
        html = html.replaceAll("Favorite Partners", "最爱的队友");
        html = html.replaceAll("on any server", "任意服务器");
        html = html.replaceAll("hours played in the ", "小时在");
        html = html.replaceAll("Player information is also available in JSON format\\.", "");
        html = html.replace("<img width=\"36\" src=\"/json.svg\"/>", "");


        html = html.replace("/localtime.js", "./res/localtime.js.下载");
        html = html.replace("/css.css?version=24", "./res/css.css");
        html = html.replace("/css-halloween.css?version=7", "./res/css-halloween.css");
        html = html.replace("/js.js", "./res/js.js.下载");
        html = html.replace("/ddnet2.svg", "./res/ddnet2.svg");
        html = html.replace("/ddnet.svg", "./res/ddnet.svg");
        html = html.replace("/jquery.js", "./res/jquery.js.下载");
        html = html.replace("/typeahead.bundle.js", "./res/typeahead.bundle.js.下载");
        html = html.replace("/playersearch.js?version=2", "./res/playersearch.js.下载");
        html = html.replace("/players-data/jquery.tablesorter.js", "./res/jquery.tablesorter.js.下载");
        html = html.replace("/players-data/sorter.js", "./res/sorter.js.下载");
        html = html.replace("/players-data/github_contribution.js", "./res/github_contribution.js.下载");
        html = html.replace("/players-data/github_contribution_graph.css", "./res/github_contribution_graph.css");
        html = html.replace("/players-data/css-sorter.css", "./res/css-sorter.css");
        html = html.replace("/countryflags/CHN.png", "./res/CHN.png");
//        html = html.replace("/json.svg","./res/json.svg");
        html = html.replace("/countryflags/CHN.png", "./res/CHN.png");
        html = html.replace("id=\"playersearch\"", "hidden id=\"playersearch\"");


        return html;
    }

}
