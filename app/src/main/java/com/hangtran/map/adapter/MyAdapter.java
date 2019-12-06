package com.hangtran.map.adapter;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hangtran.map.fragment.FirstFragment;
import com.hangtran.map.fragment.SecondFragment;

/**
 * 各フラグメントを表示するクラス
 */
public class MyAdapter extends FragmentPagerAdapter {

    private FirstFragment       mFirstFragment;                     //自分の記録マップリスト
    private SecondFragment      mSecondFragment;                   //共有された記録マップリスト

    public MyAdapter(FragmentManager fm, Activity activity) {
        super(fm);

        mFirstFragment  = new FirstFragment();
        mSecondFragment = new SecondFragment();
    }

    /**
     * 各フラグメントの位置を返す
     * @param position　位置
     * @return なし
     */
    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mFirstFragment;
        }else{
            return mSecondFragment;
        }
    }

    /**
     * マップリストの長さを計算する
     * @return listTab.length マップリストの長さ
     */
    @Override
    public int getCount() {
        return 2;
    }
}
