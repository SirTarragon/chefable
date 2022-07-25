package com.example.chefable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity
{
    ListView itemsList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        itemsList = (ListView) findViewById(R.id.ItemsList);
    }

    public void IngredientList_Intent(View view) {
        Intent intent = new Intent(this, IngredientsCabinet.class);
        startActivity(intent);
    }

    public void Settings_Intent(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void Search_Action(View view) {
        itemsList.clearChoices();

    }

    public void AddFavorite_Action(View view) {
        itemsList.getSelectedItem();
    }

    public void ViewFavorites_Intent(View view) {
        Intent inten = new Intent(this, Favorites.class);
        startActivity(inten);
    }
}
