package com.hangtran.map.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.LocationOfflineDatabase;
import com.hangtran.map.R;
import com.hangtran.map.model.IoTDeviceLocationFinder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class AddMap extends AppCompatActivity {

    private static final String urlUpload   = "http://www.jz.jec.ac.jp/jecseeds/footprint/register.php";
    //private static  final  String urlUpload = "http://10.40.112.46/sample/17jz/post_test.php";
    private static final   String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
    private EditText    edit_map_name;
    private TextView    tvStartDate;
    private TextView    tvStopDate;
    private TextView    tvStartTime;
    private TextView    tvStopTime;
    private Button      btnAddMap;
    private Calendar    startDates;
    private Calendar    endDates;

    private int[]  dateStart = new int[3];
    private int[]  timeStart = new int[2];

    private int[]  dateEnd = new int[3];
    private int[]  timeEnd = new int[2];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_map);

        initView();
        addEvents();
        createToolbar();
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white_28dp);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 未入力チェック
     */
    private void addEvents() {
        btnAddMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(edit_map_name.getText())){
                    edit_map_name.setError(getString(R.string.please_enter_your_name));
                    return;
                }

                if (TextUtils.isEmpty(tvStartDate.getText())){
                    tvStartDate.setError(getString(R.string.please_enter_start_date));
                    return;
                }

                if (TextUtils.isEmpty(tvStartTime.getText())){
                    tvStartTime.setError(getString(R.string.please_enter_start_time));
                    return;
                }

                if (TextUtils.isEmpty(tvStopDate.getText())){
                    tvStopDate.setError(getString(R.string.please_enter_stop_date));
                    return;
                }

                if (TextUtils.isEmpty(tvStopTime.getText())){
                    tvStopTime.setError(getString(R.string.please_enter_stop_time));
                    return;
                }

                addMapIntoServer();

                //Intent intent = new Intent(AddMap.this, ShowMap.class);
                //startActivity(intent);
            }
        });
        tvStartDate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickStartDate();
            }
        });
        tvStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickStartTime();
            }
        });
        tvStopDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickStopDate();
            }
        });
        tvStopTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickStopTime();
            }
        });
    }

    /**
     * 位置情報をサーバーへ送信
     */
    private void addMapIntoServer() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        //StringRequest stringRequest = new StringRequest(Request.Method.POST, urlUpload,
        Map<String, String> postParams = new HashMap<>();
        String startDate = dateFormat.format(startDates.getTimeInMillis());
        String stopDate  = dateFormat.format(endDates.getTimeInMillis());

        postParams.put("device_id"    , BaseApplication.getDeviceID());
        postParams.put("start_date"   , startDate);
        postParams.put("end_date"     , stopDate);
        postParams.put("name"         , edit_map_name.getText().toString());
        postParams.put("location"     , new Gson().toJson(new LocationOfflineDatabase().getLocationList(startDate,stopDate)));

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpload, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        if (serverResponse != null) {
                           // Log.d("Debug", serverResponse.toString());
                        }else {
                            //Log.d("Debug", "null");
                        }

                        Toast.makeText(getApplicationContext(), getString(R.string.map_registration_is_complete), Toast.LENGTH_LONG).show();
//                        Intent intent = new Intent(getApplicationContext(), ShowMap.class);
//                        intent.putExtra("Maps",item);
//                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("Debug",volleyError.toString());
                        Toast.makeText(getApplicationContext(), getString(R.string.map_registration_failed), Toast.LENGTH_LONG).show();
                        Log.d("Debug", "onErrorResponse: " + volleyError.getMessage() );
            }
        });

        requestQueue.add(request);
    }

    private void initView() {
        tvStartDate     = findViewById(R.id.start_date);
        tvStartTime     = findViewById(R.id.start_time);
        tvStopDate      = findViewById(R.id.stop_date);
        tvStopTime      = findViewById(R.id.stop_time);
        btnAddMap       = findViewById(R.id.btn_add_map);
        edit_map_name   = findViewById(R.id.edit_map_name);

        startDates = Calendar.getInstance();
        endDates   = Calendar.getInstance();
    }

    private void PickStartDate() {
        final Calendar calendar = Calendar.getInstance();
        final int date = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                dateStart[0] = i;
                dateStart[1] = i1;
                dateStart[2] = i2;

                if (timeStart[0] > 0){
                    startDates.set(dateStart[0],dateStart[1],dateStart[2],timeStart[0],timeStart[1]);
                }

                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                tvStartDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    private void PickStartTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                timeStart[0] = i;
                timeStart[1] = i1;

                if (dateStart[0] > 0){
                    startDates.set(dateStart[0],dateStart[1],dateStart[2],timeStart[0],timeStart[1]);
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, i, i1);
                tvStartTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, min, true);
        timePickerDialog.show();
    }

    private void PickStopDate() {
        final Calendar calendar = Calendar.getInstance();
        int date  = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year  = calendar.get(Calendar.YEAR);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

                dateEnd[0] = i;
                dateEnd[1] = i1;
                dateEnd[2] = i2;

                if (timeEnd[0] > 0){
                    endDates.set(dateEnd[0],dateEnd[1],dateEnd[2],timeEnd[0],timeEnd[1]);
                }

                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                tvStopDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, year, month, date);
        datePickerDialog.show();
    }

    private void PickStopTime() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {

                timeEnd[0] = i;
                timeEnd[1] = i1;

                if (dateEnd[0] > 0){
                    endDates.set(dateEnd[0],dateEnd[1],dateEnd[2],timeEnd[0],timeEnd[1]);
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
                calendar.set(0, 0, 0, i, i1);
                tvStopTime.setText(simpleDateFormat.format(calendar.getTime()));
            }
        }, hour, min, true);
        timePickerDialog.show();
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