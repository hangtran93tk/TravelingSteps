package com.hangtran.map.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.cardview.widget.CardView;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileOutputStream;

/**
 *ポストカードを保存する
 */
public class FileUtils {

    public static boolean savePostcard(CardView cardView) {
        cardView.setDrawingCacheEnabled(true);
        cardView.buildDrawingCache();
        Bitmap cache = cardView.getDrawingCache();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".png");
            cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            //Log.d("debug",e.toString());
            return false;
        } finally {
            cardView.destroyDrawingCache();
        }
    }

    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);
    }

    /**
     * QRコード作成する
     * @return
     */
    public static File createQRCode(String content){
        return QRCode.from(content).withSize(250, 250).to(ImageType.PNG).file();
    }
}