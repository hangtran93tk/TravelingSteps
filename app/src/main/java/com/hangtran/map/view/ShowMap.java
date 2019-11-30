package com.hangtran.map.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.model.Maps;
import com.hangtran.map.model.Overlap;

import java.util.HashMap;
import java.util.Map;

public class ShowMap extends AppCompatActivity {

    //private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/detail.php";
    private static final String    urlOverlap = "http://www.jz.jec.ac.jp/jecseeds/footprint/puton.php";

    private ImageView        iv_show_map;
    private Maps             maps;
    private TextView         txtNameMaps,txtRegionAndTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

        initView();
        getIntentData();
        addMaps();
    }

    @SuppressLint("SetTextI18n")
    /**
     * 追加されたマップを表示する
     */
    private void addMaps() {
        txtRegionAndTime.setText(maps.getRegion() + "  " + maps.getStartDate().substring(0,16));

        String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + maps.getImage() + ".png";
        Glide.with(getApplicationContext())
                .load(pathImage)
                .into(iv_show_map);
                txtNameMaps.setText(maps.getName());
    }

    private void getIntentData() {
        if(getIntent() != null){
            maps = (Maps) getIntent().getSerializableExtra("Maps");
        }
    }

    private void initView() {
        iv_show_map = findViewById(R.id.iv_show_map);
        txtNameMaps = findViewById(R.id.txtNameMaps);
        txtRegionAndTime = findViewById(R.id.txtRegionAndTime);
    }

    public void cancelActivity(View view) { finish(); }

    public void shareMaps(View view) {
        Intent intent = new Intent(ShowMap.this, ShareMap.class);
        intent.putExtra("Maps", maps);
        startActivity(intent);

    }
    public void creatOverlapMap( View view) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, urlOverlap,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            Overlap overlap = new Gson().fromJson(response,Overlap.class);

                            //Log.d("debug",overlap.getImage() + "");

                            Intent intent = new Intent(ShowMap.this, OverlapMap.class);
                            intent.putExtra("Overlap", overlap);
                            startActivity(intent);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d("debug",volleyError.toString());
                        Toast.makeText(getApplicationContext(), getString(R.string.unable_to_display_your_map), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                params.put("device_id", BaseApplication.getDeviceID());
                params.put("id",maps.getId());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }
    /**
     * IoTDevice
     */
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        //Log.d(TAG, "onResume: インテント：" + intent.toString());
        //Log.d(TAG, "onResume: パッケージ名：" + intent.getPackage());
        if (intent.getPackage() != null) {
            IoTDeviceLocationFinder.getCurrentLocation(this);
            // Qmote からの起動
            // サーバに位置・時刻・デバイスIDを送信
        } else {
            // 通常起動
        }
    }
}
