package com.hangtran.map.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.model.Maps;
import com.hangtran.map.model.Overlap;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ShowMap extends AppCompatActivity {

    private static final String    urlOverlap = "http://www.jz.jec.ac.jp/jecseeds/footprint/puton.php";

    private ImageView   iv_show_map;
    private Maps        maps;
    private TextView    txtNameMaps,txtRegionAndTime;
    private Button      buttonChangeOverlap;            // 2019/11/30 sugawara ADD
    private boolean     overlapped;                     // 2019/11/30 sugawara ADD (あしあとを重ねた状態かどうか)

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_map);

        initView();
        getIntentData();
        addMaps();
        overlapped = false;
    }

    @SuppressLint("SetTextI18n")
    /**
     * 追加されたマップを表示する
     */
    private void addMaps() {

        txtRegionAndTime.setText(maps.getRegion() + " " + maps.getStart_date().substring(0,16) + " - " + maps.getEnd_date().substring(11,16));
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
        iv_show_map         = findViewById(R.id.iv_show_map);
        txtNameMaps         = findViewById(R.id.txtNameMaps);
        txtRegionAndTime    = findViewById(R.id.txtRegionAndTime);
        buttonChangeOverlap = findViewById(R.id.buttonChangeOverlap); // 2019/11/30 sugawara ADD
    }

    public void cancelActivity(View view) { finish(); }

    public void shareMaps(View view) {
        Intent intent = new Intent(ShowMap.this, ShareMap.class);
        intent.putExtra("Maps", maps);
        startActivity(intent);

    }
    String TAG = "sugawara";
    public void creatOverlapMap( View view) {

        if (!overlapped) {
            RequestQueue requestQueue = Volley.newRequestQueue(this);

            /// リクエスト形式をJSONに変更
            Map<String, String> postParams = new HashMap<>();

            postParams.put("device_id", BaseApplication.getDeviceID());
            postParams.put("id",maps.getId());

            JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, urlOverlap, new JSONObject(postParams),
                    (response) -> {
                        if (response != null){
                            try {
                                int result = response.getInt("result_code");
                                if (result != 0) {
                                    Toast.makeText(this,response.getString("error_message"), Toast.LENGTH_LONG).show();
                                } else {
                                    Overlap overlap = new Gson().fromJson(response.toString(), Overlap.class);

                                    // 別の Activity に飛ぶ必要はない。この画面の表示を変えるだけにする。
                                    String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + overlap.getImage();
                                    Glide.with(getApplicationContext())
                                            .load(pathImage)
                                            .into(iv_show_map);
                                    buttonChangeOverlap.setText(R.string.delete_my_steps);
                                    overlapped = true;
                                }
                            } catch (JSONException e) {
                                Toast.makeText(this, getString(R.string.err_got_invalidresponse) + "(" + response.toString() + ")", Toast.LENGTH_LONG).show();
                                Log.e(TAG, "onResponse:" + e.getMessage());
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(this, getString(R.string.err_got_invalidresponse) + "(no response)", Toast.LENGTH_LONG).show();
                            Log.d(TAG, "onResponse: null received!!");
                        }
                    }, (volleyError) -> {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = getString(R.string.err_unreachable_server);
                } else if (volleyError instanceof ServerError) {
                    message = getString(R.string.err_server_notfound);
                } else if (volleyError instanceof TimeoutError) {
                    message = getString(R.string.err_timeout);
                } else {
                    message = getString(R.string.map_registration_failed);
                }
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                Log.d("Debug", "onErrorResponse: " + volleyError.getMessage());
            }) {
            };
            requestQueue.add(jsonRequest);
        } else {
            // すでに重ねた状態だったら、重ねた状態を解除
            addMaps();
            buttonChangeOverlap.setText(R.string.merge_my_steps);
            overlapped = false;
        }
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