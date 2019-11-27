package com.hangtran.map.model;

import android.icu.text.SimpleDateFormat;

/**
 * 取得された位置情報
 */
public class LocalLocation {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private String        id;
    private String        device_id;   //端末の広告ID
    private double        lng;          //軽度
    private double        lat;          //緯度
    private String        stamped_at;

    public LocalLocation() {
    }

    public static String getDateFormat() {
        return DATE_FORMAT;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getStamped_at() {
        return stamped_at;
    }

    public void setStamped_at() {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        //this.stamped_at = format.format(LocalDateTime.now());
        this.stamped_at = format.format(System.currentTimeMillis());
    }

    public void setStamped_at(String date) {
        this.stamped_at = date;
    }
}

