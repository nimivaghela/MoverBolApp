package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class CategoryModel {
    @SerializedName("c_id")
    private int cId;

    @SerializedName("catagory_name")
    private String categoryName;

    public int getcId() {
        return cId;
    }

    public void setcId(int cId) {
        this.cId = cId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CategoryModel)) return false;
        CategoryModel that = (CategoryModel) o;
        return getcId() == that.getcId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getcId());
    }

    @NotNull
    @Override
    public String toString() {
        return categoryName;
    }

}
