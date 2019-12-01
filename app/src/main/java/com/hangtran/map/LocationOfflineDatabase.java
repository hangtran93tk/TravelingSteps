package com.hangtran.map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hangtran.map.model.LocalLocation;

import java.util.ArrayList;

/**
 * SQLiteOpenHelper を使えるようにするための拡張クラス
 * ※直接だとDBアクセスがうまくできない
 */

public class LocationOfflineDatabase extends SQLiteOpenHelper {

    private static final String TABLE_NAME                  = "LOCATION";
    private static final String CREATE_TABLE_LOCATION       = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (" +
                                                              "id INTEGER primary key autoincrement not null, " +
                                                              "idDevice TEXT not null, " +
                                                              "longitude REAL not null, " +
                                                              "latitude REAL not null, " +
                                                              "date TEXT not null)";
    private static final String INSERT_INTO_TABLE_LOCATION  = "INSERT INTO "    + TABLE_NAME + " VALUES(null,'";
    private static final String SELECT_DATA_TABLE_LOCATION  = "SELECT * FROM "  + TABLE_NAME;
    private static final String DELETE_TABLE_LOCATION       = "DELETE FROM "    + TABLE_NAME;

    public LocationOfflineDatabase() {
        super(BaseApplication.getContext(), "Location.sql", null, 1);

        getWritableDatabase().execSQL(CREATE_TABLE_LOCATION);
    }

    public void insertLocation(LocalLocation localLocation){
        getWritableDatabase().execSQL("INSERT INTO " + TABLE_NAME + " VALUES(null, '"+ localLocation.getDevice_id() + "', '" + localLocation.getLng() + "' ,  '" + localLocation.getLat() + "' , '" + localLocation.getStamped_at() + "' )");
    }

    public void deleteLocation(){
        getWritableDatabase().execSQL(DELETE_TABLE_LOCATION);
    }

    public ArrayList<LocalLocation> getLocationList(){
        ArrayList<LocalLocation> mArrays = new ArrayList<>();

        Cursor mCursor = getReadableDatabase().rawQuery(SELECT_DATA_TABLE_LOCATION,null);
        while (mCursor.moveToNext()){
            LocalLocation localLocation = new LocalLocation();
            localLocation.setDevice_id(mCursor.getString(1));
            localLocation.setLat(mCursor.getDouble(3));
            localLocation.setLng(mCursor.getDouble(2));
            localLocation.setStamped_at(mCursor.getString(4));
            mArrays.add(localLocation);
        }
        return mArrays;

    }
    public ArrayList<LocalLocation> getLocationList(String start_date, String stop_date) {
        ArrayList<LocalLocation> mArrays = new ArrayList<>();
        String[] params = new String[]{start_date, stop_date};
        Cursor mCursor = getReadableDatabase().rawQuery(SELECT_DATA_TABLE_LOCATION + " WHERE  date >= '" + start_date + "' AND date <= '" + stop_date + "'", null);
        while (mCursor.moveToNext()){
            LocalLocation localLocation = new LocalLocation();
            localLocation.setDevice_id(mCursor.getString(1));
            localLocation.setLat(mCursor.getDouble(3));
            localLocation.setLng(mCursor.getDouble(2));
            localLocation.setStamped_at(mCursor.getString(4));
            mArrays.add(localLocation);
        }
        return mArrays;
    }

    /**
     * このデータベースを初めて使用する時に実行される処理
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
    }
    /**
     * アプリケーションの更新などによって、データベースのバージョンが上がった場合に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // バージョンアップの際に使用。最初は不要
    }
}
