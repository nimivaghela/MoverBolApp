package com.moverbol.model.billOfLading;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.moverbol.BR;
import com.moverbol.model.billOfLading.newRequestChargesMoleds.ChargesUnitModel;

import java.util.ArrayList;


public class AddChargesModel extends BaseObservable {

    private String description;
    private String quantity;
    private String unit;
    private String rate;
    private String days;
    private String billing;
    public boolean showDays;
    public boolean isForStorageRecurring;
    public boolean isEdit;

    public boolean shouldShowDeleteButton;
    public boolean isFirstTimeUnitSelected;

    public ArrayList<ChargesUnitModel> unitModelList;

    /*public AddChargesItemDialogCopy.OnNewItemSubmittedListenerForStorageRecurring newItemSubmittedListenerForStorageRecurring;
    public AddChargesItemDialogCopy.OnNewItemSubmittedListener newItemSubmittedListener;
    public AddChargesItemDialogCopy.OnEditedItemSubmittedListenerForStorageRecurring editedItemSubmittedListenerForStorageRecurring;
    public AddChargesItemDialogCopy.OnEditedItemSubmittedListener editedItemSubmittedListener;
    public AddChargesItemDialogCopy.OnUnitSelectedListener onUnitSelectedListener;*/

    /*public AddChargesItemDialog.OnNewItemSubmittedListenerForStorageRecurring newItemSubmittedListenerForStorageRecurring;
    public AddChargesItemDialog.OnNewItemSubmittedListener newItemSubmittedListener;
    public AddChargesItemDialog.OnEditedItemSubmittedListenerForStorageRecurring editedItemSubmittedListenerForStorageRecurring;
    public AddChargesItemDialog.OnEditedItemSubmittedListener editedItemSubmittedListener;
    public AddChargesItemDialog.OnUnitSelectedListener onUnitSelectedListener;
*/
  /*  public AddChargesItemDialog.OnNewItemSubmittedListenerForStorageRecurring getNewItemSubmittedListenerForStorageRecurring() {
        return newItemSubmittedListenerForStorageRecurring;
    }

    public void setNewItemSubmittedListenerForStorageRecurring(AddChargesItemDialog.OnNewItemSubmittedListenerForStorageRecurring newItemSubmittedListenerForStorageRecurring) {
        this.newItemSubmittedListenerForStorageRecurring = newItemSubmittedListenerForStorageRecurring;
    }

    public AddChargesItemDialog.OnNewItemSubmittedListener getNewItemSubmittedListener() {
        return newItemSubmittedListener;
    }

    public void setNewItemSubmittedListener(AddChargesItemDialog.OnNewItemSubmittedListener newItemSubmittedListener) {
        this.newItemSubmittedListener = newItemSubmittedListener;
    }

    public AddChargesItemDialog.OnEditedItemSubmittedListenerForStorageRecurring getEditedItemSubmittedListenerForStorageRecurring() {
        return editedItemSubmittedListenerForStorageRecurring;
    }

    public void setEditedItemSubmittedListenerForStorageRecurring(AddChargesItemDialog.OnEditedItemSubmittedListenerForStorageRecurring editedItemSubmittedListenerForStorageRecurring) {
        this.editedItemSubmittedListenerForStorageRecurring = editedItemSubmittedListenerForStorageRecurring;
    }

    public AddChargesItemDialog.OnEditedItemSubmittedListener getEditedItemSubmittedListener() {
        return editedItemSubmittedListener;
    }

    public void setEditedItemSubmittedListener(AddChargesItemDialog.OnEditedItemSubmittedListener editedItemSubmittedListener) {
        this.editedItemSubmittedListener = editedItemSubmittedListener;
    }

    public AddChargesItemDialog.OnUnitSelectedListener getOnUnitSelectedListener() {
        return onUnitSelectedListener;
    }

    public void setOnUnitSelectedListener(AddChargesItemDialog.OnUnitSelectedListener onUnitSelectedListener) {
        this.onUnitSelectedListener = onUnitSelectedListener;
    }*/

    @Bindable
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        notifyPropertyChanged(BR.description);
    }

    @Bindable
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
        notifyPropertyChanged(BR.quantity);
    }


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    @Bindable
    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
        notifyPropertyChanged(BR.rate);
    }

    @Bindable
    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
        notifyPropertyChanged(BR.days);
    }

    public String getBilling() {
        return billing;
    }

    public void setBilling(String billing) {
        this.billing = billing;
    }

    public boolean isShowDays() {
        return showDays;
    }

    public void setShowDays(boolean showDays) {
        this.showDays = showDays;
    }

    public boolean isForStorageRecurring() {
        return isForStorageRecurring;
    }

    public void setForStorageRecurring(boolean forStorageRecurring) {
        isForStorageRecurring = forStorageRecurring;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public boolean isShouldShowDeleteButton() {
        return shouldShowDeleteButton;
    }

    public void setShouldShowDeleteButton(boolean shouldShowDeleteButton) {
        this.shouldShowDeleteButton = shouldShowDeleteButton;
    }

    public boolean isFirstTimeUnitSelected() {
        return isFirstTimeUnitSelected;
    }

    public void setFirstTimeUnitSelected(boolean firstTimeUnitSelected) {
        isFirstTimeUnitSelected = firstTimeUnitSelected;
    }

    public ArrayList<ChargesUnitModel> getUnitModelList() {
        return unitModelList;
    }

    public void setUnitModelList(ArrayList<ChargesUnitModel> unitModelList) {
        this.unitModelList = unitModelList;
    }
}
