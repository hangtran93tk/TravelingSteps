package com.hangtran.map.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * マップを作るための情報
 */
public class MyLocation {

    @SerializedName("start_date")  //開始時間
    @Expose
    private String startDate;
    @SerializedName("end_date")   //終了時間
    @Expose
    private String endDate;
    @SerializedName("device_id")  //端末ID
    @Expose
    private String deviceId;
    @SerializedName("name")       //マップの名前
    @Expose
    private String name;
    @SerializedName("locations")  //ユーザの位置
    @Expose
    private List<Location> locations = null;

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

}
