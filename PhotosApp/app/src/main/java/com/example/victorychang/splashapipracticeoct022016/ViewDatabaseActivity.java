package com.example.victorychang.splashapipracticeoct022016;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import android.widget.ListView;
import android.view.View;

public class ViewDatabaseActivity extends AppCompatActivity {


    SQLiteDatabase db;              // Reference to SQLite Database
    ArrayList<String> urls;         // List of urls extracted from db to be displayed on ListView

    ListView listView;              // The list view to be populated
    ArrayAdapter<String> adapter;   // Adapter to assist populating listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_database);

        Intent intent = getIntent();
        urls = (ArrayList<String>)intent.getSerializableExtra("urls");
        populateListView();
    }

    /**
     * Populates the ListView with the saved contents/urls int the sqlite database
     */
    public void populateListView() {
        listView = (ListView) findViewById(R.id.savedPhotosListView);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, urls);
        listView.setAdapter(adapter);

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(urls.get(position));

                //Move to webpage with the image
                Uri webpage = Uri.parse(urls.get(position));
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }
        };

        listView.setOnItemClickListener(listener);




    }

    /**
     * Back button clicked, navigate back to main activity
     */
    public void backButtonClicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
