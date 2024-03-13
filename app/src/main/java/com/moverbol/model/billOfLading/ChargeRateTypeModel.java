package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class ChargeRateTypeModel {
    @SerializedName("c_id")
    private String cId = "";
    @SerializedName(value = "c_stock_material_name", alternate = {"c_tarrif_code"})
    private String cStockMaterialName = "";
    private String taxable = "0";

    @Override
    public String toString() {
        return cStockMaterialName;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChargeRateTypeModel)) return false;
        ChargeRateTypeModel that = (ChargeRateTypeModel) o;
        return Objects.equals(getcStockMaterialName(), that.getcStockMaterialName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getcStockMaterialName());
    }

    public String getcStockMaterialName() {
        return cStockMaterialName;
    }

    public void setcStockMaterialName(String cStockMaterialName) {
        this.cStockMaterialName = cStockMaterialName;
    }

    public String getTaxable() {
        return taxable;
    }

    public void setTaxable(String taxable) {
        this.taxable = taxable;
    }
}
