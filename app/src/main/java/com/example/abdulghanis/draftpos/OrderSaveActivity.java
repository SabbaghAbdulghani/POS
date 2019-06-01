package com.example.abdulghanis.draftpos;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

public class OrderSaveActivity extends AppCompatActivity {

    private MainActivity mainAc;
    Button btCall;
    String[] arrAccounts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        arrAccounts = general.getAccountsArray();
        setContentView(R.layout.activity_order_save);
        btCall=findViewById(R.id.btCall);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

                 //this.getPackageManager()
        mainAc = (MainActivity) general.AppMainActivity;
        this.BindOrder();

        btCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
// make call
                AutoCompleteTextView actCustomer = findViewById(R.id.txCustomer);
                String _name = actCustomer.getText().toString();
                account_info acc = general.getAccountByName(_name);
                if(acc!=null) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + acc.contact2));
                    startActivity(intent);
                }
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
            setOrderValues();
            if (mainAc.SaveOrder()) {
                this.finish();
            }
        } else if (id == R.id.tbpBack) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }


    private void BindOrder() {
        arrAccounts = general.getAccountsArray();
        AutoCompleteTextView actCustomer = findViewById(R.id.txCustomer);
        //ArrayAdapter<String> adpAcc = new ArrayAdapter<String>(mainAc, android.R.layout.select_dialog_item, arrAccounts);
        ArrayAdapter<String> adpAcc = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrAccounts);
        actCustomer.setAdapter(adpAcc);
        actCustomer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _name = arrAccounts[position];
                account_info acc = general.getAccountByName(_name);
                btCall.setVisibility(view.GONE);
                if (acc != null) {
                    general.ActiveOrder.account_id = acc.account_id;
                    if(acc.contact2 !=null && !acc.contact2.equals(""))
                        btCall.setVisibility(view.VISIBLE);
                }
            }
        });


        final EditText txTotal = findViewById(R.id.txTotal);
        final EditText txDiscount = findViewById(R.id.txDiscount);
        final EditText txPaid = findViewById(R.id.txPaid);
        final EditText txUnPaid = findViewById(R.id.txUnPaid);
        EditText txNotes = findViewById(R.id.txNotes);

        txTotal.setText(String.valueOf(general.ActiveOrder.getTotalItems()));
        txDiscount.setText(String.valueOf(general.ActiveOrder.statment_discount_net));
        txPaid.setText(String.valueOf(general.ActiveOrder.Payment));
        txUnPaid.setText(String.valueOf(general.ActiveOrder.getTotalItems() - general.ActiveOrder.statment_discount_net - general.ActiveOrder.Payment));
        txNotes.setText(general.ActiveOrder.remarks);
        actCustomer.setText("");
        btCall.setVisibility(findViewById(R.id.content).GONE);
        if( general.ActiveOrder.account_id !=null && !general.ActiveOrder.account_id.equals("")) {
            //int ind = -1;
            account_info selectedAcc = general.getAccountById(general.ActiveOrder.account_id);
            if (selectedAcc != null) {
                for (int i = 0; i < arrAccounts.length; i++) {
                    if (arrAccounts[i].equals((selectedAcc.getlongName()))) {
                        //ind = i;
                        actCustomer.setText(selectedAcc.getlongName());
                        if(selectedAcc.contact2!=null && !selectedAcc.contact2.equals(""))
                            btCall.setVisibility(findViewById(R.id.content).VISIBLE);
                        break;
                    }
                }
            }
            //if (ind >= 0)
              //  actCustomer.setSelection(ind);
        }
        txDiscount.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                double discount = 0;
                if (!txDiscount.getText().toString().equals(""))
                    discount = Double.valueOf(txDiscount.getText().toString());
                double total = general.ActiveOrder.getTotalItems();
                double paid = general.ActiveOrder.Payment;
                double upPaid = total - discount - paid;
                general.ActiveOrder.statment_discount_net = discount;
                txUnPaid.setText(String.valueOf(upPaid));
                return false;
            }
        });
        txDiscount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                double discount = 0;
                if (!txDiscount.getText().toString().equals(""))
                    discount = Double.valueOf(txDiscount.getText().toString());
                double total = general.ActiveOrder.getTotalItems();
                double paid = general.ActiveOrder.Payment;
                double upPaid = total - discount - paid;
                general.ActiveOrder.statment_discount_net = discount;
                txUnPaid.setText(String.valueOf(upPaid));
            }
        });

        txPaid.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                double discount = general.ActiveOrder.statment_discount_net;
                double total = general.ActiveOrder.getTotalItems();
                double paid = 0;
                if (!txPaid.getText().toString().equals(""))
                    paid = Double.valueOf(txPaid.getText().toString());
                double upPaid = total - discount - paid;
                general.ActiveOrder.Payment = paid;
                txUnPaid.setText(String.valueOf(upPaid));
                return false;
            }
        });
        txPaid.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                double discount = general.ActiveOrder.statment_discount_net;
                double total = general.ActiveOrder.getTotalItems();
                double paid = 0;
                if (!txPaid.getText().toString().equals(""))
                    paid = Double.valueOf(txPaid.getText().toString());
                double upPaid = total - discount - paid;
                general.ActiveOrder.Payment = paid;
                txUnPaid.setText(String.valueOf(upPaid));
            }
        });
    }

    private void setOrderValues() {
        EditText txNotes = findViewById(R.id.txNotes);
        general.ActiveOrder.remarks = txNotes.getText().toString();
    }

}