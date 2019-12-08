package com.hangtran.map.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cheekiat.fabmenu.FabMenu;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.SimpleScannerActivity;
import com.hangtran.map.adapter.MyMapAdapter;
import com.hangtran.map.model.Maps;
import com.hangtran.map.view.AddMap;
import com.hangtran.map.view.ShowMap;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 自分で登録したマップの一覧
 */
public class FirstFragment extends Fragment {

    private static final String urlDownload = "http://www.jz.jec.ac.jp/jecseeds/footprint/list.php";
    private static final String urlUpload   = "http://www.jz.jec.ac.jp/jecseeds/footprint/share_receive.php";

    private View            mRootView;
    private RecyclerView    mRcvFirstFragment;
    private MyMapAdapter    mMyMapAdapter;
    private ArrayList<Maps> mListPaintedMap = new ArrayList<>();
    private FabMenu         fabMenu;
    private Boolean         isDelete = false;
    private ImageView       mImgTrash;

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

        fabMenu.setOnItemClickListener(i -> {
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
        });
    }

    private void addQRCodeInfoIntoServer(String map_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        Map<String, String> postParams = new HashMap<>();

        postParams.put("map_id"           , map_id);
        postParams.put("device_id"        , BaseApplication.getDeviceID());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlUpload, new JSONObject(postParams),
                (serverResponse) -> {
                    try {
                        int result = serverResponse.getInt("result_code");
                        if (result != 0) {
                            Toast.makeText(getContext(), serverResponse.getString("error_message"), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),getString(R.string.get_map_completed), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(getContext(), getString(R.string.err_got_invalidresponse), Toast.LENGTH_LONG).show();
                    }
                }, (volleyError) -> {
            String message = null;
            if (volleyError instanceof NetworkError) {
                message = getString(R.string.err_unreachable_server);
            } else if (volleyError instanceof ServerError) {
                message = getString(R.string.err_server_notfound);
            } else if (volleyError instanceof TimeoutError) {
                message = getString(R.string.err_timeout);
            } else {
                message = getString(R.string.map_registration_failed);
            }
            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        });
        requestQueue.add(request);
    }

    private void initView() {
        mRcvFirstFragment   = mRootView.findViewById(R.id.recycler_view_fragment_first);
        fabMenu             = mRootView.findViewById(R.id.fabMenu);
        mImgTrash           = mRootView.findViewById(R.id.rlTrash);
    }
    // リストを一度表示すると再取得できない不具合を修正
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRcvFirstFragment.setHasFixedSize(true);
        mRcvFirstFragment.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        mMyMapAdapter = new MyMapAdapter(mListPaintedMap);
        mRcvFirstFragment.setAdapter(mMyMapAdapter);
//        refreshList();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshList();
    }

    private String TAG = "sugawara";
    public void refreshList() {
        mMyMapAdapter.removeAll();
        mImgTrash.setOnClickListener(view1 -> {
            mMyMapAdapter.remove();
        });
        mMyMapAdapter.setChooseImageInterface(new MyMapAdapter.ChooseImageInterface() {
            @Override
            public void onImageChoosen(int position) {
                if (!isDelete) {
                    // Move to detail image screen.
                    Intent intent = new Intent(getContext(), ShowMap.class);
                    intent.putExtra("Maps", mListPaintedMap.get(position));
                    startActivity(intent);
                    return;
                }
                mMyMapAdapter.changeStatus(position);
            }
        });
        getMapData();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 2000 && resultCode == Activity.RESULT_OK){
            if(data!=null){
                String result = data.getStringExtra("KEY_SCAN_RESULT");
                if ( result.substring(0,6).equals("map_id")) {
                    String map_id = result.substring(7, result.indexOf("\n"));
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

            }
        }
    }

    private void getMapData() {
        Log.d(TAG, "getMapData: " + "requested");
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, urlDownload + "?device_id=" + BaseApplication.getDeviceID() + "&type=0",
                (response) -> {
                    if (response != null){
                        mListPaintedMap.addAll(new Gson().fromJson(response, new TypeToken<ArrayList<Maps>>(){}.getType()));
                        mMyMapAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getContext(), getString(R.string.err_got_invalidresponse) + "(no response)", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onResponse: null received!!");
                    }
                },
                (volleyError) -> {
                    String message = null;
                    if (volleyError instanceof NetworkError) {
                        message = getString(R.string.err_unreachable_server);
                    } else if (volleyError instanceof ServerError) {
                        message = getString(R.string.err_server_notfound);
                    } else if (volleyError instanceof TimeoutError) {
                        message = getString(R.string.err_timeout);
                    } else {
                        message = getString(R.string.map_registration_failed);
                    }
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
                mMyMapAdapter.notifyItemChanged(i);
            }
            mImgTrash.setVisibility(View.GONE);
            fabMenu.setVisibility(View.VISIBLE);
        }
    }
}