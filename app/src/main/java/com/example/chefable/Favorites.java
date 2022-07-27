package com.example.chefable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Favorites extends AppCompatActivity {
    ArrayList<Recipe> favoritesArrayList;
    ListView favorites_list;
    ArrayAdapter<Recipe> arrayAdapter;
    private Recipe selected_item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favorites_list = findViewById(R.id.list_favorites);
        favoritesArrayList = new ArrayList<>();
        Recipe test_recipe = new Recipe(663136, "Thai Pizza");
        favoritesArrayList.add(test_recipe);

        Query query = FirebaseDatabase.getInstance().getReference().child("Favorites");
        arrayAdapter = new ArrayAdapter<>(Favorites.this,
                android.R.layout.simple_list_item_1,
                favoritesArrayList);
        favorites_list.setAdapter(arrayAdapter);

        favorites_list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                selected_item = (Recipe) favorites_list.getItemAtPosition(position);
            }
        });

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot favorite_recipe : snapshot.getChildren()) {
                    favoritesArrayList.add(favorite_recipe.getValue(Recipe.class));
                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void Home_Intent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void RemoveFav_Action(View view) {
        if (selected_item != null) {
            favoritesArrayList.remove(selected_item);
            arrayAdapter.notifyDataSetChanged();
            Query query = FirebaseDatabase.getInstance().getReference().child("Favorites")
                    .orderByChild("ID").equalTo(selected_item.getID());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for(DataSnapshot removal : snapshot.getChildren())
                    {
                        removal.getRef().removeValue();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void ViewWeb_Intent(View view) {
        if (selected_item != null) {
            Log.d("ViewWeb", "Link: " + selected_item.getLink().toString());
            Bundle bundle = new Bundle();
            bundle.putString("link", selected_item.getLink().toString());
            Intent intent = new Intent(this, RecipeViewer.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
