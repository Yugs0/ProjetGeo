package com.example.hugo.projetgeo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hugo.projetgeo.DbContract.*;
import com.google.android.gms.maps.model.Marker;
import com.example.hugo.projetgeo.CustomMarker;

import java.util.ArrayList;


/**
 * Created by Jean-François on 06-03-18.
 */

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "marker.db";
    private static final Integer DATABASE_VERSION = 1;

    public DbHelper (Context context) { super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();

        sb.append("CREATE TABLE ");
        sb.append(MarkersEntries.TABLE_NAME);
        sb.append(" (");
        sb.append(MarkersEntries._ID);
        sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        sb.append(MarkersEntries.TABLE_NAME);
        sb.append(" TEXT NOT NULL,");
        sb.append(MarkersEntries.MARKER_RATING);
        sb.append(" TEXT NOT NULL,");
        sb.append(MarkersEntries.MARKER_LATITUDE);
        sb.append(" REAL,");
        sb.append(MarkersEntries.MARKER_LONGITUDE);
        sb.append(" REAL,");
        sb.append(MarkersEntries.MARKER_COMMENTS);
        sb.append(" TEXT,");
        sb.append(MarkersEntries.MARKER_OWNER);
        sb.append(" TEXT NOT NULL);");

        db.execSQL(sb.toString());

        CustomMarker tab[] = new CustomMarker[10];



    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MarkersEntries.TABLE_NAME);
        onCreate(db);
    }

    public ArrayList<CustomMarker> getAllMarkers(SQLiteDatabase db){
        Cursor cursor;

        String query = "SELECT * FROM " + MarkersEntries.TABLE_NAME + " ORDER BY " + MarkersEntries._ID + " ASC";
        cursor = db.rawQuery(query, null);

        ArrayList<CustomMarker> result = new ArrayList<>();
        while(cursor.moveToNext()) {
            CustomMarker current = new CustomMarker(
                    cursor.getInt(cursor.getColumnIndex(MarkersEntries._ID)),
                    cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_NAME)),
                    cursor.getFloat(cursor.getColumnIndex(MarkersEntries.MARKER_LATITUDE)),
                    cursor.getFloat(cursor.getColumnIndex(MarkersEntries.MARKER_LONGITUDE)),
                    cursor.getInt(cursor.getColumnIndex(MarkersEntries.MARKER_RATING)),
                    cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_COMMENTS)),
                    cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_OWNER))
            );
            result.add(current);
        }

        return result;


    }

    public Cursor getCursorMarker(SQLiteDatabase db) {
        String query = "SELECT * FROM" + MarkersEntries.TABLE_NAME + " ORDER BY " + MarkersEntries._ID + " ASC";
        return db.rawQuery(query,null);
    }
     public void addMarker(SQLiteDatabase db, CustomMarker[] tab) {

        for(int i = 0 ; i < tab.length; i++) {
            ContentValues cv = new ContentValues();
            cv.put(MarkersEntries.MARKER_NAME, tab[i].getName());
            cv.put(MarkersEntries.MARKER_LATITUDE, tab[i].getLatitude());
            cv.put(MarkersEntries.MARKER_LONGITUDE, tab[i].getLongitude());
            cv.put(MarkersEntries.MARKER_RATING, tab[i].getRating());
            cv.put(MarkersEntries.MARKER_COMMENTS, tab[i].getComments());
            cv.put(MarkersEntries.MARKER_OWNER, tab[i].getOwner());

            db.insert(MarkersEntries.TABLE_NAME, null, cv);
         }
     }

     public CustomMarker getMarkerById(int id , SQLiteDatabase db){
        String query = "SELECT * FROM " + MarkersEntries.TABLE_NAME + " WHERE " + MarkersEntries._ID + " = " + id;
        Cursor cursor = db.rawQuery(query,null);
        if(!cursor.moveToNext()) return null;
        return new CustomMarker(
                cursor.getInt(cursor.getColumnIndex(MarkersEntries._ID)),
                cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_NAME)),
                cursor.getFloat(cursor.getColumnIndex(MarkersEntries.MARKER_LATITUDE)),
                cursor.getFloat(cursor.getColumnIndex(MarkersEntries.MARKER_LONGITUDE)),
                cursor.getInt(cursor.getColumnIndex(MarkersEntries.MARKER_RATING)),
                cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_COMMENTS)),
                cursor.getString(cursor.getColumnIndex(MarkersEntries.MARKER_OWNER))

        );

     }
}
