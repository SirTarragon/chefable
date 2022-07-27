package com.example.chefable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class RecipeViewer extends AppCompatActivity {
    public static final String ARG_LINK = "link";
    private static final String TAG_WEB_FRAGMENT = "web_fragment";

    private WebFragment webFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_viewer);

        Bundle bundle = getIntent().getExtras();

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction trans;
        trans = manager.beginTransaction();

        if (bundle != null) {
            Log.d("BundleNotNull", "Inside of grabbing of bundle");
            String url = bundle.getString(ARG_LINK);
            webFragment = WebFragment.newInstance(url);
        } else {
            webFragment = WebFragment.newInstance();
        }

        trans.add(R.id.web_fragment_container, webFragment, TAG_WEB_FRAGMENT);
        trans.commit();
    }

    public void Home_Intent(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}