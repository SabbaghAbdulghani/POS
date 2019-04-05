package com.example.abdulghanis.draftpos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public orderItem selectOrderItem ;
    public listOrderAdapter adpOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        general.AppMainActivity=this;
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // for notification
                Snackbar.make(view, "This section for notification edit invoice", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //login
        //Intent loginIntent=new Intent(getApplicationContext(), LoginActivity.class);
        //startActivityForResult(loginIntent, 1);
        // eng login
        InitActivity();
    }
    /*
    @Override
    protected void onDestroy(){
        super.onDestroy();
        System.exit(0);
    }
    */
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
            Intent settingIntent=new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(settingIntent);
            //return true;
        } else if (id == R.id.tbPayAcc) {
            Intent orderPayIntent=new Intent(this,OrderSaveActivity.class);
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
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            NewOrder();
                        }
                    })
                    .setNegativeButton(getString(R.string.no), null)
                    .show();


        } else if (id == R.id.tbAccounts) {

        } else if (id == R.id.tbProducts) {

        } else if (id == R.id.tbProfit) {
            Intent ProfitIntent=new Intent(getApplicationContext(), TransactionActivity.class);
            Bundle profitBundle=new Bundle();
            profitBundle.putString("transaction_type", "in");
            ProfitIntent.putExtras(profitBundle);
            startActivity(ProfitIntent);

        } else if (id == R.id.tbPayments) {
            Intent ProfitIntent=new Intent(getApplicationContext(), TransactionActivity.class);
            Bundle profitBundle=new Bundle();
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
        final ListView lv = (ListView) findViewById(R.id.lvOrderItems);
        adpOrder=new listOrderAdapter(general.ActiveOrder.Items);
        lv.setAdapter(adpOrder);
        EditText txTotal = (EditText) findViewById(R.id.txTotal);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //edit order item
                selectOrderItem=general.ActiveOrder.Items.get(position);
                editOrderProduct(selectOrderItem);
            }
        });
        txTotal.setText(String.valueOf(general.ActiveOrder.getTotalItems()));

        /*
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvItemId=(TextView)view.findViewById(R.id.tvItemId);
                selectOrderItemId=tvItemId.getText().toString();

                View.DragShadowBuilder mShadow = new View.DragShadowBuilder(view);
                ClipData.Item item = new ClipData.Item(view.getTag().toString());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    view.startDragAndDrop(data, mShadow, null, 0);
                } else {
                    view.startDrag(data, mShadow, null, 0);
                }
                return false;
            }
        });

        lv.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if(selectOrderItemId!="" && event.getX()> Float.valueOf( lv.getWidth())){
                   product _pro=general.getProductById(selectOrderItemId);
                   if(_pro!=null){
                       try {
                           _order.Items.remove(_pro);
                           InitOrdertViewOrder();
                       }catch (Exception ex){

                       }

                   }
                }
                return false;
            }
        });
        */
    }


    // Grid view adapter
    private void InitActivity()
    {
        SharedPreferences preferences=getSharedPreferences("POS_PREF",0);

        general.ViewProductMenus=preferences.getBoolean("ViewProductMenus", false);
        general.AutoEditSalesItem=preferences.getBoolean("AutoEditSalesItem", false);
        general.PhotoAlbum=preferences.getBoolean("PhotoAlbum", false);
        general.StoreCode=preferences.getString("StoreCode", "");
        general.ServiceURL=preferences.getString("ServiceURL", "");


        EditText txProduct = (EditText) findViewById(R.id.txProduct);
        txProduct.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EditText txProduct = (EditText) findViewById(R.id.txProduct);
                String str = txProduct.getText().toString();
                if (str.length() > 1) {
                    ArrayList<product> lst = general.getProductSearch(str);
                    InitProductsMenu(lst);
                }
                return false;
            }
        });


        LinearLayout lyProductMenus = (LinearLayout) findViewById(R.id.lyProductMenus);
        LinearLayout loutSelectProduct = (LinearLayout) findViewById(R.id.loutSelectProduct);
        if (general.ViewProductMenus) {
            loutSelectProduct.setVisibility(View.GONE);
        } else {
            lyProductMenus.setVisibility(View.GONE);
        }

        AutoCompleteTextView actProduct=(AutoCompleteTextView)findViewById(R.id.actProduct);
        /*
        actProduct.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        */
        if(general.ActiveOrder==null || general.Products.size()==0){
            NewOrder();
        }
        if(actProduct.getAdapter()==null){
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
                TextView tvMenuProductId = (TextView) view.findViewById(R.id.tvMenuProductId);
                product selectedProduct = general.getProductById(tvMenuProductId.getText().toString());
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
                TextView tvMenuProductId = (TextView) view.findViewById(R.id.tvMenuProductId);
                product selectedProduct = general.getProductById(tvMenuProductId.getText().toString());
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


        final AutoCompleteTextView actProduct = (AutoCompleteTextView) findViewById(R.id.actProduct);
        final String[] arrProducts = general.getProductsArray();
        ArrayAdapter<String> adpPro = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrProducts);
        //ArrayAdapter<String> adpPro = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, arrProducts);

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
            View myView = linflater.inflate(R.layout.list_row_view, null);
            TextView tvItemTitle = (TextView) myView.findViewById(R.id.tvItemTitle);
            TextView tvItemValue = (TextView) myView.findViewById(R.id.tvItemValue);
            TextView tvItemCurr = (TextView) myView.findViewById(R.id.tvItemCurr);
            TextView tvItemPrice = (TextView) myView.findViewById(R.id.tvItemPrice);
            TextView tvItemQn = (TextView) myView.findViewById(R.id.tvItemQn);
            TextView tvItemPartialQn = (TextView) myView.findViewById(R.id.tvItemPartialQn);
            TextView tvItemNotes = (TextView) myView.findViewById(R.id.tvItemNotes);
            TextView tvItemId = (TextView) myView.findViewById(R.id.tvItemId);

            orderItem item = _items.get(position);
            product _product=general.getProductById(item.product_id);
            tvItemTitle.setText(item.product_name);
            tvItemValue.setText(String.valueOf(item.getTotalPrice()));
            //tvItemPrice.setText(item.getPriceString());
            tvItemQn.setText(String.valueOf(item.Quntity) + " " + item.Unit);
            if(item.PartUnit != null && !item.PartUnit.equals("")){
                tvItemPartialQn.setText(" - " + String.valueOf(item.Quntity* _product.part_quntity)+ item.PartUnit +" - ");
            }
            tvItemPrice.setText(getString(R.string.price) + " : " + String.valueOf(item.price));
            tvItemCurr.setText(general.CurrencyName);
            tvItemNotes.setText(item.product_note);
            tvItemId.setText(item.product_id);

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
            TextView tvMenuProductId = (TextView) myView.findViewById(R.id.tvMenuProductId);
            TextView tvProductPrice = (TextView) myView.findViewById(R.id.tvProductPrice);
            ImageView imgProduct = (ImageView) myView.findViewById(R.id.imgProduct);

            product item = _items.get(position);

            tvProductTitle.setText(item.product_name);
            tvMenuProductId.setText(item.product_id);
            tvProductPrice.setText(String.valueOf(item.price1));

            try {
                if (item.logo != null && item.logo.length > 0) {
                    imgProduct.setImageBitmap(byteToBitmap(item.logo));
                } else {
                    imgProduct.setImageResource(R.drawable.label1);
                }
            } catch (Exception ex) {

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
        if(general.AutoEditSalesItem){
            editOrderProduct(item);
        }

    }

    private void editOrderProduct(orderItem item)
    {
        selectOrderItem=item;
        if(selectOrderItem!=null)
        {
            editOrderItem pop=new editOrderItem() ;
            pop.show(this.getFragmentManager(), "editOrderItem");
        }
        adpOrder.notifyDataSetChanged();
        EditText txTotal = (EditText) findViewById(R.id.txTotal);
        txTotal.setText(String.valueOf(general.ActiveOrder.getTotalItems()));
    }
    public boolean SaveOrder(){


        return  true;
    }
    private void NewOrder(){
        general.refreshProducts(loadFile("products.json"));
        general.refreshAccounts(loadFile("accounts.json"));
        general.ActiveOrder = new order();
        InitProductsCategory();
        BindOrder();

    }

    // out of busnse
    private String loadFile(String fileName) {
        try {
            InputStream fis = getAssets().open(fileName);
            //FileInputStream fis = this.openFileInput(fileName);
            String myStr = "";
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