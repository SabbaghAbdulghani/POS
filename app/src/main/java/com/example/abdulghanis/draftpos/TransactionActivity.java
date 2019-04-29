package com.example.abdulghanis.draftpos;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.DragEvent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class TransactionActivity extends AppCompatActivity {

    private transaction_header transaction = new transaction_header();
    String transaction_type = "in";
    Toolbar toolbar;
    LinearLayout layListParent;
    ListView lvTransItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btAdd = (Button) findViewById(R.id.btAdd);
        layListParent = findViewById(R.id.layListParent);
        lvTransItems = findViewById(R.id.lvTransItems);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccountValue();

            }
        });
        InitTransaction();


        Bundle extras = getIntent().getExtras();

        transaction.transaction_number = 0;
        if (extras != null) {
            transaction_type = extras.getString("transaction_type", "in");
        }
        if (!transaction_type.equals("in")) {
            this.setTitle(R.string.payments);
            layListParent.setBackgroundColor(0xA0f39200);
        }
        else{
            layListParent.setBackgroundColor(0xAA4F77B9);
        }
        general.AppMainActivity = this;
        newTransaction();

        layListParent.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                if (event.getAction() == DragEvent.ACTION_DROP) {
                    if (v != lvTransItems) {
                        try {
                            int itemIndex = Integer.valueOf(event.getClipData().getItemAt(0).getText().toString());
                            transaction_detail trans = null;
                            for (int ind = 0; ind < transaction.items.size(); ind++) {
                                if (transaction.items.get(ind).itemIndex == itemIndex)
                                    trans = transaction.items.get(ind);
                            }
                            if (trans != null) {
                                transaction.items.remove(trans);
                                BindTransaction();
                                return true;
                            }

                        } catch (Exception ex) {

                        }
                    }
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_payacc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.tbpSave) {
            //save transaction
            if (transaction.items.size() == 0)
            {
                Toast.makeText(this,getString(R.string.missing_value), Toast.LENGTH_LONG).show();
                return false;
            }


            transaction.location_create=Build.MANUFACTURER;
            Gson gosn = new Gson();
            String TransactionJson=gosn.toJson(transaction);
            new general.ApiGetRequest().execute(general.ServiceURL + general.saveTransactionAPI
                    + general.ActiveUser.userId , "SaveTransaction", TransactionJson);

        } else if (id == R.id.tbpBack) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public  void SaveTransactionResponse(String response){
        if(response.equals("OK")){
            Toast.makeText(this,getString(R.string.SavedSuccess), Toast.LENGTH_LONG).show();
            newTransaction();
            BindTransaction();
            general.refreshAccounts();
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



    private void InitTransaction() {
        final String[] arrAccounts = general.getAccountsArray();
        final AutoCompleteTextView actCustomer = (AutoCompleteTextView) findViewById(R.id.txCustomer);
        ArrayAdapter<String> adpAcc = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, arrAccounts);
        actCustomer.setAdapter(adpAcc);
        actCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        actCustomer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                }
            }
        });

    }

    private void newTransaction() {

        transaction = new transaction_header();
        if (transaction_type.equals("in")) {
            toolbar.setTitle(getString(R.string.title_activity_transaction));
            transaction.build_from = 2;
        } else {
            toolbar.setTitle(getString(R.string.payment_transaction));
            TextView tvAcc = (TextView) findViewById(R.id.tvAcc);
            tvAcc.setText(getString(R.string.debit_account));
            transaction.build_from = 3;
        }
        transaction.transaction_id="";
        transaction.transaction_number = 0;
        transaction.transaction_sub_number = 0;
        transaction.branch_code ="";// general.StoreCode;
        transaction.create_user = general.ActiveUser.userName;
        transaction.location_create = "";
        account_info acc = general.getAccountById(general.ActiveUser.cashBox_id);
        if (acc != null) {
            transaction.oldBalance = acc.balance;
        }
        transaction.transaction_id = "00000000-0000-0000-0000-000000000000";
        transaction.transaction_date= new Date();
    }

    private void addAccountValue() {
        AutoCompleteTextView txCustomer = (AutoCompleteTextView) findViewById(R.id.txCustomer);
        EditText txValue = (EditText) findViewById(R.id.txValue);
        EditText txDescription = (EditText) findViewById(R.id.txDescription);
        double value = 0;
        if (!txValue.getText().toString().equals("")) {
            value = Double.valueOf(txValue.getText().toString());
        }

        if (value <= 0) {
            Toast.makeText(this, getString(R.string.missing_value), Toast.LENGTH_LONG).show();
            return;
        }
        account_info acc = general.getAccountByName(txCustomer.getText().toString());
        if (acc == null) {
            Toast.makeText(this, getString(R.string.missing_account), Toast.LENGTH_LONG).show();
            return;
        }

        transaction_detail item = new transaction_detail();
        item.account_id = acc.account_id;
        item.account_name = acc.account_name;
        item.explantion = txDescription.getText().toString();
        item.price_in = value;
        transaction.items.add(item);

        BindTransaction();

        txCustomer.setText("");
        txValue.setText("");
        txDescription.setText("");

    }

    public void BindTransaction() {
        final ListView lv = (ListView) findViewById(R.id.lvTransItems);
        listTransactionAdapter adpOrder = new listTransactionAdapter(transaction.items);
        lv.setAdapter(adpOrder);
        EditText txTotal = (EditText) findViewById(R.id.txTotal);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        double total = 0;
        for (int ind = 0; ind < transaction.items.size(); ind++) {
            total += transaction.items.get(ind).price_in;
            transaction.items.get(ind).itemIndex = ind + 1;
        }
        txTotal.setText(String.valueOf(total));

    }


    class listTransactionAdapter extends BaseAdapter {
        ArrayList<transaction_detail> _items;

        listTransactionAdapter(ArrayList<transaction_detail> items) {
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
            final View myView = linflater.inflate(R.layout.transaction_row_view, null);
            TextView txTransAccountName = (TextView) myView.findViewById(R.id.txTransAccountName);
            TextView etTransDescription = (TextView) myView.findViewById(R.id.txTransDescription);
            TextView etTransValue = (TextView) myView.findViewById(R.id.txTransValue);

            transaction_detail item = _items.get(position);
            txTransAccountName.setText(item.account_name);
            etTransDescription.setText(item.explantion);
            etTransValue.setText(String.valueOf(item.price_in));
            myView.setTag(item);



            myView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    transaction_detail trans = (transaction_detail) myView.getTag();

                    View.DragShadowBuilder mShadow = new View.DragShadowBuilder(v);
                    ClipData.Item item = new ClipData.Item(String.valueOf(trans.itemIndex));
                    String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                    //ClipData data = new ClipData(v.getTag().toString(), mimeTypes, item);
                    ClipData data = new ClipData(String.valueOf(trans.itemIndex), mimeTypes, item);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        v.startDragAndDrop(data, mShadow, null, 0);
                    } else {
                        v.startDrag(data, mShadow, null, 0);
                    }
                    return true;

                }
            });

            return myView;
        }
    }
}
