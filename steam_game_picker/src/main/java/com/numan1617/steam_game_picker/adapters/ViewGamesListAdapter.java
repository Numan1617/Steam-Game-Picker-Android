package com.numan1617.steam_game_picker.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.numan1617.steam_game_picker.R;
import com.numan1617.steam_game_picker.objects.Games;

import java.util.ArrayList;

public class ViewGamesListAdapter extends BaseAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private static LayoutInflater inflater = null;
    ArrayList<Games> data;
    private Activity activity;

    public ViewGamesListAdapter(Activity a, ArrayList<Games> d) {
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<Games> getData() {
        return data;
    }

    public void setData(ArrayList<Games> data) {
        this.data = data;
    }

    public int getCount() {
        return data.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.game_list_row, null);

            viewHolder = new ViewHolder();
            viewHolder.gameTitle = (TextView) convertView.findViewById(R.id.gameTitle);
            viewHolder.gameLogo = (ImageView) convertView.findViewById(R.id.gameLogo);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Games games = data.get(position);
        viewHolder.gameTitle.setText(games.getName());

        if (games.getImgIconUrl() != null && !games.getImgLogoUrl().equals("") && games.getAppid() != 0) {
            String gameLogoUrl = "http://media.steampowered.com/steamcommunity/public/images/apps/" + games.getAppid() + "/" + games.getImgLogoUrl() + ".jpg";
            ImageLoader.getInstance().displayImage(gameLogoUrl, viewHolder.gameLogo);
            viewHolder.gameLogo.setVisibility(View.VISIBLE);
        } else {
            viewHolder.gameLogo.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    static class ViewHolder {
        TextView gameTitle;
        ImageView gameLogo;
    }
}
