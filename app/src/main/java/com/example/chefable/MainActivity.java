package com.example.chefable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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

        recipeArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                recipeArrayList);
        itemsList.setAdapter(arrayAdapter);

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
        ArrayList<String> ingredients = new ArrayList<>();

        // get ingredients stored in cabinet

        // get search string

        // set up API call

        // search with API and convert data to Recipe format

        // populate itemsList and recipeArrayList
    }

    public void AddFavorite_Action(View view) {
        Recipe item = (Recipe) itemsList.getSelectedItem();
        if (item != null) {
            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Favorites").orderByChild("ID").equalTo(item.getID());

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Toast.makeText(MainActivity.this,
                                "Selected recipe is already present in your favorites list",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseDatabase.getInstance().getReference().child("Favorites")
                                .push().setValue(item);

                        Toast.makeText(MainActivity.this,
                                "Selected recipe was added to your favorites list",
                                Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void ViewFavorites_Intent(View view) {
        Intent intent = new Intent(this, Favorites.class);
        startActivity(intent);
    }

    public void ViewWeb_Intent(View view) {
        Recipe item = (Recipe) itemsList.getSelectedItem();
        if (item != null) {
            Bundle bundle = new Bundle();
            bundle.putString("ARG_LINK", item.getLink().toString());
            Intent intent = new Intent(MainActivity.this, RecipeViewer.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
