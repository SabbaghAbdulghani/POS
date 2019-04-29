package com.example.abdulghanis.draftpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import android.graphics.Rect;

public class MainActivity extends AppCompatActivity {

    public orderItem selectOrderItem;
    public listOrderAdapter adpOrder;
    AutoCompleteTextView actProduct;
    ListView lvOrderItems;
    String[] arrProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        general.AppMainActivity = this;
        lvOrderItems = (ListView) findViewById(R.id.lvOrderItems);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for notification
                Snackbar.make(view, "This section for notification edit invoice", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        actProduct = (AutoCompleteTextView) findViewById(R.id.actProduct);

        InitActivity();
    }
    @Override
    protected void onResume() {
        super.onResume();
        general.AppMainActivity =this;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        general.AppMainActivity=null;
        //general.ActiveOrder=null;
        //System.exit(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                //boolean result=data.getBooleanExtra("result", false);
                InitActivity();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                this.finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settingIntent = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingIntent);
            //return true;
        } else if (id == R.id.tbPayAcc) {
            Intent orderPayIntent = new Intent(this, OrderSaveActivity.class);
            //Bundle orderPayBundle=new Bundle();
            //orderPayBundle.putString("order", "");
            //orderPayIntent.putExtras(orderPayBundle);
            startActivity(orderPayIntent);

        } else if (id == R.id.tbSave) {
            SaveOrder();

        } else if (id == R.id.tbCancel) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.alert_new_order))
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewOrder();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();


        } else if (id == R.id.tbAccounts) {
            Intent repIntent = new Intent(getApplicationContext(), reportActivity.class);
            Bundle repBundle = new Bundle();
            repBundle.putString("mode", "acc_balance");
            repIntent.putExtras(repBundle);
            startActivity(repIntent);

        } else if (id == R.id.tbProducts) {
            Intent repIntent = new Intent(getApplicationContext(), reportActivity.class);
            Bundle repBundle = new Bundle();
            repBundle.putString("mode", "pro_balance");
            repIntent.putExtras(repBundle);
            startActivity(repIntent);
        } else if (id == R.id.tbAccountBalance) {
            Intent repIntent = new Intent(getApplicationContext(), reportActivity.class);
            Bundle repBundle = new Bundle();
            repBundle.putString("mode", "leged");
            repIntent.putExtras(repBundle);
            startActivity(repIntent);

        } else if (id == R.id.tbProfit) {
            Intent ProfitIntent = new Intent(getApplicationContext(), TransactionActivity.class);
            Bundle profitBundle = new Bundle();
            profitBundle.putString("transaction_type", "in");
            ProfitIntent.putExtras(profitBundle);
            startActivity(ProfitIntent);

        } else if (id == R.id.tbPayments) {
            Intent ProfitIntent = new Intent(getApplicationContext(), TransactionActivity.class);
            Bundle profitBundle = new Bundle();
            profitBundle.putString("transaction_type", "out");
            ProfitIntent.putExtras(profitBundle);
            startActivity(ProfitIntent);
        } else if (id == R.id.action_exit) {
            this.finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


    public void BindOrder() {

        for(int i=0;i<general.ActiveOrder.Items.size();i++)
            general.ActiveOrder.Items.get(i).ItemNO=i+1;
        adpOrder = new listOrderAdapter(general.ActiveOrder.Items);
        lvOrderItems.setAdapter(adpOrder);
        EditText txTotal = (EditText) findViewById(R.id.txTotal);

        lvOrderItems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //edit order item
                selectOrderItem = general.ActiveOrder.Items.get(position);
                editOrderProduct(selectOrderItem);
            }
        });
        txTotal.setText(String.valueOf(general.ActiveOrder.getTotalItems()));

    }


    // Grid view adapter
    private void InitActivity() {

        //SharedPreferences preferences = getSharedPreferences("POS_PREF", 0);
        //general.ViewProductMenus = preferences.getBoolean("ViewProductMenus", false);
        //general.AutoEditSalesItem = preferences.getBoolean("AutoEditSalesItem", false);
        //general.PhotoAlbum = preferences.getBoolean("PhotoAlbum", false);
        //general.StoreCode = preferences.getString("StoreCode", "ST1");
        //general.ServiceURL = preferences.getString("ServiceURL", "http://10.0.2.2:8011/");


        EditText txProduct =  findViewById(R.id.txProduct);
        txProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText txProduct = findViewById(R.id.txProduct);
                String str = txProduct.getText().toString();
                if (str.length() > 1) {
                    ArrayList<product> lst = general.getProductSearch(str);
                    InitProductsMenu(lst);
                }
                return false;
            }
        });


        LinearLayout lyProductMenus = findViewById(R.id.lyProductMenus);
        LinearLayout loutSelectProduct =  findViewById(R.id.loutSelectProduct);
        if (general.ViewProductMenus) {
            loutSelectProduct.setVisibility(View.GONE);
        } else {
            lyProductMenus.setVisibility(View.GONE);
        }



        actProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    arrProducts = general.getProductsArray();
                    ArrayAdapter<String> adpPro = new ArrayAdapter<String>(general.AppMainActivity, android.R.layout.simple_list_item_1, arrProducts);
                    actProduct.setAdapter(adpPro);
                }
            }
        });

        if (general.ActiveOrder == null || general.Products.size() == 0) {
            NewOrder();
        }
        if (actProduct.getAdapter() == null) {
            InitProductsCategory();
            BindOrder();
        }

    }

    private void InitProductsMenu(String parentId) {
        GridView gv = (GridView) findViewById(R.id.gvProducts);
        final ArrayList<product> products = general.getProducts(parentId);
        productsMenuAdapter adp = new productsMenuAdapter(products);
        gv.setAdapter(adp);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add item to order
                 product selectedProduct =(product) view.getTag();
                if (selectedProduct != null) {
                    addOrderProduct(selectedProduct);
                }
            }
        });
    }

    private void InitProductsMenu(ArrayList<product> products) {
        GridView gv = (GridView) findViewById(R.id.gvProducts);
        productsMenuAdapter adp = new productsMenuAdapter(products);
        gv.setAdapter(adp);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add item to order
                product selectedProduct =(product)view.getTag();
                if (selectedProduct != null) {
                    addOrderProduct(selectedProduct);
                }
            }
        });
    }

    private void InitProductsCategory() {
        GridView gv = (GridView) findViewById(R.id.gvCats);
        ArrayList<product> cats = general.getProductCategories();
        CategoryMenuAdapter adp = new CategoryMenuAdapter(cats);
        gv.setAdapter(adp);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //add item to order
                TextView tvCategoryId = (TextView) view.findViewById(R.id.tvCategoryId);
                InitProductsMenu(tvCategoryId.getText().toString());
            }
        });


        //final AutoCompleteTextView actProduct = (AutoCompleteTextView) findViewById(R.id.actProduct);
        arrProducts = general.getProductsArray();
        ArrayAdapter<String> adpPro = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrProducts);
        //////ArrayAdapter<String> adpPro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrProducts);
        actProduct.setAdapter(adpPro);

        actProduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _name = arrProducts[position];
                product pro = general.getProductByName(_name);
                if (pro != null) {
                    addOrderProduct(pro);
                    actProduct.setText("");
                }
            }
        });

    }


    // list view adapter
    class listOrderAdapter extends BaseAdapter {
        ArrayList<orderItem> _items;

        listOrderAdapter(ArrayList<orderItem> items) {
            _items = items;
        }

        @Override
        public int getCount() {
            return _items.size();
        }

        @Override
        public Object getItem(int position) {
            return this._items.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (position < _items.size() && position >= 0)
                return position;
            else
                return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater linflater = getLayoutInflater();
            View myView = linflater.inflate(R.layout.list_row_view, null);//xx
            TextView tvNO = myView.findViewById(R.id.tvNO);
            TextView tvItemTitle = myView.findViewById(R.id.tvItemTitle);
            TextView tvItemValue = myView.findViewById(R.id.tvItemValue);
            TextView tvItemCurr = myView.findViewById(R.id.tvItemCurr);
            TextView tvItemPrice = myView.findViewById(R.id.tvItemPrice);
            TextView tvItemQn = myView.findViewById(R.id.tvItemQn);
            TextView tvItemPartialQn = myView.findViewById(R.id.tvItemPartialQn);
            TextView tvItemNotes = myView.findViewById(R.id.tvItemNotes);
            TextView tvItemId = myView.findViewById(R.id.tvItemId);

            orderItem item = _items.get(position);
            product _product = general.getProductById(item.product_id);
            tvItemTitle.setText(item.product_name);
            tvItemValue.setText(String.valueOf(item.getTotalPrice()));
            tvNO.setText(String.valueOf(item.ItemNO));
            //tvItemPrice.setText(item.getPriceString());
            tvItemQn.setText((item.Quntity) + " " + item.Unit);
            if (item.PartUnit != null && !item.PartUnit.equals("")) {
                tvItemPartialQn.setText(" - " + (item.Quntity * _product.part_quntity) + item.PartUnit + " - ");
            }
            tvItemPrice.setText(getString(R.string.price) + " : " + (item.price));
            tvItemCurr.setText(general.CurrencyName);
            tvItemNotes.setText(item.product_note);
            tvItemId.setText(item.product_id);

            /*
            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TextView tvItemId=v.findViewById(R.id.tvItemId);
                    selectedItemId=tvItemId.getText().toString();

                    View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
                    //ClipData.Item item = new ClipData.Item(v.getTag().toString());
                    ClipData.Item item = new ClipData.Item(selectedItemId);
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    //ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
                    ClipData data = new ClipData(selectedItemId, mimeTypes, item);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(data, mShadow, null, 0);
                    } else {
                        v.startDrag(data, mShadow, null, 0);
                    }
                    return true;

                }
            });
 */
            return myView;
        }

    }

    ///end list view adapter
    class productsMenuAdapter extends BaseAdapter {
        ArrayList<product> _items;

        productsMenuAdapter(ArrayList<product> items) {
            _items = items;
        }

        @Override
        public int getCount() {
            return _items.size();
        }

        @Override
        public Object getItem(int position) {
            return this._items.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (position < _items.size() && position >= 0)
                return position;
            else
                return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater linflater = getLayoutInflater();
            View myView = linflater.inflate(R.layout.product_menu_view, null);
            TextView tvProductTitle = (TextView) myView.findViewById(R.id.tvProductTitle);
            TextView tvProductPrice = (TextView) myView.findViewById(R.id.tvProductPrice);
            ImageView imgProduct = (ImageView) myView.findViewById(R.id.imgProduct);

            product item = _items.get(position);

            tvProductTitle.setText(item.product_name);
            myView.setTag(item);
            tvProductPrice.setText(String.valueOf(item.price1));

            try {
                if(!item.color.equals("") && !item.color.equals("null")){
                    //imgProduct.setBackgroundColor(Long.parseLong(item.color.substring(1),16));
                    imgProduct.setBackgroundColor(Color.parseColor(item.color));
                }
                if (item.logo!=null){
                    if (item.logo.length!=0) {
                        //String urlStr=general.ServiceURL+ general.getLoginAPI+ item.product_id;
                        //URL url=new URL(urlStr);
                        //Bitmap mIcon_val = BitmapFactory.decodeStream(url.openConnection() .getInputStream());
                        //imgProduct.setImageBitmap(mIcon_val);
                        imgProduct.setImageBitmap(byteToBitmap(item.logo));
                    }
                } else {
                    imgProduct.setImageResource(R.drawable.label1);
                }
            } catch (Exception ex) {
               ex.printStackTrace();
            }

            // tvItemId.setText(item.product_id);

            return myView;
        }


    }

    class CategoryMenuAdapter extends BaseAdapter {
        ArrayList<product> _items;

        CategoryMenuAdapter(ArrayList<product> items) {
            _items = items;
        }

        @Override
        public int getCount() {
            return _items.size();
        }

        @Override
        public Object getItem(int position) {
            return this._items.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (position < _items.size() && position >= 0)
                return position;
            else
                return -1;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater linflater = getLayoutInflater();
            View myView = linflater.inflate(R.layout.category_menu, null);
            TextView tvCategoryTitle = (TextView) myView.findViewById(R.id.tvCategoryTitle);
            TextView tvCategoryId = (TextView) myView.findViewById(R.id.tvCategoryId);
            product item = _items.get(position);

            tvCategoryTitle.setText(item.product_name);
            tvCategoryId.setText(item.product_id);

            return myView;
        }


    }

    ///end grid view adapter


    //adding to order
    private void addOrderProduct(product _product) {
        orderItem item = new orderItem();
        item.CurrencyEqual = 1;
        item.product_id = _product.product_id;
        item.product_name = _product.product_name;
        item.price = _product.price1;
        item.Quntity = 1;
        item.Quntity_piece = 1;
        item.Unit = _product.quntity_name;
        item.PartUnit = _product.part_name;

        general.ActiveOrder.Items.add(item);
        BindOrder();
        if (general.AutoEditSalesItem) {
            editOrderProduct(item);
        }

    }

    private void editOrderProduct(orderItem item) {
        selectOrderItem = item;
        if (selectOrderItem != null) {
            //editOrderItem pop = new editOrderItem();
            //pop.show(this.getFragmentManager(), "edit_order_item");

            FragmentTransaction ft = getFragmentManager().beginTransaction();
            Fragment prev = getFragmentManager().findFragmentByTag("edit_order_item");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            editOrderItem editOrder = editOrderItem.newInstance(selectOrderItem);
            editOrder.show(ft, "edit_order_item");

        }
      /* adpOrder.notifyDataSetChanged();
        EditText txTotal = (EditText) findViewById(R.id.txTotal);
        txTotal.setText(String.valueOf(general.ActiveOrder.getTotalItems()));*/
    }

    public boolean SaveOrder() {
        if (general.ActiveOrder.Items.size() == 0)
        {
            Toast.makeText(this,getString(R.string.missing_value), Toast.LENGTH_LONG).show();
            return false;
        }
        //general.ActiveOrder.location_name=Build.MANUFACTURER+ " " +Build.BOARD + " "+ Build.USER;
        general.ActiveOrder.location_name=Build.MANUFACTURER + Build.HOST;
        Gson gosn = new Gson();
        String orderJson=gosn.toJson(general.ActiveOrder);
        new general.ApiGetRequest().execute(general.ServiceURL + general.saveStatmentAPI
                + general.ActiveUser.userId , "SaveStatment", orderJson);
        return true;
    }

    public  void SaveOrderResponse(String response){
        if(response.equals("OK")){
            Toast.makeText(this,getString(R.string.SavedSuccess), Toast.LENGTH_LONG).show();
            general.refreshProducts();
            general.refreshAccounts();

            NewOrder();
        }else{
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getString(R.string.app_name))
                    .setMessage(getString(R.string.savefailed) + System.getProperty("line.separator") + response)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    private void NewOrder() {
        //general.refreshProducts(loadFile("products.json"));
          InitProductsCategory();

        new general.ApiGetRequest().execute(general.ServiceURL + general.getNewStatmentAPI
                + "?userid=" + general.ActiveUser.userId + "&storeCode=" + general.StoreCode, "NewStatment");
        //general.ActiveOrder = general.NewOrder();
        //BindOrder();
    }

    // out of busnse
    private String loadFile(String fileName) {
        try {
            InputStream fis = getAssets().open(fileName);
            //FileInputStream fis = this.openFileInput(fileName);
            String myStr;
            int fsize = fis.available();
            byte[] buffer = new byte[fsize];
            fis.read(buffer);

            /*int i=0;
            while ((i=fis.read())!=-1){
                myStr+=String.valueOf((char)i);
            }*/
            fis.close();
            myStr = new String(buffer);
            return myStr;

        } catch (Exception ex) {
            return "";
        }
    }

    private void saveFile(String fileName, String body) {
        try {
            FileOutputStream fout = this.openFileOutput(fileName, MODE_PRIVATE);
            byte[] b = body.getBytes();
            fout.write(b);
            fout.flush();
            fout.close();

        } catch (Exception ex) {
            Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private Bitmap byteToBitmap(byte[] arr) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(arr);
        Bitmap bitmapProfile = BitmapFactory.decodeStream(arrayInputStream);
        return bitmapProfile;
    }

    private byte[] BitmapTobyte(Bitmap bitmapProfile) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmapProfile.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        return byteArray;
    }


}