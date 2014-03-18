package com.numan1617.steam_game_picker;

import android.util.Log;
import android.util.Xml;

import com.numan1617.steam_game_picker.objects.SteamCommunityProfile;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class SteamCommunityXmlParser {

    private static final String KEY_PROFILE = "profile";
    private static final String KEY_MEMBER_SINCE = "memberSince";
    private static final String KEY_AVATAR_FULL = "avatarFull";
    private static final String KEY_STEAM_64_ID = "steamID64";
    private static final String KEY_STEAM_ID = "steamID";
    private final String TAG = this.getClass().getSimpleName();

    public SteamCommunityProfile parse(InputStream in) throws XmlPullParserException, IOException {
        Log.v(TAG, "parse called");
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readProfile(parser);
        } finally {
            in.close();
        }
    }

    private SteamCommunityProfile readProfile(XmlPullParser parser) throws XmlPullParserException, IOException {
        Log.v(TAG, "readProfile called");
        parser.require(XmlPullParser.START_TAG, null, KEY_PROFILE);
        SteamCommunityProfile steamCommunityProfile = new SteamCommunityProfile();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals(KEY_STEAM_64_ID)) {
                steamCommunityProfile.setSteam64Id(readElement(parser, KEY_STEAM_64_ID));
            } else if (name.equals(KEY_AVATAR_FULL)) {
                steamCommunityProfile.setAvatarFull(readElement(parser, KEY_AVATAR_FULL));
            } else if (name.equals(KEY_MEMBER_SINCE)) {
                steamCommunityProfile.setMemberSince(readElement(parser, KEY_MEMBER_SINCE));
            } else if (name.equals(KEY_STEAM_ID)) {
                steamCommunityProfile.setSteamId(readElement(parser, KEY_STEAM_ID));
            } else {
                skip(parser);
            }
        }
        return steamCommunityProfile;
    }

    private String readElement(XmlPullParser parser, String elementName) throws IOException, XmlPullParserException {
        String result = "";

        parser.require(XmlPullParser.START_TAG, null, elementName);

        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        parser.require(XmlPullParser.END_TAG, null, elementName);

        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
