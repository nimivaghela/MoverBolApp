package com.moverbol.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by AkashM on 28/11/17.
 */

public class JobDetailPojo {

    @SerializedName("customer-info")
    private CustomerInfoPojo customerInfo;

    @SerializedName("pickupaddress")
    private AdressPojo pickupadress;

    @SerializedName("pickup-extra-stop")
    private List<ExtraStopsPojo> pickupExtraStops;

    @SerializedName("deliveryaddress")
    private AdressPojo deliveryadress;

    @SerializedName("delivery-extra-stop")
    private List<ExtraStopsPojo> deliveryExtraStops;

    @SerializedName("disptach")
    private List<MoveStageDetailsPojo> moveStageDetailsPojoList;

    public List<MoveStageDetailsPojo> getMoveStageDetailsPojoList() {
        return moveStageDetailsPojoList;
    }

    public void setMoveStageDetailsPojoList(List<MoveStageDetailsPojo> moveStageDetailsPojoList) {
        this.moveStageDetailsPojoList = moveStageDetailsPojoList;
    }

    public CustomerInfoPojo getCustomerInfo() {
        return customerInfo;
    }

    public void setCustomerInfo(CustomerInfoPojo customerInfo) {
        this.customerInfo = customerInfo;
    }

    public AdressPojo getPickupadress() {
        return pickupadress;
    }

    public void setPickupadress(AdressPojo pickupadress) {
        this.pickupadress = pickupadress;
    }

    public List<ExtraStopsPojo> getPickupExtraStops() {
        return pickupExtraStops;
    }

    public void setPickupExtraStops(List<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }

    public AdressPojo getDeliveryadress() {
        return deliveryadress;
    }

    public void setDeliveryadress(AdressPojo deliveryadress) {
        this.deliveryadress = deliveryadress;
    }

    public List<ExtraStopsPojo> getDeliveryExtraStops() {
        return deliveryExtraStops;
    }

    public void setDeliveryExtraStops(List<ExtraStopsPojo> deliveryExtraStops) {
        this.deliveryExtraStops = deliveryExtraStops;
    }
}
