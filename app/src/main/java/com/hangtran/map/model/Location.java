package com.hangtran.map.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * マップを作るための位置情報
 */
public class Location {

    @SerializedName("lng")       //軽度
    @Expose
    private String lng;
    @SerializedName("lat")       //緯度
    @Expose
    private String lat;
    @SerializedName("stamped_at") //
    @Expose
    private String stampedAt;

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getStampedAt() {
        return stampedAt;
    }

    public void setStampedAt(String stampedAt) {
        this.stampedAt = stampedAt;
    }

}
