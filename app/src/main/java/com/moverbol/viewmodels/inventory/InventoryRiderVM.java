package com.moverbol.viewmodels.inventory;

import androidx.databinding.ObservableArrayList;

import com.moverbol.model.InventoryRiderPojo;

/**
 * Created by Admin on 28-09-2017.
 */

public class InventoryRiderVM {
    public ObservableArrayList<InventoryRiderPojo> inventoryRiderPojos = new ObservableArrayList<>();

    public void loadData() {
        for (int i = 1; i <= 5; i++) {
            inventoryRiderPojos.add(new InventoryRiderPojo("01245", "John Smith", "09876543210"));
        }
    }
}
