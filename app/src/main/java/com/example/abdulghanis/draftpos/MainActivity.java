package com.example.abdulghanis.draftpos;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    order _order = new order();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        testProducts();
        InitProductsCategory();
        InitOrdertViewOrder();
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
            return true;
        } else if (id == R.id.tbSave) {

        } else if (id == R.id.tbCancel) {

        } else if (id == R.id.tbAccounts) {

        } else if (id == R.id.tbProducts) {

        } else if (id == R.id.tbProfit) {

        } else if (id == R.id.tbPayments) {

        }

        return super.onOptionsItemSelected(item);
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
    }


    private void InitOrdertViewOrder() {

        ListView lv = (ListView) findViewById(R.id.lvOrderItems);
        listOrderAdapter adp = new listOrderAdapter(this._order.Items);
        lv.setAdapter(adp);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //edit order item

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
            TextView tvItemNotes = (TextView) myView.findViewById(R.id.tvItemNotes);
            TextView tvItemId = (TextView) myView.findViewById(R.id.tvItemId);

            orderItem item = _items.get(position);

            tvItemTitle.setText(item.product_name);
            tvItemValue.setText(item.getPriceString());
            tvItemNotes.setText(item.product_note);
            tvItemId.setText(item.product_id);

            return myView;
        }
    }
    ///end list view adapter

    // Grid view adapter
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



    //adding to order
    private void addOrderProduct(product _product){
        orderItem item=new orderItem() ;
        item.CurrencyEqual=1;
        item.product_id=_product.product_id;
        item.product_name=_product.product_name;
        item.price=_product.price1;
        item.Quntity=1;
        item.Quntity_piece=1;
        item.Unit=_product.quntity_name;
        item.PartUnit=_product.part_name;

        this._order.Items.add(item);
        InitOrdertViewOrder();
    }

    private void  testProducts() {
        ArrayList<product> arr=new ArrayList<product>();
        product pro=new product() ;
        pro.product_type=0;
        pro.price1=0;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="cat1";
        pro.product_code="01";
        pro.product_name="Menu1";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=0;
        pro.price1=0;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="cat2";
        pro.product_code="02";
        pro.product_name="Menu2";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=0;
        pro.price1=0;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="cat3";
        pro.product_code="03";
        pro.product_name="Menu3";
        arr.add(pro);


        pro=new product() ;
        pro.product_type=1;
        pro.price1=100;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p1";
        pro.parent_id="cat1";
        pro.product_code="0101";
        pro.product_name="Product 1 from my test";
        arr.add(pro);
        pro=new product() ;
        pro.product_type=1;
        pro.price1=120;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p2";
        pro.parent_id="cat1";
        pro.product_code="0102";
        pro.product_name="Product 2 is no item";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=1;
        pro.price1=125000;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p3";
        pro.parent_id="cat1";
        pro.product_code="0103";
        pro.product_name="Siramic graniy  jsdhakj";
        arr.add(pro);


        pro=new product() ;
        pro.product_type=1;
        pro.price1=12600;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p4";
        pro.parent_id="cat2";
        pro.product_code="0201";
        pro.product_name="سيراميك بدون لمعة شهياء 1231";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=1;
        pro.price1=10200;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p5";
        pro.parent_id="cat2";
        pro.product_code="0202";
        pro.product_name="سيراميك مع لمعة شهياء 32132";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=1;
        pro.price1=5644;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p6";
        pro.parent_id="cat2";
        pro.product_code="0203";
        pro.product_name="سيراميك 4565 مصدر اوروبي 60*60";
        arr.add(pro);

        pro=new product() ;
        pro.product_type=1;
        pro.price1=6546;
        pro.logo=new byte[0];
        pro.costprice=0;
        pro.product_id="p7";
        pro.parent_id="cat2";
        pro.product_code="0204";
        pro.product_name="سيراميك 4565 مصدر اوروبي 30*30";
        arr.add(pro);

        general.setProducts(arr);
    }
}
