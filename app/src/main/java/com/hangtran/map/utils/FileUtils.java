package com.hangtran.map.utils;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import androidx.cardview.widget.CardView;

import com.hangtran.map.BaseApplication;

import net.glxn.qrgen.android.QRCode;
import net.glxn.qrgen.core.image.ImageType;

import java.io.File;
import java.io.FileOutputStream;

/**
 * ポストカードを保存する
 */
public class FileUtils {

    public static boolean savePostcard(CardView cardView) {
        cardView.setDrawingCacheEnabled(true);
        cardView.buildDrawingCache();
        Bitmap cache = cardView.getDrawingCache();
        try {
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/");
            @SuppressLint("DefaultLocale") String fileName = String.format("%d.png", System.currentTimeMillis());
            File outFile = new File(dir, fileName);

            FileOutputStream fileOutputStream = new FileOutputStream( outFile);
            cache.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            scanFile(outFile);
            return true;
        } catch (Exception e) {
            //Log.d("debuggfhfg4hgfh4gfh84",e.toString());
            return false;
        } finally {
            cardView.destroyDrawingCache();
        }
    }

    private static void scanFile(File file) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(Uri.fromFile(file));
        BaseApplication.getContext().sendBroadcast(scanIntent);
    }

    /**
     * QRコード作成する
     *
     * @return
     */
    public static File createQRCode(String content) {
        return QRCode.from(content).withSize(250, 250).to(ImageType.PNG).file();
    }
}