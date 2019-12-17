package com.hangtran.map.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.model.MapShare;
import com.hangtran.map.model.Maps;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import top.defaults.colorpicker.ColorPickerPopup;

public class ShareMap extends AppCompatActivity {

    private static final String urlUpload   = "http://www.jz.jec.ac.jp/jecseeds/footprint/share.php";

    private ImageView chooseColor;
    private Maps      maps;
    private int       colorMaps;
    private EditText  editMapName;
    private EditText  editMapStep;
    private ImageView iv_share_foot_mark;

    private String TAG = "ShareMap";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.share_map);

        initView();
        getIntentData();
        createToolbar();
    }

    private void getIntentData() {
        if(getIntent() != null){
            maps = (Maps) getIntent().getSerializableExtra("Maps");
            String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + maps.getImage() + ".png";
            Glide.with(getApplicationContext())
                    .load(pathImage)
                    .into(iv_share_foot_mark);
        }
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_28dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        chooseColor = findViewById(R.id.chooseColor);
        editMapStep = findViewById(R.id.editMapStep);
        editMapName = findViewById(R.id.editMapName);
        iv_share_foot_mark = findViewById(R.id.iv_share_foot_mark);
    }

    /**
     * 色を選ぶ
     * @param view
     */
    public void chooseColor(View view){
        new ColorPickerPopup.Builder(this)
                .initialColor(Color.parseColor("#03B47F")) // Set initial color
                .enableBrightness(false) // Enable brightness slider or not
                .enableAlpha(false) // Enable alpha slider or not
                .okTitle(getString(R.string.choose))
                .cancelTitle(getString(R.string.cancel))
                .showIndicator(true)
                .showValue(false)
                .build()
                .show(chooseColor, new ColorPickerPopup.ColorPickerObserver() {
                    @Override
                    public void onColorPicked(int color) {
                        colorMaps = color;
                        chooseColor.setBackgroundColor(colorMaps);
                    }
                });
    }

    /**
     * 共有情報を取得する
     * @param view
     */
    public void shareMaps(View view) {
        //未入力チェックする
        if (TextUtils.isEmpty(editMapName.getText())){
            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_your_name), Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(editMapStep.getText())){
            Toast.makeText(getApplicationContext(), getString(R.string.please_enter_your_step_name), Toast.LENGTH_SHORT).show();
            return;
        }

        MapShare mapShare = new MapShare();
        mapShare.setId(maps.getId());
        mapShare.setName(editMapName.getText().toString());
        mapShare.setNameStep(editMapStep.getText().toString());
        mapShare.setColor(String.format("%06X", (0xFFFFFF & colorMaps)) + "");
        mapShare.setStartDate(maps.getStart_date());
        mapShare.setRegion(maps.getRegion());
        mapShare.setImage(maps.getImage());
        addShareMapInfoIntoSever(mapShare);

    }
    public void addShareMapInfoIntoSever(MapShare mapShare) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        Map<String, String> postParams = new HashMap<>();

        postParams.put("id"           , mapShare.getId());
        postParams.put("device_id"    , BaseApplication.getDeviceID());
        postParams.put("owner"        , mapShare.getName());
        postParams.put("name"         , mapShare.getNameStep());
        postParams.put("color"        , mapShare.getColor());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpload, new JSONObject(postParams),
                serverResponse -> {
                    if (serverResponse != null) {
                        try {
                            int result = serverResponse.getInt("result_code");
                            if (result != 0) {
                                Toast.makeText(this,serverResponse.getString("error_message"), Toast.LENGTH_LONG).show();
                            } else {
                                mapShare.setImage(serverResponse.getString("image"));
                                Intent intent = new Intent(ShareMap.this, PrintMap.class);
                                intent.putExtra("MapShare", mapShare);
                                startActivity(intent);
                            }
                        }catch(JSONException e) {
                            Toast.makeText(this, getString(R.string.err_got_invalidresponse) + "(" + serverResponse.toString() + ")", Toast.LENGTH_LONG).show();
                            //Log.e(TAG, "onResponse:" + e.getMessage());
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(this, getString(R.string.err_got_invalidresponse) + "(no response)", Toast.LENGTH_LONG).show();
                        //Log.d(TAG, "onResponse: null received!!");
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.share_map_completed), Toast.LENGTH_LONG).show();
                }, volleyError -> {
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
        });
        requestQueue.add(request);
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