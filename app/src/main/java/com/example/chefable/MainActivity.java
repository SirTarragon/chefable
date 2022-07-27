package com.example.chefable;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.ANRequest;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.AnalyticsListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class MainActivity extends AppCompatActivity
{
    ArrayList<Recipe> recipeArrayList;
    ListView itemsList;
    EditText input_search;
    ArrayAdapter<Recipe> arrayAdapter;
    ArrayList<String> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        AndroidNetworking.initialize(getApplicationContext());

        itemsList = findViewById(R.id.ItemsList);
        input_search = findViewById(R.id.SearchBarField);

        recipeArrayList = new ArrayList<>();
        ingredients = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1,
                recipeArrayList);
        itemsList.setAdapter(arrayAdapter);

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

    }

    public void IngredientList_Intent(View view) {
        Intent intent = new Intent(this, IngredientsCabinet.class);
        startActivity(intent);
    }

    public void Search_Action(View view) {
        recipeArrayList.clear();

        // setup search string
        String searchVal = input_search.getText().toString();

        // set up API call
        ANRequest.GetRequestBuilder builder = AndroidNetworking.get(
                "https://api.spoonacular.com/recipes/complexSearch")
                .addQueryParameter("apiKey", "74c012848d124ff5ac41b6a48f7723f2");

        if(searchVal.length() != 0) {
            builder.addQueryParameter("query", searchVal);
        }

        if(ingredients.size() != 0) {
            int pos = 0;

            String ingredBuilder = "";

            for(String ingredient : ingredients) {
                if(pos == 0) {
                    ingredBuilder = ingredBuilder.concat(ingredient.toLowerCase());
                } else {
                    ingredBuilder = ingredBuilder.concat(","+ingredient.toLowerCase());
                }
                pos++;
            }

            builder.addQueryParameter("includeIngredients", ingredBuilder);
        }

        ANRequest reg = builder.addQueryParameter("number", "10")
                        .setPriority(Priority.MEDIUM)
                        .build();

        // search with API and convert data to Recipe format
        reg.setAnalyticsListener(new AnalyticsListener() {
            @Override
            public void onReceived(long timeTakenInMillis, long bytesSent, long bytesReceived,
                                   boolean isFromCache) {
                Log.d("ANRequest reg", " timeTakenInMillis : " + timeTakenInMillis);
                Log.d("ANRequest reg", " bytesSent : " + bytesSent);
                Log.d("ANRequest reg", " bytesReceived : " + bytesReceived);
                Log.d("ANRequest reg", " isFromCache : " + isFromCache);
            }
        }).getAsObjectList(SpoonacularRecipe.class, new ParsedRequestListener<List<SpoonacularRecipe>>() {
            @Override
            public void onResponse(List<SpoonacularRecipe> response) {
                // populate itemsList and recipeArrayList
                for (SpoonacularRecipe recipe : response) {
                    Log.d("id", String.valueOf(recipe.getID()));
                    Log.d("title", recipe.getTitle());
                    Log.d("image", recipe.getImage());
                    Log.d("imageType", recipe.getImageType());

                    recipeArrayList.add(new Recipe(recipe));
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(ANError anError) {
                Toast.makeText(MainActivity.this,
                        "There was an error on receiving info from the API.",
                        Toast.LENGTH_SHORT).show();
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
            bundle.putString("link", item.getLink().toString());
            Intent intent = new Intent(MainActivity.this, RecipeViewer.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
