package com.example.sj.keymeasures.algopuzzles.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class OjGoal implements Parcelable {
    private String me, rival;
    private String ojTag;
    private int goalNumber;
    /**
     * public default constructor needed for automatic loading into Firebase
     */
    public OjGoal() {
    }

    public String getMe() {
        return me;
    }

    public void setMe(String me) {
        this.me = me;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public String getOjTag() {
        return ojTag;
    }

    public void setOjTag(String ojTag) {
        this.ojTag = ojTag;
    }

    public int getGoalNumber() {
        return goalNumber;
    }

    public void setGoalNumber(int goalNumber) {
        this.goalNumber = goalNumber;
    }

    public OjGoal(String me, String rival, String ojTag, int goalNumber) {
        this.me = me;
        this.rival = rival;
        this.ojTag = ojTag;
        this.goalNumber = goalNumber;
    }

    public static final Parcelable.Creator<OjGoal> CREATOR= new Parcelable.Creator<OjGoal>() {
        @Override
        public OjGoal createFromParcel(Parcel parcel) {
            return new OjGoal(parcel);
        }
        @Override
        public OjGoal[] newArray(int size) {
            return new OjGoal[size];
        }
    };

    private OjGoal( Parcel in ) {
        me= in.readString();
        rival= in.readString();
        ojTag= in.readString();
        goalNumber= in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(me);
        parcel.writeString(rival);
        parcel.writeString(ojTag);
        parcel.writeInt(goalNumber);
    }
}

