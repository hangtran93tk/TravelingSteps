package com.hangtran.map.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.service.LocationService;

import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    private static final int REQUEST_MULTI_PERMISSIONS = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_screen);
    }
    public void newActivity(View view){
        if (checkMultiPermissions()) {
            Intent intent = new Intent(getApplication(), LocationService.class);
            startService(intent);

            startActivity(new Intent(getApplicationContext(), SelectLanguageActivity.class));
            finish();
        }
    }
    private boolean checkMultiPermissions(){
        // 位置情報の Permission
        int permissionLocation = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        ArrayList reqPermissions = new ArrayList<>();

        // 位置情報の Permission が許可されているか確認
        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
            // 許可済
        }
        else{
            // 未許可
            reqPermissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }

        // 未許可チェック
        if (!reqPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[])reqPermissions.toArray(new String[0]),
                    REQUEST_MULTI_PERMISSIONS);
            return false;
        }
        else{
            return true;
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