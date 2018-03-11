package com.example.hugo.projetgeo;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Hugo on 11/03/2018.
 */

public class AddLocation extends AppCompatActivity {

    SQLiteDatabase db;
    DbHelper dbHelper;
    FloatingActionButton fabValider;
    EditText editTitle;
    EditText editDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);

        final Bundle extras = getIntent().getExtras();


        final DbHelper dbHelper = new DbHelper(this);
        db = dbHelper.getWritableDatabase();

        final Context context = this;
        FloatingActionButton fab = findViewById(R.id.fabValider);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTitle.getText().toString().length() > 0){
                    //Toast.makeText(context,"Envoi à la DB",Toast.LENGTH_LONG).show();
                    String titre = editTitle.getText().toString();
                    String comments = editDescription.getText().toString();
                    float defaultLat = 0.347596f;   //sets the default marker location to Kampala, Uganda
                    float defaultLong = 32.582520f;
                    float latitude = (float) extras.getDouble("lat");
                    float longitude = (float) extras.getDouble("lng");
                    CustomMarker newMarker = new CustomMarker(titre,latitude,longitude,"Test Owner",comments);
                    //CustomMarker test = new CustomMarker()

                    dbHelper.addMarker(db,newMarker);

                    finish();

                }else{
                    Toast.makeText(context,"Veuillez spécifier un titre",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_back) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
