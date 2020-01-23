package com.cliquet.gautier.go4lunch.Models.Pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photos {
    @SerializedName("photo_reference")
    @Expose
    private String photoReference;

    public String getPhotoReference() {
        return photoReference;
    }

    public void setPhotoReference(String photoReference) {
        this.photoReference = photoReference;
    }
}
