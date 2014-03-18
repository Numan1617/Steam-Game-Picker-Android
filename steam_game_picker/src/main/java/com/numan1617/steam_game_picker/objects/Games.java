package com.numan1617.steam_game_picker.objects;

import org.json.JSONObject;

public class Games {

    private boolean hasCommunityVisibleStats;
    private String imgLogoUrl;
    private String imgIconUrl;
    private double playtimeForever;
    private double playtime2weeks;
    private int appid;
    private String name;


    public Games() {

    }

    public Games(JSONObject json) {

        this.hasCommunityVisibleStats = json.optBoolean("has_community_visible_stats");
        this.imgLogoUrl = json.optString("img_logo_url");
        this.imgIconUrl = json.optString("img_icon_url");
        this.playtimeForever = json.optDouble("playtime_forever");
        this.playtime2weeks = json.optDouble("playtime_2weeks");
        this.appid = json.optInt("appid");
        this.name = json.optString("name");

    }

    public boolean getHasCommunityVisibleStats() {
        return this.hasCommunityVisibleStats;
    }

    public void setHasCommunityVisibleStats(boolean hasCommunityVisibleStats) {
        this.hasCommunityVisibleStats = hasCommunityVisibleStats;
    }

    public String getImgLogoUrl() {
        return this.imgLogoUrl;
    }

    public void setImgLogoUrl(String imgLogoUrl) {
        this.imgLogoUrl = imgLogoUrl;
    }

    public String getImgIconUrl() {
        return this.imgIconUrl;
    }

    public void setImgIconUrl(String imgIconUrl) {
        this.imgIconUrl = imgIconUrl;
    }

    public double getPlaytimeForever() {
        return this.playtimeForever;
    }

    public void setPlaytimeForever(double playtimeForever) {
        this.playtimeForever = playtimeForever;
    }

    public double getPlaytime2weeks() {
        return this.playtime2weeks;
    }

    public void setPlaytime2weeks(double playtime2weeks) {
        this.playtime2weeks = playtime2weeks;
    }

    public int getAppid() {
        return this.appid;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
