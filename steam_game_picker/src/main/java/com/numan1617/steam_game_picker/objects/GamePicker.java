package com.numan1617.steam_game_picker.objects;

import org.json.JSONObject;

public class GamePicker {

    private Response response;


    public GamePicker() {

    }

    public GamePicker(JSONObject json) {

        this.response = new Response(json.optJSONObject("response"));

    }

    public Response getResponse() {
        return this.response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }


}
