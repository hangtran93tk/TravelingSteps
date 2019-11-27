package com.hangtran.map.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 位置情報送信した後、サーバから返したマップの情報
 */
public class Maps implements Serializable {

    @SerializedName("id")         //マップのID
    private String id;
    @SerializedName("image")      //マップの画像
    private String image;
    @SerializedName("name")       //マップの名前
    private String name;
    @SerializedName("start_date")  //開始時間
    private String startDate;
    @SerializedName("region")     //歩いた場所
    private String region;

    private boolean isDeleted = false;
    private boolean isChoose = false;

    public Maps(String id, String image, String name, String startDate, String region) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.startDate = startDate;
        this.region = region;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
