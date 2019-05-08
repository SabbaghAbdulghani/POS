package com.example.abdulghanis.draftpos;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public final class general {
    public static Activity AppMainActivity;
    public static ArrayList<product> Products = new ArrayList<>();
    public static ArrayList<account_info> Accounts = new ArrayList<>();
    public static ArrayList<app_notification> notificationsList;
    public static String CurrencyName = "SYP";
    public static sys_user ActiveUser = new sys_user();
    public static order ActiveOrder;
    public static boolean ViewProductMenus = false;
    public static boolean AutoEditSalesItem = false;
    public static boolean PhotoAlbum = false;
    public static String StoreCode = "";
    public static String ServiceURL = "";


    private static String getImageAPI = "home/getImage?imageid=";
    private static String getAccountListAPI = "home/getAccountMiniList";
    private static String geAccountByIdAPI = "home/geAccountById?accid=";
    private static String getProductListListAPI = "home/getProductMiniList";
    private static String getProductByIdAPI = "home/getProductById?productid=";
    //public static String getNewStatmentAPI = "home/getNewStatment";//userid=&storeCode=";
    //public static String getAPIStatment = "home/geStatmentById?id=";//userid=";
    //public static String getLoginAPI = "home/login";
    //public static String saveStatmentAPI = "home/saveStatment?userid=";
    //public static String saveTransactionAPI = "home/saveTransactionInOut?userid=";
    //public static String getApiRepAccountBalance = "home/getAccountBalance";
    //public static String getApiRepProductBalance = "home/getProductBalance";
    //public static String getApiRepledger = "home/ledger?accid=";
    //public static String getApiNotification = "home/getNotification?storecode=";
    //public static String getApiCloseNotification = "home/closeNotify?storecode=";
/*

 public static ArrayList<product>  getProducts(){
        return _Products;
    }
    public static ArrayList<account_info> getAccounts(){
       return _Accounts;
    }
    public static String getCurrencyName(){
        return _CurrencyName;
    }
    public static sys_user getActiveUser(){
        return _ActiveUser;
    }
    public static void setActiveOrder(order _order){
        _ActiveOrder=_order;
    }
    public static order getActiveOrder(){
        return _ActiveOrder;
    }
    public static void setProducts(ArrayList<product> _products) {
        Products = _products;
    }
 */


    public static void setProducts(ArrayList<product> _products) {
        Products = _products;
    }

    public static ArrayList<product> getProductCategories() {
        ArrayList<product> lst = new ArrayList<>();
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
        ArrayList<product> lst = new ArrayList<>();
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
        String str;
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            str = pro.product_code + "-" + pro.product_name;
            if (str.equals(productName)) {
                return pro;
            }
        }
        return null;
    }

    public static ArrayList<product> getProductSearch(String pName) {
        product pro;
        ArrayList<product> lst = new ArrayList<>();
        for (int i = 0; i < Products.size(); i++) {
            pro = Products.get(i);
            if (pro.product_type != 0 && (pro.product_code.contains(pName) || pro.product_name.contains(pName))) {
                lst.add(pro);
            }
        }
        return lst;
    }

    public static String[] getProductsArray() {
        ArrayList<product> lst = getProductSearch("");
        product pro;
        String[] arr = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            pro = lst.get(i);
            arr[i] = pro.product_code + "-" + pro.product_name;
        }
        return arr;
    }

    public static void refreshProducts() {
        new ApiGetRequest().execute(ServiceURL + getProductListListAPI, "products");
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
        String str;
        for (int i = 0; i < Accounts.size(); i++) {
            acc = Accounts.get(i);
            str = acc.account_code + "-" + acc.account_name;
            if (str.equals(accountName)) {
                return acc;
            }
        }
        return null;
    }

    public static ArrayList<account_info> getAccountSearch(String pName) {
        account_info acc;
        ArrayList<account_info> lst = new ArrayList<>();
        for (int i = 0; i < Accounts.size(); i++) {
            acc = Accounts.get(i);
            if (acc.account_code.contains(pName) || acc.account_name.contains(pName)) {
                lst.add(acc);
            }
        }
        return lst;
    }

    public static String[] getAccountsArray() {
        ArrayList<account_info> lst = getAccountSearch("");
        account_info acc;
        String[] arr = new String[lst.size()];
        for (int i = 0; i < lst.size(); i++) {
            acc = lst.get(i);
            arr[i] = acc.getlongName();
        }
        return arr;
    }

    public static void refreshAccounts() {
        new ApiGetRequest().execute(ServiceURL + getAccountListAPI, "accounts");
    }



    public static class ApiGetRequest extends AsyncTask<String, Void, String> {
        private static final int READ_TIMEOUT = 15000;
        private static final int CONNECTION_TIMEOUT = 15000;
        private String response;
        private String ErrorMsg;
        private String extras;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            extras = params[1];
            String data = "";
            if (params.length > 2) {
                try {
                    data = params[2];
                    data = URLEncoder.encode(data, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            response = "";
            ErrorMsg = "";
            String inputLine;
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                connection = (HttpURLConnection) myUrl.openConnection();
                if (data.equals("")) {
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                } else {
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes("data=" + data);
                    wr.flush();
                    wr.close();
/*
                    OutputStream outputPost = new BufferedOutputStream(connection.getOutputStream());
                    connection.setFixedLengthStreamingMode(data.getBytes().length);
                    connection.setChunkedStreamingMode(0);
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputPost, "UTF-8"));
                    writer.write("data="+ data);
                    writer.flush();
                    */
                }
                //Set methods and timeouts
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //connection.setDoOutput(true);
                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                response = stringBuilder.toString();
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                response = "";
                ErrorMsg = e.getMessage();
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Gson gosn = new Gson();
            try {
                if (result == null || result.equals("")) {
                    if (extras.equals("authentication")) {
                        if (ErrorMsg.contains("failed")) {
                            ((LoginActivity) AppMainActivity).setServiceUrl(ErrorMsg);
                        } else {
                            ((LoginActivity) AppMainActivity).NotAuthentican();
                        }
                    }
                    //  Toast.makeText(general.AppMainActivity,"error connect API", Toast.LENGTH_LONG);
                } else if (extras.equals("accounts")) {
                    TypeToken<List<account_info>> token = new TypeToken<List<account_info>>() {
                    };
                    Accounts = gosn.fromJson(result, token.getType());
                    //general.ParseJsonAccounts(result);
                } else if (extras.equals("products")) {
                    TypeToken<List<product>> token = new TypeToken<List<product>>() {
                    };
                    Products = gosn.fromJson(result, token.getType());
                    //general.ParseJsonProducts(result);
                } else if (extras.equals("NewStatment")) {
                    ActiveOrder = gosn.fromJson(result, order.class);
                    //ParseOrder(result);
                    ((MainActivity) AppMainActivity).BindOrder();
                } else if (extras.equals("authentication")) {
                    ActiveUser = gosn.fromJson(result, sys_user.class);
                    refreshProducts();
                    refreshAccounts();
                    ((LoginActivity) AppMainActivity).startMainIntent();
                    //ParseUser(result);
                } else if (extras.equals("SaveStatment")) {
                    ((MainActivity) AppMainActivity).SaveOrderResponse(result);

                } else if (extras.equals("SaveTransaction")) {
                    ((TransactionActivity) AppMainActivity).SaveTransactionResponse(result);

                } else if (extras.equals("acc_balance")) {
                    TypeToken<List<repBalanceDataset>> token = new TypeToken<List<repBalanceDataset>>() {
                    };
                    ArrayList<repBalanceDataset> myList = gosn.fromJson(result, token.getType());
                    ((reportActivity) AppMainActivity).BindRepAccountsBalance(myList);

                } else if (extras.equals("pro_balance")) {
                    TypeToken<List<repBalanceProductDataset>> token = new TypeToken<List<repBalanceProductDataset>>() {
                    };
                    ArrayList<repBalanceProductDataset> myList = gosn.fromJson(result, token.getType());
                    ((reportActivity) AppMainActivity).BindRepProductsBalance(myList);

                } else if (extras.equals("rep_ledger")) {
                    TypeToken<List<repLedgerDataset>> token = new TypeToken<List<repLedgerDataset>>() {
                    };
                    ArrayList<repLedgerDataset> myList = (ArrayList<repLedgerDataset>) gosn.fromJson(result, token.getType());
                    ((reportledgerActivity) AppMainActivity).BindLedger(myList);

                } else if (extras.equals("notification")) {
                    TypeToken<List<app_notification>> token = new TypeToken<List<app_notification>>() {
                    };
                    notificationsList = (ArrayList<app_notification>) gosn.fromJson(result, token.getType());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

    /*
    public static void ParseJsonProducts(String jsonStr) {
        ArrayList<product> lst = new ArrayList<product>();
        try {
            JSONArray jsonArr = new JSONArray(jsonStr);
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
                    pr.product_type = Byte.valueOf(c.getString("product_type"));
                    pr.price1 = Double.valueOf(c.getString("price1"));
                    pr.price2 = Double.valueOf(c.getString("price2"));
                    pr.quntity_name = c.getString("quntity_name");
                    pr.part_name = c.getString("part_name");
                    if (!c.getString("part_quntity").equals(""))
                        pr.part_quntity = Integer.valueOf(c.getString("part_quntity"));
                    else
                        pr.part_quntity = 1;
                    pr.color = c.getString("color");
                    pr.price_in = Double.valueOf(c.getString("price_in"));
                    pr.price_out = Double.valueOf(c.getString("price_out"));
                    pr.Quntity = Double.valueOf(c.getString("Quntity"));
                    pr.provider_account_id = c.getString("provider_account_id");
                    lst.add(pr);

                } catch (Exception ex) {

                }
                // pr.logo=c.getString("logo");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Products = lst;

    }
    public static void ParseJsonAccounts(String jsonStr) {
        ArrayList<account_info> lst = new ArrayList<account_info>();
        try {
            //JSONObject jsonObj = new JSONObject(jsonStr);
            //JSONArray jsonArr = jsonObj.getJSONArray("accounts");
            JSONArray jsonArr = new JSONArray(jsonStr);
            account_info acc;
            for (int i = 0; i < jsonArr.length(); i++) {
                try {
                    JSONObject c = jsonArr.getJSONObject(i);
                    acc = new account_info();
                    acc.account_id = c.getString("account_id");
                    acc.account_name = c.getString("account_name");
                    acc.account_code = c.getString("account_code");
                    acc.account_parent = c.getString("account_parent");
                    acc.account_type = Byte.valueOf(c.getString("account_type"));
                    acc.balance = Double.valueOf(c.getString("balance"));
                    acc.parent_name = c.getString("parent_name");

                    lst.add(acc);
                } catch (Exception ex) {

                }
                // pr.logo=c.getString("logo");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Accounts = lst;

    }
    public static void ParseOrder(String jsonStr) {
        order _order = new order();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            _order.statment_id = obj.getString("statment_id");
            _order.account_id = obj.getString("account_id");
            _order.account_name = obj.getString("account_name");
            _order.currency_id = obj.getString("currency_id");
            _order.currency_name = obj.getString("currency_name");
            _order.currency_value = Double.valueOf(obj.getString("currency_value"));
            _order.remarks = obj.getString("remarks");
            _order.statment_discount_net = Double.valueOf(obj.getString("statment_discount_net"));
            _order.Payment = Double.valueOf(obj.getString("Payment"));
            _order.statment_date = new Date();
            _order.statment_number = Integer.valueOf(obj.getString("statment_number"));
            _order.statment_sub_number = Byte.valueOf(obj.getString("statment_sub_number"));
            _order.statment_type_id = Byte.valueOf(obj.getString("statment_type_id"));
            _order.store_code = obj.getString("store_code");
            _order.VenderAccount_id = obj.getString("VenderAccount_id");
            _order.Items = new ArrayList<orderItem>();

            JSONArray jsonArr = new JSONArray(obj.getString("Items"));
            orderItem item;
            for (int i = 0; i < jsonArr.length(); i++) {
                JSONObject c = jsonArr.getJSONObject(i);
                item = new orderItem();
                item.Id = c.getString("Id");
                item.product_id = c.getString("product_id");
                item.product_name = c.getString("product_name");
                item.product_note = c.getString("product_note");
                item.SupplierId = c.getString("SupplierId");
                item.Unit = c.getString("Unit");
                item.PartUnit = c.getString("PartUnit");
                item.price = Double.valueOf(c.getString("price"));
                item.Quntity = Double.valueOf(c.getString("Quntity"));
                item.Quntity_piece = Double.valueOf(c.getString("Quntity_piece"));
                item.RatePartialQuntity = Double.valueOf(c.getString("RatePartialQuntity"));
                item.CurrencyEqual = Double.valueOf(c.getString("CurrencyEqual"));
                item.BasePrice = Double.valueOf(c.getString("BasePrice"));

                _order.Items.add(item);
            }
            ActiveOrder = _order;


        } catch (Exception ex) {

        }
    }
    public static void ParseUser(String jsonStr) {
        sys_user user = new sys_user();
        try {
            JSONObject obj = new JSONObject(jsonStr);
            user.userId = obj.getString("userId");
            user.userName = obj.getString("userName");
            user.fullName = obj.getString("fullName");
            user.cashBox_id = obj.getString("cashBox_id");
            user.isViewPayment = Boolean.parseBoolean(obj.getString("isViewPayment"));
            user.isViewReports = Boolean.parseBoolean(obj.getString("isViewReports"));
            ActiveUser = user;
        } catch (Exception ex) {

        }
    }
*/


}
