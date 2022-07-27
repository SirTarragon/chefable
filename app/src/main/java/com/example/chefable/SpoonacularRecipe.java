package com.example.chefable;

import android.net.Uri;

public class SpoonacularRecipe {
    private int id;
    private String title;
    private String image;
    private String imageType;

    public SpoonacularRecipe(int i, String name, String im, String imType) {
        id = i;
        title = name;
        image = im;
        imageType = imType;
    }

    public int getID() {return id;}
    public String getTitle() {return title;}
    public String getImage() {return image;}
    public String getImageType() {return imageType;}
}
