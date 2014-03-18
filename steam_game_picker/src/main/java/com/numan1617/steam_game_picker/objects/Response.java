package com.numan1617.steam_game_picker.objects;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Response {

    private ArrayList<Games> games;
    private double gameCount;


    public Response() {

    }

    public Response(JSONObject json) {


        this.games = new ArrayList<Games>();
        JSONArray arrayGames = json.optJSONArray("games");
        if (null != arrayGames) {
            int gamesLength = arrayGames.length();
            for (int i = 0; i < gamesLength; i++) {
                JSONObject item = arrayGames.optJSONObject(i);
                if (null != item) {
                    this.games.add(new Games(item));
                }
            }
        } else {
            JSONObject item = json.optJSONObject("games");
            if (null != item) {
                this.games.add(new Games(item));
            }
        }

        this.gameCount = json.optDouble("game_count");

    }

    public ArrayList<Games> getGames() {
        return this.games;
    }

    public void setGames(ArrayList<Games> games) {
        this.games = games;
    }

    public double getGameCount() {
        return this.gameCount;
    }

    public void setGameCount(double gameCount) {
        this.gameCount = gameCount;
    }


}
