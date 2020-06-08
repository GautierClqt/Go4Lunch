package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;

import java.util.List;

public class Restaurant implements Parcelable {

    private String mId;
    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private int numberOfWorkmates;
    private boolean mLikedByUser;
    private int mRating;
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

    public Restaurant(String id) {
        this.mId = id;
    }

    public Restaurant(String id, String name, double latitude, double longitude, String address, int numberOfWorkmates, boolean likedByUser, int rating, boolean selectedByUser, String phone, String websiteUrl, float distance, String photoReference, String openingHoursString) {
        this.mId = id;
        this.mName = name;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAddress = address;
        this.numberOfWorkmates = numberOfWorkmates;
        this.mLikedByUser = likedByUser;
        this.mRating = rating;
        this.mSelectedByUser = selectedByUser;
        this.mPhone = phone;
        this.mWebsiteUrl = websiteUrl;
        this.mDistance = distance;
        this.mPhotoReference = photoReference;
        this.mOpeningHoursString = openingHoursString;
    }

    protected Restaurant(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mAddress = in.readString();
        numberOfWorkmates = in.readInt();
        mLikedByUser = in.readByte() != 0;
        mRating = in.readInt();
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

    public String getId() { return mId; }
    public String getName() { return mName; }
    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }
    public String getAddress() { return mAddress; }
    public int getNumberOfWorkmates() { return numberOfWorkmates; }
    public boolean getLikedByUser() { return mLikedByUser; }
    public int getRating() { return mRating; }
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
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeDouble(mLatitude);
        dest.writeDouble(mLongitude);
        dest.writeString(mAddress);
        dest.writeInt(numberOfWorkmates);
        dest.writeByte((byte) (mLikedByUser ? 1 : 0));
        dest.writeInt(mRating);
        dest.writeByte((byte) (mSelectedByUser ? 1 : 0));
        dest.writeString(mPhone);
        dest.writeString(mWebsiteUrl);
        dest.writeFloat(mDistance);
        dest.writeString(mPhotoReference);
        dest.writeByte((byte) (mOpenNow ? 1 : 0));
        dest.writeString(mOpeningHoursString);
    }
}
