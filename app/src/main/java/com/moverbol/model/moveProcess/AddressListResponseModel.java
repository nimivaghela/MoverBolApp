package com.moverbol.model.moveProcess;

import com.google.gson.annotations.SerializedName;
import com.moverbol.model.AdressPojo;
import com.moverbol.model.ExtraStopsPojo;

import java.util.ArrayList;
import java.util.List;

public class AddressListResponseModel {

    @SerializedName("pickupaddress")
    private AdressPojo pickupAddress;

    @SerializedName("deliveryaddress")
    private AdressPojo deliveryAddress;

    @SerializedName("pickup-extra-stop")
    private List<ExtraStopsPojo> pickupExtraStops;

    @SerializedName("delivery-extra-stop")
    private List<ExtraStopsPojo> deliveryExtraStops;

    public AdressPojo getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(AdressPojo pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public AdressPojo getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AdressPojo deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public List<ExtraStopsPojo> getPickupExtraStops() {
        return pickupExtraStops;
    }

    public void setPickupExtraStops(List<ExtraStopsPojo> pickupExtraStops) {
        this.pickupExtraStops = pickupExtraStops;
    }

    public List<ExtraStopsPojo> getDeliveryExtraStops() {
        return deliveryExtraStops;
    }

    public void setDeliveryExtraStops(List<ExtraStopsPojo> deliveryExtraStops) {
        this.deliveryExtraStops = deliveryExtraStops;
    }


    public List<String> getAddressStringList() {
        List<String> listToReturn = new ArrayList<>();

        if(pickupAddress!=null){
            listToReturn.add(pickupAddress.getFullAddressString());
        }

        if(deliveryAddress!=null){
            listToReturn.add(deliveryAddress.getFullAddressString());
        }

        if(pickupExtraStops!=null && !pickupExtraStops.isEmpty()){
            for (int i = 0; i < pickupExtraStops.size(); i++) {
                listToReturn.add(pickupExtraStops.get(i).getFullAddressString());
            }
        }

        if(deliveryExtraStops!=null && !deliveryExtraStops.isEmpty()){
            for (int i = 0; i < deliveryExtraStops.size(); i++) {
                listToReturn.add(deliveryExtraStops.get(i).getFullAddressString());
            }
        }

        return listToReturn;
    }
}
