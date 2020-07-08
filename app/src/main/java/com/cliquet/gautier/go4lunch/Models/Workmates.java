package com.cliquet.gautier.go4lunch.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Workmates implements Parcelable {

    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String urlPicture;
    private String selectedRestaurant;

    public Workmates() {}

    public Workmates(String id, String firstName, String lastName, String email, String urlPicture, String selectedRestaurant) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.urlPicture = urlPicture;
        this.selectedRestaurant = selectedRestaurant;
    }

    public String getId() {
        return id;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getEmail() {
        return email;
    }
    public String getUrlPicture() {
        return urlPicture;
    }
    public String getSelectedRestaurant() { return selectedRestaurant; }

    protected Workmates(Parcel in) {
        id = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        email = in.readString();
        urlPicture = in.readString();
        selectedRestaurant = in.readString();
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
        dest.writeString(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(email);
        dest.writeString(urlPicture);
        dest.writeString(selectedRestaurant);
    }
}
