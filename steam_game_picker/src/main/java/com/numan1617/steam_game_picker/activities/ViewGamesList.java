package com.numan1617.steam_game_picker.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.numan1617.steam_game_picker.DatabaseHandler;
import com.numan1617.steam_game_picker.R;
import com.numan1617.steam_game_picker.adapters.ViewGamesListAdapter;
import com.numan1617.steam_game_picker.dialogs.GameSelectionDialog;
import com.numan1617.steam_game_picker.interfaces.SteamRestTaskListener;
import com.numan1617.steam_game_picker.networking.SteamRestClientUsage;
import com.numan1617.steam_game_picker.objects.Games;
import com.numan1617.steam_game_picker.objects.SteamCommunityProfile;

import org.json.JSONException;

import java.util.ArrayList;

public class ViewGamesList extends ActionBarActivity implements SteamRestTaskListener {

    private final String TAG = this.getClass().getSimpleName();
    private LinearLayout steamProfileContainer;
    private ImageView steamProfileImage;
    private TextView steamProfileName;
    private SteamCommunityProfile steamCommunityProfile;
    private ListView gamesListView;
    private DatabaseHandler databaseHandler;
    private ViewGamesListAdapter viewGamesListAdapter;
    private ArrayList<Games> steamProfileGames;
    private ProgressBar gamesListProgressBar;
    private TextView steamProfileMemberSince;
    private LinearLayout memberSinceContainer;
    private LinearLayout gameCountContainer;
    private TextView steamGameCount;
    private Button selectRandomGame;

    @Override
    public void onGetOwnedGamesComplete() {
        Log.v(TAG, "onGetOwnedGamesComplete");
        steamProfileGames = databaseHandler.getAllGames();

        if (viewGamesListAdapter == null) {
            viewGamesListAdapter = new ViewGamesListAdapter(this, steamProfileGames);
            gamesListView.setAdapter(viewGamesListAdapter);
            viewGamesListAdapter.notifyDataSetChanged();
        } else {
            viewGamesListAdapter.setData(steamProfileGames);
            viewGamesListAdapter.notifyDataSetChanged();
        }

        gamesListProgressBar.setVisibility(View.GONE);

        if (steamProfileGames.size() > 0) {
            selectRandomGame.setVisibility(View.VISIBLE);
            gamesListView.setVisibility(View.VISIBLE);
        } else {
            Log.v(TAG, "No steam games");
            TextView noSteamGames = (TextView) findViewById(R.id.noSteamGames);
            noSteamGames.setVisibility(View.VISIBLE);
        }

        steamGameCount.setText("" + steamProfileGames.size());
        gameCountContainer.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_games_list);

        Intent loadedIntent = getIntent();
        steamCommunityProfile = loadedIntent.getParcelableExtra("steamCommunityProfile");

        steamProfileContainer = (LinearLayout) findViewById(R.id.steamProfileContainer);
        steamProfileImage = (ImageView) findViewById(R.id.steamProfileImage);
        steamProfileName = (TextView) findViewById(R.id.steamProfileName);
        gamesListView = (ListView) findViewById(R.id.gamesListView);
        gamesListProgressBar = (ProgressBar) findViewById(R.id.gamesListProgressBar);
        steamProfileMemberSince = (TextView) findViewById(R.id.steamProfileMemberSince);
        memberSinceContainer = (LinearLayout) findViewById(R.id.memberSinceContainer);
        gameCountContainer = (LinearLayout) findViewById(R.id.gameCountContainer);
        steamGameCount = (TextView) findViewById(R.id.steamGameCount);
        selectRandomGame = (Button) findViewById(R.id.selectRandomGame);

        databaseHandler = DatabaseHandler.getInstance(getApplicationContext());

        steamProfileName.setText(steamCommunityProfile.getSteamId());
        ImageLoader.getInstance().displayImage(steamCommunityProfile.getAvatarFull(), steamProfileImage);

        if (steamCommunityProfile.getMemberSince() == null) {
            memberSinceContainer.setVisibility(View.GONE);
        } else {
            steamProfileMemberSince.setText(steamCommunityProfile.getMemberSince());
        }

        try {
            Log.v(TAG, "Getting user details");
            new SteamRestClientUsage(getString(R.string.steam_api_key)).getOwnedGames(this, steamCommunityProfile.getSteam64Id(), this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void randomGameClicked(View view) {
        GameSelectionDialog gameSelectionDialog = new GameSelectionDialog(this);
        gameSelectionDialog.show();

    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.v(TAG, "onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.v(TAG, "onSaveInstanceState");

        outState.putParcelable("steamCommunityProfile", steamCommunityProfile);
    }

    @Override
    public void onStart() {
        super.onStart();
        EasyTracker.getInstance(this).activityStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EasyTracker.getInstance(this).activityStop(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.view_games_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                performLogout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void performLogout() {
        SharedPreferences settings = getSharedPreferences("loginDetails", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();

        Intent loadMainActivity = new Intent(this, MainActivity.class);
        loadMainActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(loadMainActivity);
    }
}
