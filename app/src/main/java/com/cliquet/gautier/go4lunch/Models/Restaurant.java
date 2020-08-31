package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Restaurant implements Parcelable {

    private String id;
    private String name;
    private double latitude;
    private double longitude;
    private String adress;
    private int numberOfWorkmates;
    private boolean likedByUser;
    private int rating;
    private boolean selectedByUser;
    private String phone;
    private String websiteUrl;
    private float distance;
    private String photoReference;
    private String openingHours;

    public Restaurant() {
    }

    public Restaurant(String id) {
        this.id = id;
    }

    public Restaurant(String id, String name, double latitude, double longitude, String address, int numberOfWorkmates, boolean likedByUser, int rating, boolean selectedByUser, String phone, String websiteUrl, float distance, String photoReference, String openingHoursString) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.adress = address;
        this.numberOfWorkmates = numberOfWorkmates;
        this.likedByUser = likedByUser;
        this.rating = rating;
        this.selectedByUser = selectedByUser;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.distance = distance;
        this.photoReference = photoReference;
        this.openingHours = openingHoursString;
    }

    protected Restaurant(Parcel in) {
        id = in.readString();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        adress = in.readString();
        numberOfWorkmates = in.readInt();
        likedByUser = in.readByte() != 0;
        rating = in.readInt();
        selectedByUser = in.readByte() != 0;
        phone = in.readString();
        websiteUrl = in.readString();
        distance = in.readFloat();
        photoReference = in.readString();
        openingHours = in.readString();
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

    public String getId() { return id; }
    public String getName() { return name; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getAdress() { return adress; }
    public int getNumberOfWorkmates() { return numberOfWorkmates; }
    public boolean getLikedByUser() { return likedByUser; }
    public int getRating() { return rating; }
    public boolean getSelectedByUser() { return selectedByUser; }
    public String getPhone() { return phone; }
    public String getWebsiteUrl() { return websiteUrl; }
    public float getDistance() { return distance; }
    public String getPhotoReference() { return photoReference; }
    public String getOpeningHours() { return openingHours; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(adress);
        dest.writeInt(numberOfWorkmates);
        dest.writeByte((byte) (likedByUser ? 1 : 0));
        dest.writeInt(rating);
        dest.writeByte((byte) (selectedByUser ? 1 : 0));
        dest.writeString(phone);
        dest.writeString(websiteUrl);
        dest.writeFloat(distance);
        dest.writeString(photoReference);
        dest.writeString(openingHours);
    }
}
