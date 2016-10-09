package com.example.victorychang.splashapipracticeoct022016;

import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.victorychang.splashapipracticeoct022016.model.OneImage;
import com.example.victorychang.splashapipracticeoct022016.model2.Result;
import com.example.victorychang.splashapipracticeoct022016.model2.SearchImage;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;


public class MainActivity extends AppCompatActivity {

    ArrayList<String> imageList;    //list of images
    ArrayList<String> imageTagList; //list of tags to set imagebuttons
    ArrayList<String> urls;         //urls to be passed to viewdatabaseactivity

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //1. Construct openOrCreateDatabase
        db = openOrCreateDatabase("SavedImages", MODE_PRIVATE, null);

        //2. Create a table
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Photos(id VARCHAR, url VARCHAR);";
        db.execSQL(CREATE_TABLE);

        readFromDatabase();
        randomTenButtonClicked(null);
    }

    public void imageButtonClicked(View view) {
        Toast.makeText(this, "an ImageButton was clicked", Toast.LENGTH_SHORT).show();
        //Let's pop up a fragment
        //1. Create a Fragment Layout Manager
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        int index = -1;
        for (int i=0; i <gridLayout.getChildCount(); i++) {
            if (view.getId() == gridLayout.getChildAt(i).getId()) {
                index = i;
            }
        }
        System.out.println("Button index is: " + index);

        // Save the image
        addToDatabase(imageTagList.get(index), imageList.get(index));
    }



    public void viewSavedButtonClick(View view) {

        Toast.makeText(this, "VIEW SAVED clicked.", Toast.LENGTH_SHORT).show();

        readFromDatabase();
        Intent intent = new Intent(this, ViewDatabaseActivity.class);
        intent.putExtra("urls", urls);
        startActivity(intent);

    }


    /*
    public void addToDatabaseTester() {
        //3. Insert Values
        String INSERT_INTO = "INSERT INTO Photos VALUES('apple1', 'wwwnutmegc./ommyimg')";
        db.execSQL(INSERT_INTO);
        Toast.makeText(this, "Inserted into DB", Toast.LENGTH_SHORT).show();
    }
    */

    public void addToDatabase(String imageID, String imageURL) {
        //3. Insert Values
        String INSERT_INTO = "INSERT INTO Photos VALUES('" + imageID + "', '" + imageURL +"')";
        db.execSQL(INSERT_INTO);
        Toast.makeText(this, "Inserted into DB", Toast.LENGTH_SHORT).show();
    }

    public void readFromDatabase() {
        Toast.makeText(this, "Attempting to read from DB", Toast.LENGTH_SHORT).show();
        //4. Read the values
        String SELECT_STAR = "SELECT * FROM Photos";
        Cursor resultSet = db.rawQuery(SELECT_STAR, null);
        urls = new ArrayList<String>();

        if (resultSet.getCount() > 0) {
            resultSet.moveToFirst();
            String data = "";
            do {
                String id = resultSet.getString(0); //resultSet.getColumnIndex("id"));
                String url = resultSet.getString(1); // resultSet.getColumnIndex("url"));
                urls.add(url);
                System.out.println("DEBUG DB: \nid: " + id + ", url: " + url);
            } while (resultSet.moveToNext());
            Toast.makeText(this, "Read from DB", Toast.LENGTH_SHORT).show();
        }
    }


    public void randomTenButtonClicked(View view) {

        Toast.makeText(this, "The API only refreshed every so often.", Toast.LENGTH_SHORT).show();

        Callback<List<OneImage>> temp = new Callback<List<OneImage>>() {
            @Override
            public void onResponse(Call<List<OneImage>> call, Response<List<OneImage>> response) {
                int statusCode = response.code();
                System.out.println("Status code: " + statusCode);

                ArrayList<OneImage> list = (ArrayList<OneImage>) response.body();
                System.out.println(list);

                imageList = new ArrayList<String>();
                imageTagList = new ArrayList<String>();

                for (int i=0; i<10; i++) {
                    System.out.println(list.get(i).getUrls().getSmall());
                    System.out.println(list.get(i).getId());
                    imageList.add(list.get(i).getUrls().getSmall());
                    imageTagList.add(list.get(i).getId());
                }
                populateGridUpToTenImage(imageList);

            }

            @Override
            public void onFailure(Call<List<OneImage>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API call to photos failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        EndpointInterfaceAPI.Factory.getInstance().getListOneImageObjects().enqueue(temp);

    }

    public void searchTenButtonClicked(View view) {

        Toast.makeText(this, "You clicked SEARCH TEN.", Toast.LENGTH_SHORT).show();

        //1. Pull text in searchEditText
        EditText searchEditText = (EditText) findViewById(R.id.searchEditText);

        //2. Add the search api in interface


        //3. Implement the search
        Callback<SearchImage> temp = new Callback<SearchImage>() {
            @Override
            public void onResponse(Call<SearchImage> call, Response<SearchImage> response) {
                int statusCode = response.code();
                System.out.println("Status code: " + statusCode);

                ArrayList<Result> list = (ArrayList<Result>) response.body().getResults();
                System.out.println(list);

                imageList = new ArrayList<String>();
                imageTagList = new ArrayList<String>();


                for (int i=0; i<list.size(); i++) {

                    System.out.println(list.get(i).getUrls().getSmall());
                    System.out.println(list.get(i).getId());

                    imageList.add(list.get(i).getUrls().getSmall());
                    imageTagList.add(list.get(i).getId());
                }
                populateGridUpToTenImage(imageList);

            }

            @Override
            public void onFailure(Call<SearchImage> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API call to search/photos failed: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        };

        EndpointInterfaceAPI.Factory.getInstance().getSearchListOneImageObjects(searchEditText.getText().toString()).enqueue(temp);


        //4. test
    }


    /**
     * A method that takes up to 10 images in a list and then displayed them on the 5x2 gird on main activity
     */
    public void populateGridUpToTenImage(ArrayList<String> urls) {
        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);

        //Clear the board
        for (int i=0; i< gridLayout.getChildCount(); i++) {
            ImageButton imageButton = (ImageButton) gridLayout.getChildAt(i);
            imageButton.setImageResource(R.drawable.whitebox);
            imageButton.setTag("None");
        }

        //Set image and the corresponding tag
        for (int i=0; i < gridLayout.getChildCount(); i++) {
            if (urls.size() == i) {break;}
            ImageButton imageButton = (ImageButton) gridLayout.getChildAt(i);
            Picasso.with(this).load(urls.get(i)).resize(125,125).centerInside().into(imageButton);
            imageButton.setTag(imageTagList.get(i));
            System.out.println("Got it");

        }

    }

}
