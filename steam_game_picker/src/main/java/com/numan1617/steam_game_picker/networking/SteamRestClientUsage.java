package com.numan1617.steam_game_picker.networking;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.numan1617.steam_game_picker.DatabaseHandler;
import com.numan1617.steam_game_picker.interfaces.SteamRestTaskListener;
import com.numan1617.steam_game_picker.objects.GamePicker;
import com.numan1617.steam_game_picker.objects.Games;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SteamRestClientUsage {

    private final String TAG = this.getClass().getSimpleName();
    private String STEAM_API_KEY;

    public SteamRestClientUsage(String steamApiKey) {
        STEAM_API_KEY = steamApiKey;
    }

    public void getOwnedGames(final Context context, final String steamId, final SteamRestTaskListener steamRestTaskListener) throws JSONException {
        Log.v(TAG, "Getting games list");

        RequestParams params = new RequestParams();
        params.put("key", STEAM_API_KEY);
        params.put("steamid", steamId);
        params.put("format", "json");
        params.put("include_appinfo", "1");
        params.put("include_played_free_games", "1");

        SteamRestClient.get("GetOwnedGames/v0001/", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                GamePicker gamePicker = new GamePicker(response);
                Log.v(TAG, "Number of returned games: " + gamePicker.getResponse().getGames().size());

                new SaveAllGames(context, steamRestTaskListener).execute(gamePicker.getResponse().getGames());
            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONArray errorResponse) {
                Log.e(TAG, "onFailure(int statusCode, Throwable e, JSONArray errorResponse)");

            }

            @Override
            public void onFailure(int statusCode, Throwable e, JSONObject errorResponse) {
                Log.e(TAG, "onFailure(int statusCode, Throwable e, JSONObject errorResponse)");

            }
        });
    }

    private class SaveAllGames extends AsyncTask<ArrayList<Games>, Void, Boolean> {

        private Context context;
        private SteamRestTaskListener steamRestTaskListener;

        public SaveAllGames(Context context, SteamRestTaskListener steamRestTaskListener) {
            this.context = context;
            this.steamRestTaskListener = steamRestTaskListener;
        }

        protected Boolean doInBackground(ArrayList<Games>... games) {
            DatabaseHandler db = DatabaseHandler.getInstance(context);
            db.removeAllGames();
            db.saveGames(games[0]);

            return true;
        }

        protected void onPostExecute(Boolean result) {
            steamRestTaskListener.onGetOwnedGamesComplete();
        }
    }

}
