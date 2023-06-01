package com.silver.ddrtools.ddr.entity;

import lombok.Data;

/**
 * @ClassName DDrPlayerInfo
 * @Description TODO
 * @Author silver
 * @Date 2022/11/19 22:30
 * @Version 1.0
 **/
@Data
public class DDrPlayerInfo {



    private String player;
    private PointsBean points;
    private TeamRankBean team_rank;
    private RankBean rank;
    private PointsLastMonthBean points_last_month;
    private PointsLastWeekBean points_last_week;
    private FirstFinishBean first_finish;
    private TypesBean types;
    private int hours_played_past_365_days;
    private java.util.List<LastFinishesBean> last_finishes;
    private java.util.List<FavoritePartnersBean> favorite_partners;
    private java.util.List<ActivityBean> activity;

    public static class PointsBean {
        /**
         * total : 29544
         * points : 1021
         * rank : 11654
         */

        private int total;
        private int points;
        private String rank;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class TeamRankBean {
        /**
         * rank : unranked
         */

        private String rank;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class RankBean {
        /**
         * rank : unranked
         */

        private String rank;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class PointsLastMonthBean {
        /**
         * points : 585
         * rank : 501
         */

        private int points;
        private String rank;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class PointsLastWeekBean {
        /**
         * points : 105
         * rank : 1116
         */

        private int points;
        private String rank;

        public int getPoints() {
            return points;
        }

        public void setPoints(int points) {
            this.points = points;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }
    }

    public static class FirstFinishBean {
        /**
         * timestamp : 1664513985
         * map : Sunny Side Up
         * time : 261.8
         */

        private int timestamp;
        private String map;
        private double time;

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getMap() {
            return map;
        }

        public void setMap(String map) {
            this.map = map;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }
}
