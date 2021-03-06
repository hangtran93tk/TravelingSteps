package com.hangtran.map.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.hangtran.map.BaseApplication;
import com.hangtran.map.R;
import com.hangtran.map.model.MyMapShare;
import com.hangtran.map.utils.ScreenUtils;
import com.hangtran.map.view.ShowMap2;
import java.util.ArrayList;

public class ShareMapAdapter extends RecyclerView.Adapter<ShareMapAdapter.ViewHolder> {

    private ArrayList<MyMapShare> mapList;

    public ShareMapAdapter(ArrayList<MyMapShare> mapList) {
        this.mapList = mapList;
    }

    @NonNull
    @Override
    public ShareMapAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false);
        return new ShareMapAdapter.ViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ShareMapAdapter.ViewHolder holder, int position) {

        String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + mapList.get(position).getImage() + ".png";

        Glide.with(BaseApplication.getContext())
                .load(pathImage)
                .centerCrop()
                .into(holder.ivAvatar);

        Log.d("fg4re84regrewg",mapList.get(position).getStart_date() + "");

        holder.tvName.setText(  mapList.get(position).getName() + "\n" +
                mapList.get(position).getRegion() + "\n" +
                mapList.get(position).getStart_date().substring(0,16) + "\n" +
                "From: " + mapList.get(position).getOwner());

        if (mapList.get(position).isChoose()) {
            holder.icChoose.setVisibility(View.VISIBLE);
        } else {
            holder.icChoose.setVisibility(View.GONE);
        }
    }

    /**
     * 画面上のあしあとをすべて削除する（DBから削除はしない）
     */
    public void removeAll() {
        mapList.clear();
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return mapList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvName;
        private ImageView icChoose;

        ViewHolder(View itemView) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName   = itemView.findViewById(R.id.tv_name);
            icChoose = itemView.findViewById(R.id.icChoose);
            ivAvatar.setLayoutParams(new FrameLayout.LayoutParams(ScreenUtils.getWidth()/2 - 20,ScreenUtils.getWidth()/2 - 20));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ShowMap2.class);
                    intent.putExtra("ShareMap",mapList.get(getAdapterPosition()));
                    view.getContext().startActivity(intent);
                }
            });
        }
    }
}
