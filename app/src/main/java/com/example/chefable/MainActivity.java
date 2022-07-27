package com.example.chefable;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Recipe> recipeArrayList;
    ListView itemsList;
    EditText input_search;
    ArrayAdapter<Recipe> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AndroidNetworking.initialize(getApplicationContext());

        itemsList = findViewById(R.id.ItemsList);
        input_search = findViewById(R.id.SearchBarField);

        recipeArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                recipeArrayList);
        itemsList.setAdapter(arrayAdapter);

    }

    public void IngredientList_Intent(View view) {
        Intent intent = new Intent(this, IngredientsCabinet.class);
        startActivity(intent);
    }

    public void Search_Action(View view) {
        itemsList.clearChoices();
        ArrayList<String> ingredients = new ArrayList<>();

        // get ingredients stored in cabinet
        Query query = FirebaseDatabase.getInstance().getReference().child("Ingredients");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot favorite_recipe : snapshot.getChildren()) {
                    ingredients.add(favorite_recipe.getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // setup search string
        String queryURL =
                "https://api.spoonacular.com/recipes/complexSearch?apiKey="+
                "74c012848d124ff5ac41b6a48f7723f2";

        String searchVal = input_search.getText().toString();

        if(searchVal.length() != 0) {
            queryURL.concat("&query="+searchVal.toLowerCase());
        }

        if(ingredients.size() != 0) {
            int pos = 0;
            queryURL.concat("&includeIngredients=");

            for(String ingredient : ingredients) {
                if(pos == 0) {
                    queryURL.concat(ingredient.toLowerCase());
                } else {
                    queryURL.concat(",+"+ingredient.toLowerCase());
                }
                pos++;
            }
        }

        queryURL.concat("&number=10");

        // set up API call

        // search with API and convert data to Recipe format

        // populate itemsList and recipeArrayList

        ANRequest reg = AndroidNetworking.get(queryURL).build();
        reg.getAsObjectList(SpoonacularRecipe.class, new ParsedRequestListener<List<SpoonacularRecipe>>() {
            @Override
            public void onResponse(List<SpoonacularRecipe> response) {
                for (SpoonacularRecipe recipe : response) {
                    recipeArrayList.add(new Recipe(recipe));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ANError anError) {

            }
        });
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
