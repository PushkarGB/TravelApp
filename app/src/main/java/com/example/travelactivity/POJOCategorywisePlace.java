package com.example.travelactivity;

public class POJOCategorywisePlace {
    String id, categoryname, placename, placeimage , placerating, placeoffer, placediscription;

    public POJOCategorywisePlace(String id, String categoryname, String placename,
                                 String placeimage, String placerating,
                                 String placeoffer, String placediscription) {

        this.id = id;
        this.categoryname = categoryname;
        this.placename = placename;
        this.placeimage = placeimage;
        this.placerating = placerating;
        this.placeoffer = placeoffer;
        this.placediscription = placediscription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getPlacename() {
        return placename;
    }

    public void setPlacename(String placename) {
        this.placename = placename;
    }

    public String getPlaceimage() {
        return placeimage;
    }

    public void setPlaceimage(String placeimage) {
        this.placeimage = placeimage;
    }

    public String getPlacerating() {
        return placerating;
    }

    public void setPlacerating(String placerating) {
        this.placerating = placerating;
    }

    public String getPlaceoffer() {
        return placeoffer;
    }

    public void setPlaceoffer(String placeoffer) {
        this.placeoffer = placeoffer;
    }

    public String getPlacediscription() {
        return placediscription;
    }

    public void setPlacediscription(String placediscription) {
        this.placediscription = placediscription;
    }
}
