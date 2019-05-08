package com.example.abdulghanis.draftpos;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class reportledgerActivity extends AppCompatActivity {

    String ApiRepledger = "home/ledger?accid=";

    RecyclerView rvRows;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reportledger);


        general.AppMainActivity = this;
        this.setTitle(getString(R.string.ledger));
        rvRows=findViewById(R.id.rvRows);
        final String[] arrAccounts = general.getAccountsArray();
        AutoCompleteTextView actAccount = findViewById(R.id.txAccount);
        ArrayAdapter<String> adpAcc = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, arrAccounts);
        actAccount.setAdapter(adpAcc);


        actAccount.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rvRows.setAdapter(null);
                String _name = arrAccounts[position];
                account_info acc = general.getAccountByName(_name);
                if (acc != null) {
                    RequestData(acc.account_id);
                }
            }
        });

    }

    private void RequestData(String accountid){
        //progressBar.setVisibility(View.VISIBLE);
         new general.ApiGetRequest().execute(general.ServiceURL + ApiRepledger+ accountid+ "&sdate=", "rep_ledger");
    }
    public void BindLedger(ArrayList<repLedgerDataset> myList){
        adapter=new RepLedgerAdapter(myList);
        rvRows.setHasFixedSize(true);
        manager=new LinearLayoutManager(this);
        rvRows.setLayoutManager(manager);
        rvRows.setAdapter(adapter);
        //progressBar.setVisibility(View.INVISIBLE);
    }
}
