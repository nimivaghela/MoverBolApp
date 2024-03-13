package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 21/12/17.
 */

public class MaterialMetadatAssigned {

    @SerializedName("c_id")
    private String materialId;

    @SerializedName("c_stock_material_name")
    private String stockMaterialName;

    @SerializedName("rate")
    private String materialrate;

    @SerializedName("pack_rate")
    private String materialPackRate;

    @SerializedName("unpack_rate")
    private String materialUnpackRate;

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getStockMaterialName() {
        return stockMaterialName;
    }

    public void setStockMaterialName(String stockMaterialName) {
        this.stockMaterialName = stockMaterialName;
    }

    public String getMaterialrate() {
        return materialrate;
    }

    public void setMaterialrate(String materialrate) {
        this.materialrate = materialrate;
    }

    public String getMaterialPackRate() {
        return materialPackRate;
    }

    public void setMaterialPackRate(String materialPackRate) {
        this.materialPackRate = materialPackRate;
    }

    public String getMaterialUnpackRate() {
        return materialUnpackRate;
    }

    public void setMaterialUnpackRate(String materialUnpackRate) {
        this.materialUnpackRate = materialUnpackRate;
    }

    @Override
    public String toString() {
        return stockMaterialName;
    }
}
