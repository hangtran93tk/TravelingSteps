package com.hangtran.map.model;

import java.io.Serializable;

/**
 * 位置情報送信した後、サーバから返したマップの情報
 */
public class Maps implements Serializable {

    private int result_code;
    private String id;          //マップのID
    private String image;       //マップの画像
    private String name;        //マップの名前
    /// 2019/11/30 sugawara modify START
    /// startDate →start_date に変更。アクセサの名前も変更すること。
    private String start_date;   //開始時間
    /// 2019/11/30 sugawara modify END
    private String end_date;    //終了時間
    private String region;      //歩いた場所
    private boolean isDeleted = false;
    private boolean isChoose = false;

    public Maps(String id, String imageUrl, String end_date, String startDate, String region) {
        this.id = id;
        this.image = imageUrl;
        this.end_date = end_date;
        this.start_date = startDate;
        this.region = region;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
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

    public String getStart_date() {
        return start_date;
    }

    public void setStartDate(String startDate) {
        this.start_date = startDate;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
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