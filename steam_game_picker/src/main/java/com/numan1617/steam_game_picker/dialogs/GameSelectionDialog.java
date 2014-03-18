package com.numan1617.steam_game_picker.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.numan1617.steam_game_picker.R;
import com.numan1617.steam_game_picker.activities.RandomGameSelection;

public class GameSelectionDialog extends AlertDialog implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private final String TAG = this.getClass().getSimpleName();
    private Context context;
    private CheckBox includePlayedLifetime;
    private CheckBox includePlayed2Weeks;
    private Button confirmButton;
    private Button cancelButton;

    public GameSelectionDialog(Context context) {
        super(context);

        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_game_selection);

        confirmButton = (Button) findViewById(R.id.confirmButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        includePlayedLifetime = (CheckBox) findViewById(R.id.includePlayedLifetime);
        includePlayed2Weeks = (CheckBox) findViewById(R.id.includePlayed2Weeks);

        includePlayedLifetime.setOnCheckedChangeListener(this);
        includePlayed2Weeks.setOnCheckedChangeListener(this);

        confirmButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view == confirmButton) {
            Intent i = new Intent(context, RandomGameSelection.class);
            i.putExtra("includePlayedLifetime", includePlayedLifetime.isChecked());
            i.putExtra("includePlayed2Weeks", includePlayed2Weeks.isChecked());
            context.startActivity(i);
        }

        this.dismiss();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        Log.v(TAG, "onCheckedChanged called");

        if (compoundButton == includePlayedLifetime) {
            Log.v(TAG, "includePlayedLifetime changed");

            if (includePlayedLifetime.isChecked()) {
                includePlayed2Weeks.setChecked(true);
            } else {
                includePlayed2Weeks.setChecked(false);
            }
        } else if (compoundButton == includePlayed2Weeks) {
            Log.v(TAG, "includePlayed2Weeks changed");

        }
    }
}
