package com.example.abdulghanis.draftpos;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;

public class transaction_header {
    protected String transaction_id;
    protected String period_id;
    protected String branch_code;
    protected int transaction_number;
    protected byte transaction_sub_number;
    protected Date transaction_date;
    protected String currency_id;
    protected double currency_value;
    protected String statment_id;
    protected String remarks;
    protected byte build_from;// 2:In , 3:out
    protected String location_create;
    protected String create_user;

    protected ArrayList<transaction_detail> items;

    protected transaction_header() {
        items = new ArrayList<transaction_detail>();
    }

    protected double oldBalance;

    protected double getNewBalance() {
        if (build_from == 2)
            return oldBalance + totalItems();
        else
            return oldBalance - totalItems();
    }

    protected double totalItems() {
        double x = 0;
        for (int i = 0; i < items.size(); i++) {
            x += items.get(i).price_in;
        }
        return x;
    }
}