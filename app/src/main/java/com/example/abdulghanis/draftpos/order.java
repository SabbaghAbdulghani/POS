package com.example.abdulghanis.draftpos;

import java.util.ArrayList;
import java.util.Date;

public class order {
    public String statment_id;
    public int statment_number;
    public byte statment_sub_number;
    public byte statment_type_id;

    public String store_code;
    public Date statment_date;
    public String remarks;
    public double statment_discount_net;
    public String account_id ;
    public String VenderAccount_id ;
    public String account_name;
    public String currency_id ;
    public String currency_name ;
    public double currency_value ;
    public double Payment ;
    public ArrayList<orderItem> Items;


    public order()
    {
        Items = new ArrayList<orderItem>();
    }
    public double getTotalItems() {
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
