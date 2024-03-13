package com.moverbol.model.valuationPage;

import android.text.TextUtils;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.gson.annotations.SerializedName;
import com.moverbol.BR;
import com.moverbol.constants.Constants;
import com.moverbol.util.Util;

/**
 * Created by AkashM on 30/1/18.
 */

public class ValuationItemPojo extends BaseObservable {

    @SerializedName("r_id")
    private String id;

    @SerializedName("r_label")
    private String label;

    @SerializedName("r_description")
    private String description;

    @SerializedName("unit_name")
    private String unit;

    @SerializedName("r_rate")
    private String rate;

    @SerializedName("r_unit")
    private String unitId;

    @SerializedName("weight")
    private String declaredValue = "";

    @SerializedName("unit_num")
    private String unitNum = "1";

    public String getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    private boolean selected = false;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    @Bindable
    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @Bindable
    public String getDeclaredValue() {
        return declaredValue;
    }

    public void setDeclaredValue(String declaredValue) {
        this.declaredValue = declaredValue;
        notifyPropertyChanged(BR.declaredValue);
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getAmount() {

        double total = 0;

        if (TextUtils.equals(label.trim().toLowerCase(), Constants.VALUACTION_LABEL_TO_IGNORE.trim().toLowerCase())
                || unit == null) {
            return "";
        }

        if (unit.equals("0.60/lb")) {
            return String.valueOf(total);
        }

        //Returning unit as one in default case of because unit is used here for division.
        double rateNum = Util.getDoubleFromString(rate);
        double quantNum = Util.getDoubleFromString(declaredValue);

        /**
         * 3 type of units are possible for unit. If unit is percentage then rate is to be converted
         * into percentage. Other two are handled in unitNum object assignment.
         */
        if (unit.trim().equalsIgnoreCase("Percentage".trim())) {
            rateNum = (quantNum * rateNum) / 100;
        }

        total = (rateNum * quantNum) / Util.getDoubleFromString(getUnitNum());

        return total + "";
    }

}
