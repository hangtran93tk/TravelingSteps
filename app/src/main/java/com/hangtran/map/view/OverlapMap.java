package com.hangtran.map.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.model.Overlap;

public class OverlapMap extends AppCompatActivity {

    private Overlap     overlap;
    private ImageView   imgOverlapMap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overlap_map);

        initView();
        getIntentData();
        loadUI();
    }

    private void loadUI() {
        Glide.with(this)
                .load(overlap.getImage())
                .into(imgOverlapMap);
    }

    private void initView() {
        imgOverlapMap = findViewById(R.id.imgOverlapMap);
    }

    private void getIntentData() {
        if (getIntent() != null){
            overlap = (Overlap) getIntent().getSerializableExtra("Overlap");
        }
    }

    public void cancelActivity(View view) { finish();}

    public void deleteSteps(View view) {
        startActivity(new Intent(OverlapMap.this, ShowMap.class));
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
