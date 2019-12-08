package com.hangtran.map.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import com.bumptech.glide.Glide;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;
import com.hangtran.map.model.MapShare;
import com.hangtran.map.utils.FileUtils;

public class PrintMap extends AppCompatActivity {

    private ImageView   imgMaps,imgQR;
    private TextView    txtNameMaps,txtRegion,txtDate;
    private MapShare    mapShare;
    private CardView    cardView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.print_map);

        initView();
        getIntentData();
        loadUI();
        createToolbar();
    }

    private void getIntentData() {
        if(getIntent() != null){
            mapShare = (MapShare) getIntent().getSerializableExtra("MapShare");
        }
    }

    /**
     * QRコードの内容と画像
     */
    private void loadUI() {

        String TAG = "sugawara";
        Log.d(TAG, "loadUI: " + mapShare.getImage());

        txtNameMaps.setText(mapShare.getNameStep());
        txtDate.setText(mapShare.getStartDate().substring(0,16));
        txtRegion.setText(mapShare.getRegion());
        Glide.with(getApplicationContext())
                .load(FileUtils.createQRCode("map_id=" + mapShare.getId() + "\n" + "device_id=" + BaseApplication.getDeviceID()))
                .into(imgQR);

        // 共有のために作られた画像を反映させる
        String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + mapShare.getImage() + ".png";
        Glide.with(getApplicationContext())
                .load(pathImage)
                .into(imgMaps);

    }

    private void initView() {
        imgQR           = findViewById(R.id.imgQR);
        imgMaps         = findViewById(R.id.imgMaps);
        cardView        = findViewById(R.id.cardView);
        txtDate         = findViewById(R.id.txtDate);
        txtRegion       = findViewById(R.id.txtRegion);
        txtNameMaps     = findViewById(R.id.txtNameMaps);
    }

    /**
     * ポストカードを保存
     * @param item
     */
    public void saveMaps(MenuItem item){
        if (FileUtils.savePostcard(cardView)){
            Toast.makeText(getApplicationContext(), getString(R.string.photos_saved), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(getApplicationContext(), getString(R.string.failed_to_save_postcard), Toast.LENGTH_SHORT).show();
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_28dp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_print_map,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
        }
    }
}