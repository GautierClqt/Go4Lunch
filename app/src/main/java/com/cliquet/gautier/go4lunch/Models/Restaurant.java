package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

    private String mName;
    private String mAddress;
    private boolean mLiked;
    private boolean mSelected;
    private String mPhone;
    private String mWebsiteUrl;
    private String mPhotoReference;

    public Restaurant() {

    }

    public Restaurant(String name, String address, boolean liked, boolean selected, String phone, String websiteUrl, String photoReference) {
        this.mName = name;
        this.mAddress = address;
        this.mLiked = liked;
        this.mSelected = selected;
        this.mPhone = phone;
        this.mWebsiteUrl = websiteUrl;
        this.mPhotoReference = photoReference;
    }

    protected Restaurant(Parcel in) {
        mName = in.readString();
        mAddress = in.readString();
        mLiked = in.readByte() != 0;
        mSelected = in.readByte() != 0;
        mPhone = in.readString();
        mWebsiteUrl = in.readString();
        mPhotoReference = in.readString();
    }

    public static final Creator<Restaurant> CREATOR = new Creator<Restaurant>() {
        @Override
        public Restaurant createFromParcel(Parcel in) {
            return new Restaurant(in);
        }

        @Override
        public Restaurant[] newArray(int size) {
            return new Restaurant[size];
        }
    };

    public String getName() { return mName; }
    public String getAddress() { return mAddress; }
    public boolean getLiked() { return mLiked; }
    public boolean getSelected() { return mSelected; }
    public String getPhone() { return mPhone; }
    public String getUrl() { return mWebsiteUrl; }
    public String getPhotoReference() { return mPhotoReference; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mAddress);
        dest.writeByte((byte) (mLiked ? 1 : 0));
        dest.writeByte((byte) (mSelected ? 1 : 0));
        dest.writeString(mPhone);
        dest.writeString(mWebsiteUrl);
        dest.writeString(mPhotoReference);
    }
}
