package com.hangtran.map;

import android.app.Application;
import android.content.Context;
import android.provider.Settings;

public class BaseApplication extends Application {

    private static Context  mContext;
    private static String   mDeviceID;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

    /**
     * 端末の広告IDを取得する
     * @return mDeviceID 端末の広告ID
     */
    public static String getDeviceID(){
        if (mDeviceID == null){
            mDeviceID = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            //Log.d("debug",mDeviceID);
        }
        return mDeviceID;
    }
}
