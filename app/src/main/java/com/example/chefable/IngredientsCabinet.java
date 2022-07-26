package com.example.chefable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class IngredientsCabinet extends AppCompatActivity {
    ArrayList<String> ingredArrayList;
    EditText input_ingred;
    ListView ingred_list;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_cabinet);
        input_ingred = findViewById(R.id.ingredient_name_input);
        ingred_list = findViewById(R.id.list_ingredients);
        ingredArrayList = new ArrayList<>();

        Query query = FirebaseDatabase.getInstance().getReference().child("Ingredients");
        arrayAdapter = new ArrayAdapter<>(IngredientsCabinet.this,
                android.R.layout.simple_list_item_1,
                ingredArrayList);
        ingred_list.setAdapter(arrayAdapter);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot favorite_recipe : snapshot.getChildren()) {
                    ingredArrayList.add(favorite_recipe.getValue(String.class));
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

    public void Submit_Action(View view) {
        String val = input_ingred.getText().toString();
        if (val.length() != 0) {

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Ingredients").orderByValue().equalTo(val);

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        Toast.makeText(IngredientsCabinet.this,
                                "Ingredient already present in your digital cabinet",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        ingredArrayList.add(val);

                        FirebaseDatabase.getInstance().getReference().child("Ingredients")
                                .push().setValue(val);

                        Toast.makeText(IngredientsCabinet.this,
                                "Ingredient added to cabinet", Toast.LENGTH_SHORT).show();
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }

    public void RemoveIngred_Action(View view) {
        String item = (String) ingred_list.getSelectedItem();
        if (item != null) {
            ingredArrayList.remove(item);

            Query query = FirebaseDatabase.getInstance().getReference()
                    .child("Ingredients").orderByValue().equalTo(item);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot removal : snapshot.getChildren()) {
                        removal.getRef().removeValue();
                        arrayAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}