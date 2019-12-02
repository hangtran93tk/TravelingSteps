package com.hangtran.map.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hangtran.map.R;
import com.hangtran.map.adapter.MyAdapter;
import com.hangtran.map.fragment.FirstFragment;
import com.hangtran.map.model.IoTDeviceLocationFinder;

public class MyActivity extends AppCompatActivity {

    private ViewPager  mVpDemo;
    private Boolean    isEditable = false;
    private MyAdapter  adapter;
    private TabLayout  tabLayout;
    private TextView   mTvRemove;
    private View       container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        initView();
        initTabPage();
    }

    public void setBackground(int color){
        container.setBackgroundColor(color);
    }

    private void initTabPage() {
        adapter = new MyAdapter(getSupportFragmentManager(),this);
        mVpDemo.setAdapter(adapter);
        tabLayout.setupWithViewPager(mVpDemo);
    }

    private void initView() {
        mVpDemo     = findViewById(R.id.vp_demo);
        tabLayout   = findViewById(R.id.tl_demo);
        mTvRemove   = findViewById(R.id.mTvRemove);
        container   = findViewById(R.id.container);

        /// リストを一度表示すると再取得できない不具合を修正
        mVpDemo.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) { }
            @Override
            public void onPageSelected(int position) {
                updateMaps();
            }
            @Override
            public void onPageScrollStateChanged(int state) { }
        });
    }

    public void removeMaps(View view){
       if (isEditable) {
            mTvRemove.setText(getString(R.string.cancel));
            isEditable = false;
        } else {
            mTvRemove.setText(getString(R.string.choose));
            isEditable = true;
        }
        updateTextWhenHaveImageChosen(isEditable);
        updateMaps();
    }

    private void updateMaps() {
        /// リストを一度表示すると再取得できない不具合を修正
        if(adapter.getItem(mVpDemo.getCurrentItem()) instanceof FirstFragment){
            ((FirstFragment)adapter.getItem(mVpDemo.getCurrentItem())).updateFloatActionButton(isEditable);
            ((FirstFragment)adapter.getItem(mVpDemo.getCurrentItem())).refreshList();
        }
    }

    public void updateTextWhenHaveImageChosen(boolean isHave) {
        if (isHave) {
            mTvRemove.setText(getString(R.string.cancel));
        } else {
            mTvRemove.setText(getString(R.string.choose));
        }
    }
    /**
     * 言語を選ぶ
     * @param view
     */
    public void selectLanguage(View view) {
        Intent intent = new Intent(getApplicationContext(),SelectLanguageActivity.class);
        intent.putExtra("123","");
        startActivity(intent);
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

