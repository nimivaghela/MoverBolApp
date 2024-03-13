package com.moverbol.model.releaseForm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.moverbol.BR;


public class AddressSelectionListItemModel extends BaseObservable {

    private String address = "";
    private boolean shouldShowAddressEmptyError;
    private boolean isSelected;

    public AddressSelectionListItemModel(String address, boolean shouldShowAddressEmptyError, boolean isSelected) {
        this.address = address;
        this.shouldShowAddressEmptyError = shouldShowAddressEmptyError;
        this.isSelected = isSelected;
    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public boolean isShouldShowAddressEmptyError() {
        return shouldShowAddressEmptyError;
    }

    public void setShouldShowAddressEmptyError(boolean shouldShowAddressEmptyError) {
        this.shouldShowAddressEmptyError = shouldShowAddressEmptyError;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
