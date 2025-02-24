package com.example.travelactivity.Models;

public class CategoryWisePlaces {
    String id, categoryName, placeName, placeImage, placeRating, placeDescription;

    public CategoryWisePlaces(String id, String categoryName, String placeName,
                              String placeImage, String placeRating,
                              String placeDescription) {

        this.id = id;
        this.categoryName = categoryName;
        this.placeName = placeName;
        this.placeImage = placeImage;
        this.placeRating = placeRating;
        this.placeDescription = placeDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getPlaceImage() {
        return placeImage;
    }

    public void setPlaceImage(String placeImage) {
        this.placeImage = placeImage;
    }

    public String getPlaceRating() {
        return placeRating;
    }

    public void setPlaceRating(String placeRating) {
        this.placeRating = placeRating;
    }


    public String getPlaceDescription() {
        return placeDescription;
    }

    public void setPlaceDescription(String placeDescription) {
        this.placeDescription = placeDescription;
    }
}
