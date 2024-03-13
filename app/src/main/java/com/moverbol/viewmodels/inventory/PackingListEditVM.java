package com.moverbol.viewmodels.inventory;

import androidx.databinding.ObservableArrayList;

import com.moverbol.model.PackingListEditPojo;

/**
 * Created by Admin on 03-10-2017.
 */

public class PackingListEditVM {
    public ObservableArrayList<PackingListEditPojo> packingListEditPojos = new ObservableArrayList<>();

    public void loadData() {
        for (int i = 1; i <= 10; i++) {
            packingListEditPojos.add(new PackingListEditPojo("51","Sofa,3 Seat","Left,Top","Left,Top"));
        }
    }
}
