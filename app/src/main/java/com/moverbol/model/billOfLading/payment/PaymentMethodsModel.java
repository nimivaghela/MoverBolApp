package com.moverbol.model.billOfLading.payment;


import com.google.gson.annotations.SerializedName;

/**
 * Created by AkashM on 21/5/18.
 */
public class PaymentMethodsModel {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    @SerializedName("card_flag")
    private String cardFlag;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardFlag() {
        return cardFlag;
    }

    public void setCardFlag(String cardFlag) {
        this.cardFlag = cardFlag;
    }

    @Override
    public String toString() {
        return name;
    }
}
