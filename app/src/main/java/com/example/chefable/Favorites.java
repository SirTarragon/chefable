package com.example.chefable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity {
    ArrayList<Favorite> favoritesArrayList;
    ListView favorites_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        favorites_list = findViewById(R.id.list_favorites);
        favoritesArrayList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("Favorites");
        ArrayAdapter<Favorite> arrayAdapter = new ArrayAdapter<>(Favorites.this,
                android.R.layout.simple_list_item_1,
                favoritesArrayList);
        favorites_list.setAdapter(arrayAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot favorite_recipe : snapshot.getChildren()) {
                    favoritesArrayList.add(favorite_recipe.getValue(Favorite.class));
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
        Favorite item = (Favorite) favorites_list.getSelectedItem();
        if (item != null) {
            favoritesArrayList.remove(item);

            Query query = FirebaseDatabase.getInstance().getReference().child("Favorites")
                    .orderByChild("ID").equalTo(item.getID());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot removal : snapshot.getChildren()) {
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
        Favorite item = (Favorite) favorites_list.getSelectedItem();
        Bundle bundle = new Bundle();
        bundle.putString("ARG_LINK", item.getLink().toString());
        Intent intent = new Intent(this, RecipeViewer.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}