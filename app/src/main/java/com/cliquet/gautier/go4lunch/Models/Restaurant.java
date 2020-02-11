package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.DetailsPojo;
import com.cliquet.gautier.go4lunch.Models.GoogleMapsApi.Pojo.NearbySearchPojo;

import java.util.List;

public class Restaurant implements Parcelable {

    private String mName;
    private double mLatitude;
    private double mLongitude;
    private String mAddress;
    private boolean mLiked;
    private boolean mSelected;
    private String mPhone;
    private String mWebsiteUrl;
    private String mPhotoReference;
    private List<DetailsPojo.Periods> mPeriods;

    public Restaurant() {

    }

    public Restaurant(String name, double latitude, double longitude, String address, boolean liked, boolean selected, String phone, String websiteUrl, String photoReference, List<DetailsPojo.Periods> periods) {
        this.mName = name;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mAddress = address;
        this.mLiked = liked;
        this.mSelected = selected;
        this.mPhone = phone;
        this.mWebsiteUrl = websiteUrl;
        this.mPhotoReference = photoReference;
        this.mPeriods = periods;
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
    public double getLatitude() { return mLatitude; }
    public double getLongitude() { return mLongitude; }
    public String getAddress() { return mAddress; }
    public boolean getLiked() { return mLiked; }
    public boolean getSelected() { return mSelected; }
    public String getPhone() { return mPhone; }
    public String getUrl() { return mWebsiteUrl; }
    public String getPhotoReference() { return mPhotoReference; }
    public List<DetailsPojo.Periods> getPeriods() { return mPeriods; }

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
