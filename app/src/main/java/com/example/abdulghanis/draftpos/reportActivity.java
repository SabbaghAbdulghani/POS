package com.example.abdulghanis.draftpos;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Visibility;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

public class reportActivity extends AppCompatActivity {

    String ApiRepAccountBalance = "home/getAccountBalance";
    String ApiRepProductBalance = "home/getProductBalance";
    String ApiRepledger = "home/ledger?accid=";

    RecyclerView rvRows;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager manager;
    String rep_mode;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);


        rvRows = findViewById(R.id.rvRows);
        progressBar = findViewById(R.id.progressBar);
        Bundle extras = getIntent().getExtras();
        try {
            rep_mode = extras.getString("rep_mode", "acc_balance");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        TextView tvHeader1=findViewById(R.id.tvHeader1);
        TextView tvHeader2=findViewById(R.id.tvHeader2);
        TextView tvHeader3=findViewById(R.id.tvHeader3);

        general.AppMainActivity=this;
        progressBar.setVisibility(View.VISIBLE);
        switch (rep_mode) {
            case "acc_balance":
                this.setTitle(getString(R.string.accountList));
                tvHeader3.setVisibility(View.GONE);
                new general.ApiGetRequest().execute(general.ServiceURL + ApiRepAccountBalance, rep_mode);
                break;
            case "pro_balance":
                this.setTitle(getString(R.string.productsList));
                tvHeader1.setText(getString(R.string.product));
                tvHeader2.setText(getString(R.string.quntity));
                tvHeader3.setText(getString(R.string.unit));
                new general.ApiGetRequest().execute(general.ServiceURL + ApiRepProductBalance, rep_mode);
                break;
            case "leged":
                this.setTitle(getString(R.string.ledger));
                new general.ApiGetRequest().execute(general.ServiceURL + ApiRepledger + "", rep_mode);
                break;
            default:

                break;
        }


    }

    public void BindRepAccountsBalance(ArrayList<repBalanceDataset> myList){
        adapter=new RepBalanceAdapter(myList);
        rvRows.setHasFixedSize(true);
        manager=new LinearLayoutManager(this);
        rvRows.setLayoutManager(manager);
        rvRows.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    public void BindRepProductsBalance(ArrayList<repBalanceProductDataset> myList){
        adapter=new RepBalanceProductAdapter(myList);
        rvRows.setHasFixedSize(true);
        manager=new LinearLayoutManager(this);
        rvRows.setLayoutManager(manager);
        rvRows.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

}
