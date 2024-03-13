package com.moverbol.model.forETA;

import com.google.gson.annotations.SerializedName;

class ETAElementDetailsValuesModel {

    @SerializedName("text")
    private String text;

    @SerializedName("value")
    private String value;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
