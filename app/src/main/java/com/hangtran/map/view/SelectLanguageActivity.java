package com.hangtran.map.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.hangtran.map.R;

import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);

        if (getIntent().getStringExtra("123") == null){
            if (checkLanguage()){
                startActivity(new Intent(getApplicationContext(),MyActivity.class));
            }
        }
    }

    private boolean checkLanguage() {
        int language = getSharedPreferences("Languages",MODE_PRIVATE).getInt("Language",0);

        if (language == 0){
            return false;
        }

        switch (language){
            case 1:
                changeLanguage(new Locale("ja","Japan"));
                break;
            case 2:
                changeLanguage(new Locale("ko","Korean"));
                break;
            case 3:
                changeLanguage(new Locale("en","US"));
                break;
            case 4:
                changeLanguage(new Locale("zh","China"));
                break;
        }
        return true;
    }

    public void selectJapan(View view) {
        changeLanguage(new Locale("ja","Japan"));
        saveLanguage(1);
        startActivity(new Intent(getApplicationContext(),MyActivity.class));
    }

    private void saveLanguage(int languageCode) {
        getSharedPreferences("Languages",MODE_PRIVATE).edit().putInt("Language",languageCode).apply();
    }

    public void selectSouthKorea(View view) {
        changeLanguage(new Locale("ko","Korean"));
        saveLanguage(2);
        startActivity(new Intent(getApplicationContext(),MyActivity.class));
    }

    public void selectUnitedKingdom(View view) {
        changeLanguage(new Locale("en","US"));
        saveLanguage(3);
        startActivity(new Intent(getApplicationContext(),MyActivity.class));
    }

    public void selectChina(View view) {
        changeLanguage(new Locale("zh","China"));
        saveLanguage(4);
        startActivity(new Intent(getApplicationContext(),MyActivity.class));
    }


    private void changeLanguage(Locale locale){
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        Configuration configuration = new Configuration();

        configuration.setLocale(locale);

        getResources().updateConfiguration(configuration,displayMetrics);
    }
}
