package com.numan1617.steam_game_picker.interfaces;

import com.numan1617.steam_game_picker.objects.SteamCommunityProfile;

public interface SteamCommunityXMLTaskListener {

    public enum FailureTypes {
        INVALID_PROFILE,
        CONNECTION_ERROR,
        IO_EXCEPTION
    }

    void onXMLParsed(SteamCommunityProfile steamCommunityProfile);

    void onFailure(FailureTypes error);

}
