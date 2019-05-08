package com.example.abdulghanis.draftpos;

import java.util.ArrayList;
import java.util.Date;

public class order {
    protected String statment_id;
    protected int statment_number;
    protected byte statment_sub_number;
    protected byte statment_type_id;

    protected String store_code;

    protected String location_name;
    //protected Date statment_date;
    protected String remarks;
    protected double statment_discount_net;
    protected String account_id ;
    protected String VenderAccount_id ;
    protected String account_name;
    protected String currency_id ;
    protected String currency_name ;
    protected double currency_value ;
    protected double Payment ;
    protected int status;
    protected ArrayList<orderItem> Items;


    public order()
    {
        status=0;
        Items = new ArrayList<>();
    }
    protected double getTotalItems() {
        int i;
        double sum = 0;
        for(i = 0; i < Items.size(); i++)
            sum += Items.get(i).getTotalPrice();
        return sum;
    }

    public double getRestPayment()
    {
        return getTotalItems() - Payment;
    }
}
