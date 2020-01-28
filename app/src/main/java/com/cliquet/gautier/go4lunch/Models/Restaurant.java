package com.cliquet.gautier.go4lunch.Models;

public class Restaurant {

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

    public String getName() { return mName; }
    public String getAddress() { return mAddress; }
    public boolean getLiked() { return mLiked; }
    public boolean getSelected() { return mSelected; }
    public String getPhone() { return mPhone; }
    public String getUrl() { return mWebsiteUrl; }
    public String getPhotoReference() { return mPhotoReference; }
}
