package com.hangtran.map.model;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hangtran.map.BaseApplication;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 現在位置の情報を取得するクラス
 * https://qiita.com/hotdrop_77/items/c8098bba9542a7898faf
 */
public class IoTDeviceLocationFinder {
    private static final String urlUpload   = "http://www.jz.jec.ac.jp/jecseeds/iot/stampiot.php";
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final String TAG         = "LocationFinder";


    /**
     * Find IoT Device Location
     * @param activity
     */
    public static void getCurrentLocation(Activity activity) {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(activity);

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
        }
        client.getLastLocation().addOnCompleteListener(activity, new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful() && task.getResult() != null) {
                    // 位置情報を取得できた場合
                    Log.d(TAG, "onComplete: 緯度：" + task.getResult().getLatitude());
                    Log.d(TAG, "onComplete: 経度：" + task.getResult().getLongitude());
                    // この後、現在時刻も取得してサーバにJSONで送る
                    addStampIntoServer(activity,task.getResult().getLatitude(), task.getResult().getLongitude());
                } else {
                    // 取得に失敗した場合
                    Log.d(TAG, "onComplete: " + task.getException().getMessage());
                }
            }
        });
    }

    /**
     * Send IoT Device information  to server
     *
     */
    public static void addStampIntoServer(Activity activity, double latitude, double longtitude) {
        RequestQueue requestQueue = Volley.newRequestQueue(activity.getApplicationContext());
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);

        Map<String, String> postParams = new HashMap<>();

        String dateTimeNow = dateFormat.format(LocalDateTime.now());

        postParams.put("device_id"    , BaseApplication.getDeviceID());
        postParams.put("stamped_at"   , dateTimeNow);
        postParams.put("longitude"    , String.valueOf(longtitude));
        postParams.put("latitude"     , String.valueOf(latitude));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpload, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        if (serverResponse != null) {
                            //Log.d("Debug", serverResponse.toString());
                        }else {
                            //Log.d("Debug", "null");
                        }
                        Toast.makeText(activity.getApplicationContext(),"Stamped", Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Log.d("Debug",volleyError.toString());
                Toast.makeText(activity.getApplicationContext(), "Stamp failed", Toast.LENGTH_LONG).show();
                //Log.d("Debug", "onErrorResponse: " + volleyError.getMessage() );
            }
        });
        requestQueue.add(request);
    }
    }
