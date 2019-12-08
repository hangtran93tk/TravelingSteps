package com.hangtran.map.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.hangtran.map.R;
import com.hangtran.map.model.MyMapShare;

public class ShowMap2 extends AppCompatActivity {

    private ImageView iv_show_map;
    private MyMapShare myMapShare;
    private TextView txtNameMaps,txtRegionAndTime;
    // 2019/11/30 sugawara ADD (あしあとを重ねた状態かどうか)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_map2);

        initView();
        getIntentData();
        addMaps();
    }

    @SuppressLint("SetTextI18n")
    /**
     * 追加されたマップを表示する
     */
    private void addMaps() {

        txtRegionAndTime.setText(myMapShare.getRegion() + " " + myMapShare.getStart_date().substring(0,16) + " - " + myMapShare.getEnd_date().substring(11,16));
        String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + myMapShare.getImage() + ".png";
        Glide.with(getApplicationContext())
                .load(pathImage)
                .into(iv_show_map);
        txtNameMaps.setText(myMapShare.getName());
    }

    private void getIntentData() {
        if(getIntent() != null){
            myMapShare = (MyMapShare) getIntent().getSerializableExtra("ShareMap");
        }
    }

    private void initView() {
        txtNameMaps         = findViewById(R.id.txtNameMaps
        );
        iv_show_map         = findViewById(R.id.iv_show_map);
        txtRegionAndTime    = findViewById(R.id.txtRegionAndTime);
    }

    public void cancelActivity(View view) { finish(); }
}