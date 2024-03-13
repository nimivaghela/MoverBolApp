package com.moverbol.model.billOfLading;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ClockHistoryResponse {
    private ArrayList<ClockHistoryModel> history;
    private ClockHistoryModel consolidation;
    @SerializedName("charges_movetype_flag")
    private int chargesMoveTypeFlag;

    public ClockHistoryModel getConsolidation() {
        return consolidation;
    }

    public void setConsolidation(ClockHistoryModel consolidation) {
        this.consolidation = consolidation;
    }

    public ArrayList<ClockHistoryModel> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<ClockHistoryModel> history) {
        this.history = history;
    }

    public int getChargesMoveTypeFlag() {
        return chargesMoveTypeFlag;
    }

    public void setChargesMoveTypeFlag(int chargesMoveTypeFlag) {
        this.chargesMoveTypeFlag = chargesMoveTypeFlag;
    }
}
