package com.moverbol.model.forETA;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class ETAElementModel {

    @SerializedName("elements")
    private List<ETAElementDetailsModel> elementDetailsModelList;

    public List<ETAElementDetailsModel> getElementDetailsModelList() {
        return elementDetailsModelList;
    }

    public void setElementDetailsModelList(List<ETAElementDetailsModel> elementDetailsModelList) {
        this.elementDetailsModelList = elementDetailsModelList;
    }
}
