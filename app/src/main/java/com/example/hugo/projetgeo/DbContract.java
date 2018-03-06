package com.example.hugo.projetgeo;

import android.location.Location;
import android.provider.BaseColumns;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Jean-François on 06-03-18.
 */

public class DbContract {
    private DbContract() {
    }

    public static final class MarkersEntries implements BaseColumns {

        public static final String TABLE_NAME = "markers";
        public static final String MARKER_NAME = "name";
        public static final String MARKER_RATING = "rating";
        public static final String MARKER_LATITUDE = "latitude";
        public static final String MARKER_LONGITUDE = "longitude";
        public static final String MARKER_COMMENTS = "comments";
        public static final String MARKER_OWNER = "username";
    }
}
