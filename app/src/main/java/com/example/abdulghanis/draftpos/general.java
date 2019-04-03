package com.example.abdulghanis.draftpos;

import android.app.Activity;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
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
    public static Activity AppMainActivity;
    public static ArrayList<product> Products = new ArrayList<product>();
    public static ArrayList<account_info> Accounts = new ArrayList<account_info>();
    public static String CurrencyName = "SYP";
    public static sys_user ActiveUser=new sys_user();
    public static order ActiveOrder ;
    public static boolean ViewProductMenus=false;
    public static boolean AutoEditSalesItem=false;
    public static boolean PhotoAlbum=false;
    public static String StoreCode="";
    public static String ServiceURL="";


    public static void setProducts(ArrayList<product> products) {
        Products = products;
    }
    public static ArrayList<product> getProductCategories() {
        ArrayList<product> lst = new ArrayList<product>();
        product pro;
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            if (pro.product_type == 0) {
                lst.add(pro);
            }
        }
        return lst;
    }
    public static ArrayList<product> getProducts(String parentId) {
        ArrayList<product> lst = new ArrayList<product>();
        product pro;
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            if (pro.product_type != 0 && pro.parent_id.equals(parentId)) {
                lst.add(pro);
            }
        }
        return lst;
    }
    public static product getProductById(String productId) {
        product pro;
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            if (pro.product_type != 0 && pro.product_id.equals(productId)) {
                return pro;
            }
        }
        return null;
    }
    public static product getProductByName(String productName) {
        product pro;
        String str="";
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            str=pro.product_code + "-" + pro.product_name;
            if (str.equals(productName)) {
                return pro;
            }
        }
        return null;
    }
    public static ArrayList<product> getProductSearch(String pName) {
        product pro;
        ArrayList<product> lst=new ArrayList<product>();
        for (int i=0; i< Products.size(); i++ ){
            pro=Products.get(i);
            if(pro.product_type!=0 && (pro.product_code.contains(pName) || pro.product_name.contains(pName))){
                lst.add(pro);
            }
        }
        return lst;
    }
    public static String[] getProductsArray(){
        ArrayList<product>lst=getProductSearch("");
        product pro;
        String[] arr=new String[lst.size()];
        for(int i=0;i<lst.size();i++){
            pro=lst.get(i);
            arr[i]=pro.product_code + "-" + pro.product_name;
        }
        return arr;
    }
    public static void refreshProducts(String jsonStr) {
        ArrayList<product> lst = new ArrayList<product>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonArr = jsonObj.getJSONArray("products");
            product pr;
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    JSONObject c = jsonArr.getJSONObject(i);
                    pr = new product();
                    pr.product_id = c.getString("product_id");
                    pr.product_name = c.getString("product_name");
                    pr.product_name_en = c.getString("product_name_en");
                    pr.product_code = c.getString("product_code");
                    pr.product_barcode = c.getString("product_barcode");
                    pr.parent_id = c.getString("parent_id");
                    pr.product_parent = c.getString("product_parent");
                    pr.provider_accountid = c.getString("provider_accountid");
                    pr.product_type = Byte.valueOf(c.getString("product_type"));
                    pr.price1 = Double.valueOf(c.getString("price1"));
                    pr.price2 = Double.valueOf(c.getString("price2"));
                    pr.quntity_name = c.getString("quntity_name");
                    pr.part_name = c.getString("part_name");
                    if(!c.getString("part_quntity").equals(""))
                        pr.part_quntity = Integer.valueOf(c.getString("part_quntity"));
                    else
                        pr.part_quntity=1;
                    pr.color = c.getString("color");
                    pr.price_in = Double.valueOf(c.getString("price_in"));
                    pr.price_out = Double.valueOf(c.getString("price_out"));
                    pr.Quntity = Double.valueOf(c.getString("Quntity"));
                    lst.add(pr);
                } catch (Exception ex) {

                }
                // pr.logo=c.getString("logo");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Products=lst;

    }



    public static void setAccounts(ArrayList<account_info> accounts) {
        Accounts = accounts;
    }
    public static account_info getAccountById(String accountId) {
        account_info acc;
        for (int i = 0; i < Accounts.size(); i++) {
            acc = Accounts.get(i);
            if (acc.account_id.equals(accountId)) {
                return acc;
            }
        }
        return null;
    }
    public static account_info getAccountByName(String accountName) {
        account_info acc;
        String str="";
        for (int i = 0; i < Accounts.size(); i++) {
            acc = Accounts.get(i);
            str=acc.account_code + "-" + acc.account_name;
            if (str.equals(accountName)) {
                return acc;
            }
        }
        return null;
    }
    public static  ArrayList<account_info> getAccountSearch(String pName) {
        account_info acc;
        ArrayList<account_info> lst=new ArrayList<account_info>();
        for (int i=0; i< Accounts.size(); i++ ){
            acc=Accounts.get(i);
            if(acc.account_code.contains(pName) || acc.account_name.contains(pName)){
                lst.add(acc);
            }
        }
        return lst;
    }
    public static String[] getAccountsArray(){
        ArrayList<account_info>lst=getAccountSearch("");
        account_info acc;
        String[] arr=new String[lst.size()];
        for(int i=0;i<lst.size();i++){
            acc=lst.get(i);
            arr[i]=acc.getlongName();
        }
        return arr;
    }
    public static void refreshAccounts(String jsonStr) {
        ArrayList<account_info> lst = new ArrayList<account_info>();
        try {
            JSONObject jsonObj = new JSONObject(jsonStr);
            JSONArray jsonArr = jsonObj.getJSONArray("accounts");
            account_info acc;
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    JSONObject c = jsonArr.getJSONObject(i);
                    acc = new account_info();
                    acc.account_id= c.getString("account_id");
                    acc.account_name = c.getString("account_name");
                    acc.account_code = c.getString("account_code");
                    acc.account_parent = c.getString("account_parent");
                    acc.account_type =Byte.valueOf(c.getString("account_type"));
                    acc.balance =Double.valueOf( c.getString("balance"));
                    acc.parent_name = c.getString("parent_name");

                    lst.add(acc);
                } catch (Exception ex) {

                }
                // pr.logo=c.getString("logo");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Accounts=lst;

    }

}
