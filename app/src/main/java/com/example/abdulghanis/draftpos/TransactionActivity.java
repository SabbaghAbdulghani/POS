package com.example.abdulghanis.draftpos;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private ArrayList<transaction_detail> Items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button btAdd=(Button) findViewById(R.id.btAdd);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addAccountValue();

            }
        });
        InitTransaction();


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String transaction_type = extras.getString("transaction_type","in");
            if(transaction_type.equals("in")){
                toolbar.setTitle(getString(R.string.title_activity_transaction));

            }else{
                toolbar.setTitle(getString(R.string.payment_transaction));
                TextView tvAcc=(TextView)findViewById(R.id.tvAcc);
                tvAcc.setText(getString(R.string.debit_account));
            }
        }

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


        } else if (id == R.id.tbpBack) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
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
                if(hasFocus){

                }
            }
        });

    }


    private void addAccountValue(){
        AutoCompleteTextView txCustomer=(AutoCompleteTextView)findViewById(R.id.txCustomer);
        EditText txValue=(EditText) findViewById(R.id.txValue);
        EditText txDescription=(EditText) findViewById(R.id.txDescription);
        double value=0;
        if(!txValue.getText().toString().equals("")){
            value=Double.valueOf(txValue.getText().toString());
        }

        if(value==0){
            Toast.makeText(this,getString(R.string.missing_value), Toast.LENGTH_LONG ).show();
            return;
        }
        account_info acc=general.getAccountByName(txCustomer.getText().toString());
        if(acc==null){
            Toast.makeText(this,getString(R.string.missing_account), Toast.LENGTH_LONG ).show();
            return;
        }

        transaction_detail item=new transaction_detail();
        item.account_id=acc.account_id;
        item.account_name=acc.account_name;
        item.explantion=txDescription.getText().toString();
        item.price_in=value;
        Items.add(item);

        BindTransaction();
    }

    public void BindTransaction() {
        final ListView lv = (ListView) findViewById(R.id.lvTransItems);
        listTransactionAdapter adpOrder=new listTransactionAdapter(Items);
        lv.setAdapter(adpOrder);
        EditText txTotal = (EditText) findViewById(R.id.txTotal);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        double total=0;
        for(int ind=0; ind< Items.size(); ind++){
            total+=Items.get(ind).price_in;
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
            View myView = linflater.inflate(R.layout.transaction_row_view, null);
            TextView txTransAccountName = (TextView) myView.findViewById(R.id.txTransAccountName);
            TextView etTransDescription = (TextView) myView.findViewById(R.id.txTransDescription);
            TextView etTransValue = (TextView) myView.findViewById(R.id.txTransValue);

            transaction_detail item = _items.get(position);
            txTransAccountName.setText(item.account_name);
            etTransDescription.setText(item.explantion);
            etTransValue.setText(String.valueOf(item.price_in));

            return myView;
        }
    }


}
