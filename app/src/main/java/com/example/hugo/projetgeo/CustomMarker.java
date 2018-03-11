package com.example.hugo.projetgeo;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jean-François on 06-03-18.
 */

public class CustomMarker {

    private int id;

    private String name;
    private float latitude;
    private float longitude;
    private int rating;
    private String owner;
    private String comments;


    public CustomMarker (int id, String name, float latitude, float longitude, int rating, String owner, String comments){
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.owner = owner;
        this.comments = comments;
    }

    public CustomMarker (String name, float latitude, float longitude, int rating, String owner, String comments){

        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = rating;
        this.owner = owner;
        this.comments = comments;
    }

    public CustomMarker (String name, float latitude, float longitude, String owner, String comments){
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rating = 0;
        this.owner = owner;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
