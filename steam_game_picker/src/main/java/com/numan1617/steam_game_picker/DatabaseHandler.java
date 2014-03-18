package com.numan1617.steam_game_picker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.numan1617.steam_game_picker.objects.Games;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String KEY_COMMUNITY_STATS_AVAILABLE = "community_stats_available";
    private static final String KEY_IMAGE_ICON_URL = "image_icon_url";
    private static final String KEY_IMAGE_LOGO_URL = "image_logo_url";
    private static final String KEY_PLAYTIME_FOREVER = "playtime_forever";
    private static final String KEY_PLAYTIME_TWO_WEEKS = "playtime_two_weeks";
    private static final String KEY_STEAM_APP_ID = "steam_app_id";
    private static final String KEY_STEAM_APP_NAME = "steam_app_name";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "steam_game_picker.sqlite";
    private static final String TABLE_GAMES = "games";
    private static DatabaseHandler mInstance = null;

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHandler getInstance(Context ctx) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (mInstance == null) {
            mInstance = new DatabaseHandler(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public String getDatabaseName() {
        return DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createGamesTable = "CREATE  TABLE " + TABLE_GAMES + " ("
                + KEY_STEAM_APP_ID + " INTEGER PRIMARY KEY  NOT NULL  UNIQUE ,"
                + KEY_STEAM_APP_NAME + " TEXT,"
                + KEY_PLAYTIME_TWO_WEEKS + " REAL,"
                + KEY_PLAYTIME_FOREVER + " REAL,"
                + KEY_IMAGE_LOGO_URL + " TEXT,"
                + KEY_IMAGE_ICON_URL + " TEXT,"
                + KEY_COMMUNITY_STATS_AVAILABLE + " INTEGER"
                + ")";

        db.execSQL(createGamesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void saveGame(Games game) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_STEAM_APP_ID, game.getAppid());
        values.put(KEY_STEAM_APP_NAME, game.getName());
        values.put(KEY_PLAYTIME_TWO_WEEKS, game.getPlaytime2weeks());
        values.put(KEY_PLAYTIME_FOREVER, game.getPlaytimeForever());
        values.put(KEY_IMAGE_LOGO_URL, game.getImgLogoUrl());
        values.put(KEY_IMAGE_ICON_URL, game.getImgIconUrl());
        values.put(KEY_COMMUNITY_STATS_AVAILABLE, game.getHasCommunityVisibleStats());

        db.replace(TABLE_GAMES, null, values);
    }

    public void saveGames(ArrayList<Games> games) {
        for (Games game : games) {
            this.saveGame(game);
        }
    }

    public ArrayList<Games> getAllGames() {
        ArrayList<Games> gamesList = new ArrayList<Games>();

        String selectQuery = "SELECT " + KEY_STEAM_APP_NAME + ", " +
                KEY_IMAGE_ICON_URL + ", " +
                KEY_IMAGE_LOGO_URL + ", " +
                KEY_STEAM_APP_ID + " FROM " + TABLE_GAMES + " ORDER BY " + KEY_STEAM_APP_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Games game = new Games();
                game.setName(cursor.getString(0));
                game.setImgIconUrl(cursor.getString(1));
                game.setImgLogoUrl(cursor.getString(2));
                game.setAppid(cursor.getInt(3));

                gamesList.add(game);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return gamesList;
    }

    public ArrayList<Games> getAllGames(Boolean includePlayed2Weeks, Boolean includePlayedLifetime) {

        String whereIncludeLifetime;
        if (includePlayedLifetime) {
            whereIncludeLifetime = KEY_PLAYTIME_FOREVER + " >= 0";
        } else {
            whereIncludeLifetime = KEY_PLAYTIME_FOREVER + " <= 0";
        }

        String whereIncludePlayed2Weeks;
        if (includePlayedLifetime) {
            whereIncludePlayed2Weeks = KEY_PLAYTIME_TWO_WEEKS + " >= 0";
        } else {
            whereIncludePlayed2Weeks = KEY_PLAYTIME_TWO_WEEKS + " <= 0";
        }

        ArrayList<Games> gamesList = new ArrayList<Games>();

        String selectQuery = "SELECT " + KEY_STEAM_APP_NAME + ", " +
                KEY_IMAGE_ICON_URL + ", " +
                KEY_IMAGE_LOGO_URL + ", " +
                KEY_STEAM_APP_ID + " FROM " + TABLE_GAMES +
                " WHERE " + whereIncludeLifetime +
                " OR " + whereIncludePlayed2Weeks +
                " ORDER BY " + KEY_STEAM_APP_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Games game = new Games();
                game.setName(cursor.getString(0));
                game.setImgIconUrl(cursor.getString(1));
                game.setImgLogoUrl(cursor.getString(2));
                game.setAppid(cursor.getInt(3));

                gamesList.add(game);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return gamesList;
    }

    public void removeAllGames() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GAMES, null, null);
    }

}
