package com.hangtran.map.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.adapter.MapViewAdapter;
import com.hangtran.map.model.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 共有されたマップの一覧
 */
public class SecondFragment extends Fragment {

    private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/list.php";

    private View            mRootView;
    private RecyclerView    mRcvSecondFragment;
    private MapViewAdapter  mMapViewAdapter;
    private ArrayList<Maps> mListPaintedMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView          = inflater.inflate(R.layout.fragment_second,container,false);
        mRcvSecondFragment =  mRootView.findViewById(R.id.recycler_view_fragment_second);
        return mRootView;
    }

    /// 2019/11/30 d.sugawara modify START\
    // リストを一度表示すると再取得できない不具合を修正
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvSecondFragment.setHasFixedSize(true);
        mRcvSecondFragment.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMapViewAdapter = new MapViewAdapter(mListPaintedMap);
        mRcvSecondFragment.setAdapter(mMapViewAdapter);
        refreshList();
    }
    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    public void refreshList() {
        mMapViewAdapter.removeAll();
        getShareMapData();
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
                            mListPaintedMap.addAll(new Gson().fromJson(response, new TypeToken<ArrayList<Maps>>(){}.getType()));
                            //Log.d(TAG, "onResponse: " + response);

                            mMapViewAdapter.notifyDataSetChanged();
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListPaintedMap = new ArrayList<>();
    }
}