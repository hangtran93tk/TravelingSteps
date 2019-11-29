package com.hangtran.map.adapter;

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
import com.hangtran.map.model.Maps;
import com.hangtran.map.utils.ScreenUtils;

import java.util.ArrayList;

//

/**
 *
 */
public class MapViewAdapter extends RecyclerView.Adapter<MapViewAdapter.ViewHolder> {

    public interface ChooseImageInterface {
        void onImageChoosen(int position);
    }

    public ArrayList<Maps> mapList;

    public MapViewAdapter(ArrayList<Maps> mapList) {
        this.mapList = mapList;
    }

    public void changeStatus(int position) {
        mapList.get(position).setChoose(!mapList.get(position).isChoose());
        notifyItemChanged(position);
    }

    public void remove() {
        while (shouldRemove()) {
            for (int i = 0; i < mapList.size(); i++) {
                if (mapList.get(i).isChoose()) {
                    mapList.remove(i);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    private Boolean shouldRemove(){
        for (int i = 0; i < mapList.size(); i++) {
            if (mapList.get(i).isChoose()) {
                return true;
            }
        }
        return false;
    }

    private ChooseImageInterface chooseImageInterface;

    public void setChooseImageInterface(ChooseImageInterface chooseImageInterface) {
        this.chooseImageInterface = chooseImageInterface;
    }

    @NonNull
    @Override
    public MapViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false);
        return new ViewHolder(itemView, chooseImageInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull MapViewAdapter.ViewHolder holder, int position) {

        String pathImage = "http://www.jz.jec.ac.jp/jecseeds/image/" + mapList.get(position).getImage() + ".png";

        Glide.with(BaseApplication.getContext())
                .load(pathImage)
                .centerCrop()
                .into(holder.ivAvatar);

        holder.tvName.setText(  mapList.get(position).getName() + "\n" +
                                mapList.get(position).getRegion() + "\n" +
                                mapList.get(position).getStartDate() + "\n" +
                                mapList.get(position).getEnd_date());

        if (mapList.get(position).isChoose()) {
            holder.icChoose.setVisibility(View.VISIBLE);
        } else {
            holder.icChoose.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mapList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView ivAvatar;
        private TextView tvName;
        private ImageView icChoose;

        public ViewHolder(View itemView, final ChooseImageInterface chooseImageInterface) {
            super(itemView);

            ivAvatar = itemView.findViewById(R.id.iv_avatar);
            tvName   = itemView.findViewById(R.id.tv_name);
            icChoose = itemView.findViewById(R.id.icChoose);
            ivAvatar.setLayoutParams(new FrameLayout.LayoutParams(ScreenUtils.getWidth()/2 - 20,ScreenUtils.getWidth()/2 - 20));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chooseImageInterface != null) {
                        chooseImageInterface.onImageChoosen(getAdapterPosition());
                    }
                }
            });
        }
    }



}
