package com.moverbol.model;

/**
 * Created by Admin on 28-09-2017.
 */

public class PackingListPojo {

    public String inventory_id;
    public String agent_name;
    public String total_items;

    public PackingListPojo(String inventory_id, String agent_name, String total_items) {
        this.inventory_id = inventory_id;
        this.agent_name = agent_name;
        this.total_items = total_items;

    }
}