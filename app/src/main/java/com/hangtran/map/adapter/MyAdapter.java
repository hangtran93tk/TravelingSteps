package com.hangtran.map.adapter;

import android.app.Activity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.hangtran.map.R;
import com.hangtran.map.fragment.FirstFragment;
import com.hangtran.map.fragment.SecondFragment;

/**
 * 各フラグメントを表示するクラス
 */
public class MyAdapter extends FragmentStatePagerAdapter {
    private String[]            listTab;
    private FirstFragment       mFirstFragment;                     //自分の記録マップリスト
    private SecondFragment      mSecondFragment;                   //共有された記録マップリスト

    public MyAdapter(FragmentManager fm, Activity activity) {
        super(fm);
        listTab = new String[2];
        listTab[0] = activity.getResources().getString(R.string.list_of_my_steps);
        listTab[1] = activity.getResources().getString(R.string.steps_shared_from_a_friend);
        mFirstFragment  = new FirstFragment();
        mSecondFragment = new SecondFragment();
    }

    /**
     * 各フラグメントの位置を返す
     * @param position　位置
     * @return なし
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return mFirstFragment;
        }else if(position == 1) {
            return mSecondFragment;
        }
        return null;
    }

    /**
     * マップリストの長さを計算する
     * @return listTab.length マップリストの長さ
     */
    @Override
    public int getCount() {
        return listTab.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
