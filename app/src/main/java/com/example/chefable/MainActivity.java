package com.example.chefable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Recipe> recipeArrayList;
    ListView itemsList;
    ArrayAdapter<Recipe> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        itemsList = findViewById(R.id.ItemsList);

        /*
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe item = (Recipe) itemsList.getSelectedItem();
                Bundle bundle = new Bundle();
                bundle.putString("ARG_LINK", item.getLink().toString());
                Intent intent = new Intent(MainActivity.this, RecipeViewer.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

         */
    }

    public void IngredientList_Intent(View view) {
        Intent intent = new Intent(this, IngredientsCabinet.class);
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

    public void ViewWeb_Intent(View view) {
        Recipe item = (Recipe) itemsList.getSelectedItem();
        Bundle bundle = new Bundle();
        bundle.putString("ARG_LINK", item.getLink().toString());
        Intent intent = new Intent(MainActivity.this, RecipeViewer.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
