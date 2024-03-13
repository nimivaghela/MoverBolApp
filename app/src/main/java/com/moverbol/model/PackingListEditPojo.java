package com.moverbol.model;

/**
 * Created by Admin on 03-10-2017.
 */

public class PackingListEditPojo {
    public String item_no;
    public String item_name;
    public String origin_pos;
    public String dest_pos;

    public PackingListEditPojo(String item_no, String item_name, String origin_pos, String dest_pos) {
        this.item_no = item_no;
        this.item_name = item_name;
        this.origin_pos = origin_pos;
        this.dest_pos = dest_pos;
    }
}