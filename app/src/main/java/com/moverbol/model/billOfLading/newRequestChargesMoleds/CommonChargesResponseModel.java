    package com.moverbol.model.billOfLading.newRequestChargesMoleds;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/**
 * Created by AkashM on 19/4/18.
 */
public class CommonChargesResponseModel {

    /*@SerializedName("charge")
    private ArrayList ;*/

    @SerializedName("unit")
    private ArrayList<ChargesUnitModel> unitModelList;

    @SerializedName("charge")
    private ArrayList<CommonChargesRequestModel> commonChargesRequestModelArrayList;

    @SerializedName("packing_charge_sales_tax")
    private double packingSalesTax;

//    @SerializedName("billing")


    public ArrayList<CommonChargesRequestModel> getCommonChargesRequestModelArrayList() {
        return commonChargesRequestModelArrayList;
    }

    public void setCommonChargesRequestModelArrayList(ArrayList<CommonChargesRequestModel> commonChargesRequestModelArrayList) {
        this.commonChargesRequestModelArrayList = commonChargesRequestModelArrayList;
    }

    public ArrayList<ChargesUnitModel> getUnitModelList() {
        return unitModelList;
    }

    public void setUnitModelList(ArrayList<ChargesUnitModel> unitModelList) {
        this.unitModelList = unitModelList;
    }

    public double getPackingSalesTax() {
        return packingSalesTax;
    }

    public void setPackingSalesTax(double packingSalesTax) {
        this.packingSalesTax = packingSalesTax;
    }
}
