package com.numan1617.steam_game_picker.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.numan1617.steam_game_picker.DatabaseHandler;
import com.numan1617.steam_game_picker.R;
import com.numan1617.steam_game_picker.objects.Games;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RandomGameSelection extends ActionBarActivity {

    private final String TAG = this.getClass().getSimpleName();
    private final int crossFadeDuration = 800;
    TextView nextGameName, view1, view2, view3;
    Timer flipIntervalSlowdownTimer;
    Timer flipFinishTimer;
    int flipInterval;
    Animation slideInTop;
    Animation slideOutBottom;
    private ArrayList<Games> steamProfileGames;
    private DatabaseHandler databaseHandler;
    private ViewFlipper flip;
    private RelativeLayout finalSelectedGameContainer;
    private ImageView finalSelectedGameImage;
    private TextView finalSelectedGameName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_game_selection);

        Intent intent = getIntent();
        Boolean includePlayed2Weeks = intent.getBooleanExtra("includePlayed2Weeks", false);
        Boolean includePlayedLifetime = intent.getBooleanExtra("includePlayedLifetime", false);

        databaseHandler = DatabaseHandler.getInstance(getApplicationContext());
        steamProfileGames = databaseHandler.getAllGames(includePlayed2Weeks, includePlayedLifetime);

        Log.v(TAG, "Game count: " + steamProfileGames.size());

        // Start flipping every 50 ms
        flipInterval = 50;

        flip = (ViewFlipper) findViewById(R.id.flip);
        view1 = (TextView) findViewById(R.id.view1);
        view2 = (TextView) findViewById(R.id.view2);
        view3 = (TextView) findViewById(R.id.view3);

        finalSelectedGameContainer = (RelativeLayout) findViewById(R.id.finalSelectedGameContainer);
        finalSelectedGameImage = (ImageView) findViewById(R.id.finalSelectedGameImage);
        finalSelectedGameName = (TextView) findViewById(R.id.finalSelectedGameName);

        view1.setText(getRandomGame().getName());
        view2.setText(getRandomGame().getName());
        view3.setText(getRandomGame().getName());

        nextGameName = view3;

        slideInTop = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        slideInTop.setDuration(flipInterval / 2);
        slideOutBottom = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        slideOutBottom.setDuration(flipInterval / 2);

        flip.setInAnimation(slideInTop);
        flip.setOutAnimation(slideOutBottom);

        slideInTop.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation animation) {
                Games randomGame = getRandomGame();

                nextGameName.setText(randomGame.getName());
                nextGameName.setTag(randomGame);

                if (nextGameName == view1) {
                    nextGameName = view2;
                } else if (nextGameName == view2) {
                    nextGameName = view3;
                } else if (nextGameName == view3) {
                    nextGameName = view1;
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
            }
        });

        //specify flipping interval
        flip.setFlipInterval(flipInterval);
        flip.startFlipping();

        flipIntervalSlowdownTimer = new Timer("FlipSlowdown");
        flipIntervalSlowdownTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                flipInterval += 5;
                flip.setFlipInterval(flipInterval);
                slideInTop.setDuration(flipInterval / 2);
                slideOutBottom.setDuration(flipInterval / 2);
            }
        }, 500, 100);


        flipFinishTimer = new Timer("FlipFinish");
        flipFinishTimer.scheduleAtFixedRate(new FlipFinishTimerTask(), 6000, 1000);
    }

    public Games getRandomGame() {
        Random r = new Random();
        int i1 = r.nextInt(steamProfileGames.size());

        return steamProfileGames.get(i1);
    }

    public void crossFadeViews(final View fadeOut, final View fadeIn) {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

            AlphaAnimation fadeOutAnimation = new AlphaAnimation(1.0f, 0.0f);
            fadeOutAnimation.setDuration(crossFadeDuration);
            fadeOutAnimation.setFillAfter(true);
            fadeOut.startAnimation(fadeOutAnimation);

            fadeIn.setVisibility(View.VISIBLE);
            AlphaAnimation fadeInAnimation = new AlphaAnimation(0.0f, 1.0f);
            fadeInAnimation.setDuration(crossFadeDuration);
            fadeInAnimation.setFillAfter(true);
            fadeIn.startAnimation(fadeInAnimation);
        } else {
            fadeOut.animate()
                    .alpha(0f)
                    .setDuration(crossFadeDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            fadeOut.setVisibility(View.GONE);
                        }
                    });

            fadeIn.setAlpha(0f);
            fadeIn.setVisibility(View.VISIBLE);

            fadeIn.animate()
                    .alpha(1f)
                    .setDuration(crossFadeDuration);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class FlipFinishTimerTask extends TimerTask {
        public void run() {
            if (flip.isFlipping()) {
                flip.stopFlipping();
            } else {
                flipIntervalSlowdownTimer.cancel();
                flipFinishTimer.cancel();

                Games finalSelectedGame = (Games) flip.getCurrentView().getTag();

                finalSelectedGameName.setText(finalSelectedGame.getName());

                if (finalSelectedGame.getImgIconUrl() != null && !finalSelectedGame.getImgLogoUrl().equals("") && finalSelectedGame.getAppid() != 0) {
                    final String gameLogoUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" + finalSelectedGame.getAppid() + "/" + finalSelectedGame.getImgLogoUrl() + ".jpg";

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ImageLoader.getInstance().displayImage(gameLogoUrl, finalSelectedGameImage);
                        }
                    });

                    finalSelectedGameImage.setVisibility(View.VISIBLE);
                } else {
                    finalSelectedGameImage.setVisibility(View.INVISIBLE);
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        crossFadeViews(flip, finalSelectedGameContainer);
                    }
                });
            }
        }
    }
}
