package com.example.travelactivity;

public class POJOGetAllCategoryDetails {
    //pojo = plain old java object
    //pojo multiple data get and set
    //reusability
    String id,categoryImage,categoryName;

    public POJOGetAllCategoryDetails(String sid, String scategoryImage, String scategoryName) {
        this.id = sid;
        this.categoryImage = scategoryImage;
        this.categoryName = scategoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
