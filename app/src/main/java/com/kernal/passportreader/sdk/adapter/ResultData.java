package com.kernal.passportreader.sdk.adapter;

import androidx.annotation.NonNull;

public class ResultData {
    private String key="";
    private String value="";
    //目前只有护照存在
    //1:不显示，2：mrz,3:viz,4:芯片
    private int iconType = 1;



    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIconType() {
        return iconType;
    }

    public void setIconType(int iconType) {
        this.iconType = iconType;
    }

    @NonNull
    @Override
    public String toString() {
        return "ResultData{" +
                "key:"+key+","+
                "value:"+value+","+
                "iconType:"+iconType+
                "}";
    }
}
