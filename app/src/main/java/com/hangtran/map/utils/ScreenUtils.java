package com.hangtran.map.utils;

import android.graphics.Point;
import android.view.WindowManager;
import com.hangtran.map.BaseApplication;

import static android.content.Context.WINDOW_SERVICE;

public class ScreenUtils {

    private static int      mWidth;
    private static int      mHeight;
    private static float    mDensity;

    public static int getWidth(){
        if(mWidth == 0) {
            Point size = new Point();
            WindowManager mWindowManager = (WindowManager) BaseApplication.getContext().getSystemService(WINDOW_SERVICE);
            if (mWindowManager != null) {
                mWindowManager.getDefaultDisplay().getSize(size);
            }
            mWidth  = size.x;
            mHeight = size.y;
        }
        return mWidth;
    }
    public static int getHeight(){
        if(mHeight == 0) {
            Point size = new Point();
            WindowManager mWindowManager = (WindowManager) BaseApplication.getContext().getSystemService(WINDOW_SERVICE);
            if (mWindowManager != null) {
                mWindowManager.getDefaultDisplay().getSize(size);
            }
            mWidth  = size.x;
            mHeight = size.y;
        }
        return mHeight;
    }

    public static float getDensity(){
        if (mDensity == 0){
            mDensity = BaseApplication.getContext().getResources().getDisplayMetrics().density;
        }
        return mDensity;
    }
}