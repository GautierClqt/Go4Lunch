package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.List;

public class Restaurant implements Parcelable {

    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private boolean mLikedByUser;
    private int mAllLikes;
    private boolean mSelectedByUser;
    private String mPhone;
    private String mWebsiteUrl;
    private float mDistance;
    private String mPhotoReference;
    private String mOpeningHoursString;
    private boolean mOpenNow;
    private List<DetailsPojo.Periods> mPeriods;

    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude, String address, boolean likedByUser, int allLikes, boolean selectedByUser, String phone, String websiteUrl, float distance, String photoReference, String openingHoursString) {
        this.mName = name;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAddress = address;
        this.mLikedByUser = likedByUser;
        this.mAllLikes = allLikes;
        this.mSelectedByUser = selectedByUser;
        this.mPhone = phone;
        this.mWebsiteUrl = websiteUrl;
        this.mDistance = distance;
        this.mPhotoReference = photoReference;
        this.mOpeningHoursString = openingHoursString;
    }

    protected Restaurant(Parcel in) {
        mName = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAddress = in.readString();
        mLikedByUser = in.readByte() != 0;
        mAllLikes = in.readInt();
        mSelectedByUser = in.readByte() != 0;
        mPhone = in.readString();
        mWebsiteUrl = in.readString();
        mDistance = in.readFloat();
        mPhotoReference = in.readString();
        mOpenNow = in.readByte() != 0;
        mOpeningHoursString = in.readString();
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
    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }
    public String getAddress() { return mAddress; }
    public boolean getLikedByUser() { return mLikedByUser; }
    public int getAllLikes() { return mAllLikes; }
    public boolean getSelectedByUser() { return mSelectedByUser; }
    public String getPhone() { return mPhone; }
    public String getWebsiteUrl() { return mWebsiteUrl; }
    public float getDistance() { return mDistance; }
    public String getPhotoReference() { return mPhotoReference; }
    public String getOpeningHoursString() { return mOpeningHoursString; }
    public boolean getOpenNow() { return mOpenNow; }
    public List<DetailsPojo.Periods> getPeriods() { return mPeriods; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mAddress);
        dest.writeByte((byte) (mLikedByUser ? 1 : 0));
        dest.writeInt(mAllLikes);
        dest.writeByte((byte) (mSelectedByUser ? 1 : 0));
        dest.writeString(mPhone);
        dest.writeString(mWebsiteUrl);
        dest.writeFloat(mDistance);
        dest.writeString(mPhotoReference);
        dest.writeByte((byte) (mOpenNow ? 1 : 0));
        dest.writeString(mOpeningHoursString);
    }
}
