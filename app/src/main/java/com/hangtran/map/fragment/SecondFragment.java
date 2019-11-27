package com.hangtran.map.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hangtran.map.R;
import com.hangtran.map.adapter.MapViewAdapter;
import com.hangtran.map.model.Maps;

import java.util.ArrayList;

/**
 * 共有されたマップの一覧
 */
public class SecondFragment extends Fragment {

    private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/share_receive.php";

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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRcvSecondFragment.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRcvSecondFragment.setAdapter(mMapViewAdapter);

        //getShareMapData();
    }
/*
    private void getShareMapData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDownload,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null){
                            mListPaintedMap.addAll(new Gson().fromJson(response, new TypeToken<ArrayList<Maps>>(){}.getType()));

                            //Log.d("debug",mListPaintedMap.size() + "");

                            mMapViewAdapter.notifyDataSetChanged();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Log.d("fsgfdfgdfgdf",volleyError.toString());
                        Toast.makeText(getActivity(), getString(R.string.unable_to_display_your_map), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {

                // Creating Map String Params.
                Map<String, String> params = new HashMap<>();

                params.put("deviceId", BaseApplication.getDeviceID());

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mListPaintedMap = new ArrayList<>();
        mMapViewAdapter = new MapViewAdapter(mListPaintedMap);
    }
    */
}
