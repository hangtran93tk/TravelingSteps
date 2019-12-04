package com.hangtran.map.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cheekiat.fabmenu.FabMenu;
import com.cheekiat.fabmenu.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.SimpleScannerActivity;
import com.hangtran.map.adapter.ShareMapAdapter;
import com.hangtran.map.model.MyMapShare;
import com.hangtran.map.view.AddMap;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 共有されたマップの一覧
 */
public class SecondFragment extends Fragment {

    private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/list.php";
    private  static final String urlUpload = "http://www.jz.jec.ac.jp/jecseeds/footprint/share_receive.php";

    private View            mRootView;
    private RecyclerView    mRcvSecondFragment;
    private ShareMapAdapter mShareMapAdapter;
    private FabMenu         fabMenu;
    private ArrayList<MyMapShare> mListMapShare = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView          = inflater.inflate(R.layout.fragment_second,container,false);
        mRcvSecondFragment =  mRootView.findViewById(R.id.recycler_view_fragment_second);
        fabMenu             = mRootView.findViewById(R.id.fabMenu);
        return mRootView;
    }

    private void initFloatingButton() {
        fabMenu.addItem(R.drawable.ic_image_64dp,R.color.blue);
        fabMenu.addItem(R.drawable.ic_camera_64dp,R.color.blue);

        fabMenu.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(int i) {
                switch (i) {
                    case 0:
                        //Log.d("VVVV","StartScan");
                        // https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                        startActivityForResult(new Intent(getActivity(), SimpleScannerActivity.class), 2000);
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), AddMap.class));
                        break;
                }
            }
        });
    }

    /// 2019/11/30 d.sugawara modify START
    // リストを一度表示すると再取得できない不具合を修正
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvSecondFragment.setHasFixedSize(true);
        mRcvSecondFragment.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mShareMapAdapter = new ShareMapAdapter(mListMapShare);
        mRcvSecondFragment.setAdapter(mShareMapAdapter);

        initFloatingButton();
        getShareMapData();
    }

    public void addQRCodeInfoIntoServer(String map_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(this.getContext());

        Map<String, String> postParams = new HashMap<>();

        postParams.put("map_id"           , map_id);
        postParams.put("device_id"        , BaseApplication.getDeviceID());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpload, new JSONObject(postParams),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject serverResponse) {
                        if (serverResponse != null) {
                            //Log.d("Debug", serverResponse.toString());
                        }else {
                            //Log.d("Debug", "null");
                        }
                        Toast.makeText(getContext(),getString(R.string.get_map_completed), Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //Log.d("Debug",volleyError.toString());
                Toast.makeText(getContext(), getString(R.string.get_map_failed), Toast.LENGTH_LONG).show();
                //Log.d("XXXXXXXX", "onErrorResponse: " + volleyError.getMessage() );
            }
        });
        requestQueue.add(request);
    }

    /// 2019/11/30 d.sugawara modify END

    private String TAG = "sugawara";
    private void getShareMapData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        /// 2019/11/30 d.sugawara modify START
        // 共有されたあしあと取得時、パラメータを渡していない不具合を修正
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDownload + "?device_id=" + BaseApplication.getDeviceID()
                + "&type=1",
                /// 2019/11/30 d.sugawara modify END
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            mListMapShare.addAll(new Gson().fromJson(response, new TypeToken<ArrayList<MyMapShare>>(){}.getType()));
                            Log.d( "rththtrhtrhrt",response);

                            mShareMapAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: " + volleyError.toString());
                        Toast.makeText(getActivity(), getString(R.string.unable_to_display_your_map), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                params.put("device_id", BaseApplication.getDeviceID());
                params.put("type", "1");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000 && resultCode == Activity.RESULT_OK){
            //Log.d("VVVV","1") ;
            if(data!=null){
                String result = data.getStringExtra("KEY_SCAN_RESULT");
                //Log.d("VVVV","Result is: "+ result.substring(0,6)) ;
                if ( result.substring(0,6).equals("map_id")) {
                    String map_id = result.substring(7, result.indexOf("\n"));
                    //Log.d("QRString", map_id);
                    addQRCodeInfoIntoServer(map_id );
                }
                else  {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("QR Code")
                            .setMessage(data.getStringExtra("KEY_SCAN_RESULT"))
                            .setNegativeButton("Open", (dialogInterface, i) -> {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(data.getStringExtra("KEY_SCAN_RESULT")));
                                startActivity(browserIntent);
                            })
                            .setPositiveButton("Cancel", (dialogInterface, i) -> {
                            })
                            .show();
                }

            }else{
                // Log.d("VVVV","3") ;
            }
        }
    }
}