package com.example.abdulghanis.draftpos;

import android.util.JsonReader;
import java.util.ArrayList;
import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public final class general {
    public static ArrayList<product> Products=new ArrayList<product>();

    public static void setProducts(ArrayList<product> products) {
        Products = products;
    }

    public static ArrayList<product> getProductCategories() {
        ArrayList<product> lst=new ArrayList<product>();
        product pro;
        for (int i=0; i< Products.size(); i++ ){
            pro=Products.get(i);
            if(pro.product_type==0){
                lst.add(pro);
            }
        }
        return lst;
    }
     public static ArrayList<product> getProducts(String parentId) {
        ArrayList<product> lst=new ArrayList<product>();
        product pro;
        for (int i=0; i< Products.size(); i++ ){
            pro=Products.get(i);
            if(pro.product_type!=0 && pro.parent_id== parentId){
                lst.add(pro);
            }
        }
        return lst;
    }
    public static product getProductById(String productId) {
        product pro;
        for (int i=0; i< Products.size(); i++ ){
            pro=Products.get(i);
            if(pro.product_type!=0 && pro.product_id== productId){
               return pro;
            }
        }
        return null;
    }

    public static void refreshProducts()
    {

    }
}
