package com.example.abdulghanis.draftpos;

import android.graphics.Bitmap;

public class product {
    public String product_id ;
    public String product_code ;
    public String product_name ;
    public String product_barcode ;
    public String parent_id ;
    public String provider_account_id ;
    public byte product_type ;
    public double price1 ;
    public double price2 ;
    public String quntity_name ;
    public String part_name ;
    public double part_quntity ;
    public double costprice ;
    public byte[] logo ;
    //public Bitmap bitmap;
    /*public byte[] getlogo(){
        if(logo != null && logo.length()>0) {
            char[] arrch = logo.toCharArray();
            byte[] arr = new byte[arrch.length];
            for (int i = 0; i < arrch.length; i++)
                arr[i] =(byte)arrch[i];
            return arr;
        }else{
            return new byte[0];
        }
    }*/
    public String color ;
    public double price_in ;
    public double price_out ;
    public double Quntity ;
}
