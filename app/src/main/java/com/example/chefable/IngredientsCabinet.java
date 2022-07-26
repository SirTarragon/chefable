package com.example.chefable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class IngredientsCabinet extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_cabinet);
    }

    public void Home_Intent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}