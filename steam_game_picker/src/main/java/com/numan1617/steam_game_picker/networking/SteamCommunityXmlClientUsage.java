package com.numan1617.steam_game_picker.networking;

import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.numan1617.steam_game_picker.SteamCommunityXmlParser;
import com.numan1617.steam_game_picker.interfaces.SteamCommunityXMLTaskListener;
import com.numan1617.steam_game_picker.objects.SteamCommunityProfile;

import org.apache.http.Header;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class SteamCommunityXmlClientUsage {

    private final String TAG = this.getClass().getSimpleName();

    public void getSteamProfileDetails(String userName, final SteamCommunityXMLTaskListener listener) throws JSONException {
        Log.v(TAG, "Getting games list");

        RequestParams params = new RequestParams();
        params.put("xml", "1");

        try {
            userName = URLEncoder.encode(userName, "utf-8");
        } catch (UnsupportedEncodingException e) {
            listener.onFailure(SteamCommunityXMLTaskListener.FailureTypes.IO_EXCEPTION);
        }

        SteamCommunityXmlClient.get(userName, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                // Successfully got a response
                InputStream in = new ByteArrayInputStream(responseBody);

                SteamCommunityProfile steamCommunityProfile = null;
                try {
                    steamCommunityProfile = new SteamCommunityXmlParser().parse(in);
                    listener.onXMLParsed(steamCommunityProfile);
                } catch (XmlPullParserException e) {
                    listener.onFailure(SteamCommunityXMLTaskListener.FailureTypes.INVALID_PROFILE);
                } catch (IOException e) {
                    listener.onFailure(SteamCommunityXMLTaskListener.FailureTypes.IO_EXCEPTION);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.v(TAG, "Status code: " + statusCode);
                listener.onFailure(SteamCommunityXMLTaskListener.FailureTypes.INVALID_PROFILE.CONNECTION_ERROR);
            }
        });
    }

}
