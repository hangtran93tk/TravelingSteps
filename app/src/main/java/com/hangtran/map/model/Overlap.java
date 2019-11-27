package com.hangtran.map.model;

import java.io.Serializable;

public class Overlap implements Serializable {
    private int result_code;
    private String image;

    public Overlap(int result_code, String image) {
        this.result_code = result_code;
        this.image = image;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
