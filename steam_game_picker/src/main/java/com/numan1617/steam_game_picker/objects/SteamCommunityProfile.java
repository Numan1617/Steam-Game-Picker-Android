package com.numan1617.steam_game_picker.objects;

import android.os.Parcel;
import android.os.Parcelable;

public class SteamCommunityProfile implements Parcelable {

    public static final Parcelable.Creator<SteamCommunityProfile> CREATOR = new Parcelable.Creator<SteamCommunityProfile>() {
        public SteamCommunityProfile createFromParcel(Parcel in) {
            return new SteamCommunityProfile(in);
        }

        public SteamCommunityProfile[] newArray(int size) {
            return new SteamCommunityProfile[size];
        }
    };
    private String steam64Id;
    private String avatarFull;
    private String memberSince;
    private String steamId;

    public SteamCommunityProfile() {

    }

    public SteamCommunityProfile(Parcel in) {
        steam64Id = in.readString();
        avatarFull = in.readString();
        memberSince = in.readString();
        steamId = in.readString();
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(steam64Id);
        out.writeString(avatarFull);
        out.writeString(memberSince);
        out.writeString(steamId);
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    public String getMemberSince() {
        return memberSince;
    }

    public void setMemberSince(String memberSince) {
        this.memberSince = memberSince;
    }

    public String getSteam64Id() {
        return steam64Id;
    }

    public void setSteam64Id(String steam64Id) {
        this.steam64Id = steam64Id;
    }

    public String getAvatarFull() {
        return avatarFull;
    }

    public void setAvatarFull(String avatarFull) {
        this.avatarFull = avatarFull;
    }
}