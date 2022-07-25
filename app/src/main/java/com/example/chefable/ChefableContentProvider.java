package com.example.chefable;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import androidx.annotation.Nullable;

public class ChefableContentProvider extends ContentProvider {
    public final static String DBNAME = "ChefBase";
    public final static String INGRED_TABLE = "IngredCabinet";
    public final static String FAV_RECIPE_TABLE = "FavoriteRecipesTable";
    private static final String SQL_CREATE_INGRED =
            "CREATE TABLE " + INGRED_TABLE + " ( " +
                    " Ingredient TEXT, " +
                    " Quantity DECIMAL )";
    private static final String SQL_CREATE_RECIPE =
            "CREATE TABLE " + FAV_RECIPE_TABLE + " ( " +
                    " RecipeID INTEGER, " +
                    " Name TEXT, " +
                    " Link TEXT )";
    public static final Uri CONTENT_URI = Uri.parse(
            "content://com.example.chefable.provider"
    );
    private MainDatabaseHelper helper;


    public ChefableContentProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return helper.getWritableDatabase().delete(
                uri.toString().replace(CONTENT_URI.toString().concat("/"), ""),
                selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        String tableName = uri.toString().replace(CONTENT_URI.toString().concat("/"), "");
        long id = helper.getWritableDatabase().insert(tableName, null, values);
        return Uri.withAppendedPath(CONTENT_URI, tableName.concat("/").concat(String.valueOf(id)));
    }

    @Override
    public boolean onCreate() {
        helper = new MainDatabaseHelper(getContext(), DBNAME, null, 1);
        if (helper != null) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String tableName = uri.toString().replace(CONTENT_URI.toString().concat("/"), "");
        return helper.getWritableDatabase()
                .query(tableName, projection, selection, selectionArgs,
                        null, null, sortOrder);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        String tableName = uri.toString().replace(CONTENT_URI.toString().concat("/"), "");
        return helper.getWritableDatabase()
                .update(tableName, values, selection, selectionArgs);
    }

    private static final class MainDatabaseHelper extends SQLiteOpenHelper {
        public MainDatabaseHelper(@Nullable Context context, @Nullable String name,
                                  @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_INGRED);
            db.execSQL(SQL_CREATE_RECIPE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}