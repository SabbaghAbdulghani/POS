package com.example.abdulghanis.draftpos;


import java.sql.Time;
import java.util.ArrayList;

public class transaction_header {
    public String transaction_id;
    public String period_id;
    public String branch_code;
    public int transaction_number;
    public byte transaction_sub_number;
    public Time transaction_date;
    public String currency_id;
    public double currency_value;
    public String statment_id;
    public String remarks;
    public byte build_from;// 2:In , 3:out
    public String location_create;
    public String create_user;

    public ArrayList<transaction_detail> items;

    public transaction_header() {
        items = new ArrayList<transaction_detail>();
    }

    public double oldBalance;

    public double getNewBalance() {
        if (build_from == 2)
            return oldBalance + totalItems();
        else
            return oldBalance - totalItems();
    }

    public double totalItems() {
        double x = 0;
        for (int i = 0; i < items.size(); i++) {
            x += items.get(i).price_in;
        }
        return x;
    }
}