package com.example.chefable;

import android.net.Uri;

public class Recipe {
    private int ID;
    private String title;
    private Uri link;

    public Recipe(int id, String name) {
        ID = id;
        title = name;
        link = generateLink(id, name);
    }

    public int getID() {return ID;}
    public String getTitle() {return title;}
    public Uri getLink() {return link;}

    public Uri generateLink(int id, String n) {
        StringBuilder builder = new StringBuilder();
        for(char c : n.toCharArray()) {
            if(Character.isLetter(c)) {
                builder.append(Character.toLowerCase(c));
            } else if(Character.isSpaceChar(c)) {
                builder.append('-');
            }
        }
        builder.append('-');
        String linkstring = builder.toString().concat(String.valueOf(id));
        return Uri.withAppendedPath(Uri.parse("https://spoonacular.com/recipes/"),linkstring);
    }
}
