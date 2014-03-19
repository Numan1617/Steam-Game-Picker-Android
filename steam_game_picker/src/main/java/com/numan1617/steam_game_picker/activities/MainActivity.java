package com.numan1617.steam_game_picker.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.numan1617.steam_game_picker.R;
import com.numan1617.steam_game_picker.interfaces.SteamCommunityXMLTaskListener;
import com.numan1617.steam_game_picker.networking.SteamCommunityXmlClientUsage;
import com.numan1617.steam_game_picker.objects.SteamCommunityProfile;

import org.json.JSONException;

public class MainActivity extends ActionBarActivity implements SteamCommunityXMLTaskListener {

    private final String TAG = this.getClass().getSimpleName();
    private LinearLayout steamLoginContainer;
    private LinearLayout steamProfileContainer;
    private ImageView steamProfileImage;
    private TextView steamProfileName;
    private TextView steamProfileMemberSince;
    private EditText steamLoginUsername;
    private Button steamLoginButton;
    private LinearLayout memberSinceContainer;
    private SteamCommunityProfile selectedSteamCommunityProfile;
    private ProgressBar profileLoadingIndicator;
    private boolean profileContainerShown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate");

        setContentView(R.layout.activity_main);
        profileContainerShown = false;

        steamLoginContainer = (LinearLayout) findViewById(R.id.steamLoginContainer);
        steamProfileContainer = (LinearLayout) findViewById(R.id.steamProfileContainer);
        steamProfileImage = (ImageView) findViewById(R.id.steamProfileImage);
        steamProfileName = (TextView) findViewById(R.id.steamProfileName);
        steamProfileMemberSince = (TextView) findViewById(R.id.steamProfileMemberSince);
        steamLoginUsername = (EditText) findViewById(R.id.steamLoginUsername);
        steamLoginButton = (Button) findViewById(R.id.steamLoginButton);
        memberSinceContainer = (LinearLayout) findViewById(R.id.memberSinceContainer);
        profileLoadingIndicator = (ProgressBar) findViewById(R.id.profileLoadingIndicator);

        steamLoginUsername.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    steamLoginButtonClicked(steamLoginButton);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onFailure(FailureTypes error) {
        Log.v(TAG, "onFailure called " + error);
        if (error == FailureTypes.INVALID_PROFILE) {
            Toast.makeText(this, String.format(getString(R.string.error_getting_profile), steamLoginUsername.getText()), Toast.LENGTH_LONG).show();
        } else if (error == FailureTypes.CONNECTION_ERROR) {
            Toast.makeText(this, getString(R.string.error_connection), Toast.LENGTH_SHORT).show();
        } else if (error == FailureTypes.IO_EXCEPTION) {
            Toast.makeText(this, String.format(getString(R.string.error_reading_profile), steamLoginUsername.getText()), Toast.LENGTH_LONG).show();
        }
        profileLoadingIndicator.setVisibility(View.GONE);
        steamLoginContainer.setVisibility(View.VISIBLE);
    }

    public void onXMLParsed(SteamCommunityProfile steamCommunityProfile) {
        profileLoadingIndicator.setVisibility(View.GONE);

        if (steamCommunityProfile == null) {
            return;
        }
        selectedSteamCommunityProfile = steamCommunityProfile;

        ImageLoader.getInstance().displayImage(steamCommunityProfile.getAvatarFull(), steamProfileImage);
        steamProfileName.setText(steamCommunityProfile.getSteamId());

        if (steamCommunityProfile.getMemberSince() == null) {
            memberSinceContainer.setVisibility(View.GONE);
        } else {
            steamProfileMemberSince.setText(steamCommunityProfile.getMemberSince());
        }

        steamLoginContainer.setVisibility(View.GONE);
        steamProfileContainer.setVisibility(View.VISIBLE);
        profileContainerShown = true;
    }

    public void steamLoginButtonClicked(View v) {
        String userName = steamLoginUsername.getText().toString();
        if (!userName.equals("")) {
            steamLoginContainer.setVisibility(View.GONE);
            profileLoadingIndicator.setVisibility(View.VISIBLE);

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(steamLoginUsername.getWindowToken(), 0);

            try {
                Log.v(TAG, "Getting user details");
                new SteamCommunityXmlClientUsage().getSteamProfileDetails(userName, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void confirmProfileClicked(View v) {
        Intent loadVideoGamesList = new Intent(this, ViewGamesList.class);
        loadVideoGamesList.putExtra("steamCommunityProfile", selectedSteamCommunityProfile);

        startActivity(loadVideoGamesList);
    }

    public void rejectProfileClicked(View v) {
        hideSteamProfileContainer();
    }

    @Override
    public void onBackPressed() {
        Log.v(TAG, "onBackPressed");
        if (profileContainerShown) {
            hideSteamProfileContainer();
            return;
        }
        super.onBackPressed();
    }

    public void hideSteamProfileContainer() {
        steamLoginUsername.setText("");
        steamLoginContainer.setVisibility(View.VISIBLE);
        steamProfileContainer.setVisibility(View.GONE);
        profileContainerShown = false;
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

}
