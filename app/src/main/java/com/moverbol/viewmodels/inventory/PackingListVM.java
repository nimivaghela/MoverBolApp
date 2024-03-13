package com.moverbol.viewmodels.inventory;

import androidx.databinding.ObservableArrayList;

import com.moverbol.model.PackingListPojo;

/**
 * Created by Admin on 28-09-2017.
 */

public class PackingListVM {
    public ObservableArrayList<PackingListPojo> packingListPojos = new ObservableArrayList<>();

    public void loadData() {

            packingListPojos.add(new PackingListPojo("01245", "John Smith", "50"));

    }
}
