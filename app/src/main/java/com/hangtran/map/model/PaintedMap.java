package com.hangtran.map.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PaintedMap {
    @SerializedName("result_code")
    @Expose
    private Integer resultCode;
    @SerializedName("maps")
    @Expose
    private ArrayList<Maps> maps = null;

    public Integer getResultCode() {
        return resultCode;
    }

    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    public ArrayList<Maps> getMaps() {
        return maps;
    }

    public void setMaps(ArrayList<Maps> maps) {
        this.maps = maps;
    }
}
