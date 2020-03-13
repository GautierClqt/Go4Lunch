package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Workmates implements Parcelable {

    private String mId;
    private String mFirstName;
    private String mLastName;
    private String mEmail;
    private String mUrlPicture;

    public Workmates(String id, String firstName, String lastName, String email, String urlPicture) {
        this.mId = id;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.mUrlPicture = urlPicture;
    }

    public String getId() {
        return mId;
    }
    public String getFirstName() {
        return mFirstName;
    }
    public String getLastName() {
        return mLastName;
    }
    public String getEmail() {
        return mEmail;
    }
    public String getUrlPicture() {
        return mUrlPicture;
    }

    protected Workmates(Parcel in) {
        mId = in.readString();
        mFirstName = in.readString();
        mLastName = in.readString();
        mEmail = in.readString();
        mUrlPicture = in.readString();
    }

    public static final Creator<Workmates> CREATOR = new Creator<Workmates>() {
        @Override
        public Workmates createFromParcel(Parcel in) {
            return new Workmates(in);
        }

        @Override
        public Workmates[] newArray(int size) {
            return new Workmates[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mFirstName);
        dest.writeString(mLastName);
        dest.writeString(mEmail);
        dest.writeString(mUrlPicture);
    }
}
