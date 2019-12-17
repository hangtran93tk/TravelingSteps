package com.hangtran.map.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.LocationOfflineDatabase;
import com.hangtran.map.R;
import com.hangtran.map.model.LocalLocation;
import com.hangtran.map.view.WelcomeScreen;

/**
 * 位置情報取得
 */
public class LocationService extends Service {

    private static final long LOCATION_REQUEST_MAX_TIME = 60 * 1000L;    // 位置情報リクエストの最長時間(msec)
    private static final float LOCATION_REQUEST_MIN_DISTANCE = 5.0F;     // 位置情報リクエストの最短距離(m)

    FusedLocationProviderClient client;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        int requestCode     = 0;
        String channelId    = "default";
        String title        = getString(R.string.app_name);

        Intent intent1      = new Intent(this, WelcomeScreen.class);

        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), requestCode,
                        intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        // ForegroundにするためNotificationが必要、Contextを設定
        NotificationManager notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        // Notification　Channel 設定
        NotificationChannel channel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(
                    channelId, title, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Silent Notification");
            // 通知音を消さないと毎回通知音が出てしまう
            // この辺りの設定はcleanにしてから変更
            channel.setSound(null, null);
            // 通知ランプを消す
            channel.enableLights(false);
            channel.setLightColor(Color.BLUE);
            // 通知バイブレーション無し
            channel.enableVibration(false);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                Notification notification = new Notification.Builder(getApplicationContext(), channelId)
                        .setContentTitle(title)
                        // 本来なら衛星のアイコンですがandroid標準アイコンを設定
                        .setSmallIcon(android.R.drawable.btn_star)
                        .setContentText("GPS")
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent)
                        .setWhen(System.currentTimeMillis())
                        .build();
                startForeground(1, notification);
            }
        }
        startTracking();
        return START_STICKY;
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Log.d("debug","Lat: " + locationResult.getLastLocation().getLatitude() + "  Long: " + locationResult.getLastLocation().getLongitude() + "  ID: " + BaseApplication.getDeviceID());

            LocalLocation localLocation = new LocalLocation();
            localLocation.setDevice_id(BaseApplication.getDeviceID());
            localLocation.setLat(locationResult.getLastLocation().getLatitude());
            localLocation.setLng(locationResult.getLastLocation().getLongitude());
            localLocation.setStamped_at();

            LocationOfflineDatabase location = new LocationOfflineDatabase();
            location.insertLocation(localLocation);

        }
    };

    private void startTracking() {
        client = new FusedLocationProviderClient(this);
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(LOCATION_REQUEST_MAX_TIME)
                .setSmallestDisplacement(LOCATION_REQUEST_MIN_DISTANCE);      // デバッグ時はコメントアウト
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
        }
        client.requestLocationUpdates(locationRequest, locationCallback, null);
    }
    /**
     * サービス終了時に位置情報取得を停止
     */
    @Override
    public void onDestroy() {
        if (client != null)
        client.removeLocationUpdates(locationCallback);
        super.onDestroy();
    }
}
