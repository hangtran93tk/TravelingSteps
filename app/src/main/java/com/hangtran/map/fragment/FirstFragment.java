package com.hangtran.map.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.cheekiat.fabmenu.FabMenu;
import com.cheekiat.fabmenu.listener.OnItemClickListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.SimpleScannerActivity;
import com.hangtran.map.adapter.MapViewAdapter;
import com.hangtran.map.model.Maps;
import com.hangtran.map.view.AddMap;
import com.hangtran.map.view.ShowMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自分で登録したマップの一覧
 */
public class FirstFragment extends Fragment {

    private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/list.php";

    private View            mRootView;
    private RecyclerView    mRcvFirstFragment;
    private MapViewAdapter  mMapViewAdapter;
    private ArrayList<Maps> mListPaintedMap = new ArrayList<>();
    private FabMenu         fabMenu;
    private Boolean         isDelete = false;
    private ImageView       mImgTrash;
    // https://stackoverflow.com/questions/2201917/how-can-i-open-a-url-in-androids-web-browser-from-my-application
    private TextView        mTvScanResult;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_first, container, false);

        initView();
        initFloatingButton();

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
                        Log.d("VVVV","StartScan");
                        // https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
                        startActivityForResult(new Intent(getActivity(), SimpleScannerActivity.class), 2000);
                        break;
                    case 1:
                        startActivity(new Intent(getActivity(), AddMap.class));
                        break;
                }
            }
        });

        mTvScanResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open browser

                // Invisible mTvScanResult
            }
        });
    }

    private void initView() {
        mRcvFirstFragment   = mRootView.findViewById(R.id.recycler_view_fragment_first);
        fabMenu             = mRootView.findViewById(R.id.fabMenu);
        mImgTrash           = mRootView.findViewById(R.id.rlTrash);
        mTvScanResult       = mRootView.findViewById(R.id.tvScanResult);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRcvFirstFragment.setHasFixedSize(true);
        mRcvFirstFragment.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMapViewAdapter = new MapViewAdapter(mListPaintedMap);
        mRcvFirstFragment.setAdapter(mMapViewAdapter);

        mImgTrash.setOnClickListener(view1 -> {
            mMapViewAdapter.remove();
        });
        mMapViewAdapter.setChooseImageInterface(new MapViewAdapter.ChooseImageInterface() {
            @Override
            public void onImageChoosen(int position) {
                if (!isDelete) {
                    // Move to detail image screen.
                    Intent intent = new Intent(getContext(), ShowMap.class);
                    intent.putExtra("Maps", mListPaintedMap.get(position));
                    startActivity(intent);
                    return;
                }
                mMapViewAdapter.changeStatus(position);
            }
        });
        getMapData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000 && resultCode == Activity.RESULT_OK){
            Log.d("VVVV","1") ;
            if(data!=null){
                Log.d("VVVV","Result is: "+data.getStringExtra("KEY_SCAN_RESULT")) ;
                mTvScanResult.setText(data.getStringExtra("KEY_SCAN_RESULT"));
            }else{
                Log.d("VVVV","3") ;
            }
        }
    }

    private void getMapData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDownload + "?device_id=" + BaseApplication.getDeviceID()
                                                                                            + "&type=0",
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

                params.put("device_id", BaseApplication.getDeviceID());
                params.put("type", "0");

                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    /**
     * 写真を削除する
     * @param isDelete
     */
    public void updateFloatActionButton(Boolean isDelete) {
        this.isDelete = isDelete;
        if (isDelete) {
            mImgTrash.setVisibility(View.VISIBLE);
            fabMenu.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < mListPaintedMap.size(); i++) {
                mListPaintedMap.get(i).setChoose(false);
                mMapViewAdapter.notifyItemChanged(i);
            }
            mImgTrash.setVisibility(View.GONE);
            fabMenu.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkHaveChosenImage() {
        for (Maps maps : mListPaintedMap) {
            if (maps.isChoose()) {
                return true;
            }
        }
        return false;
    }

}
