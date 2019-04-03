package com.example.abdulghanis.draftpos;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.DecimalFormat;

public class editOrderItem extends DialogFragment implements View.OnClickListener {
    View view;
    private MainActivity mainAc;
    orderItem _ordedritem=null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.edit_order_item, parent, false);
        Button btn = (Button) view.findViewById(R.id.btCloseEditItem);
        btn.setOnClickListener(this);

        mainAc = (MainActivity)getActivity();

        TabHost tabhost = (TabHost) view.findViewById(R.id.tabProduct);
        tabhost.setup();
        TabHost.TabSpec tspac = tabhost.newTabSpec(getString(R.string.details));
        tspac.setContent(R.id.tabDetail);
        tspac.setIndicator(getString(R.string.details));
        tabhost.addTab(tspac);
        tspac = tabhost.newTabSpec(getString(R.string.product_info));
        tspac.setContent(R.id.tabInfo);
        tspac.setIndicator(getString(R.string.product_info));
        tabhost.addTab(tspac);

        fillOrderItem();
        return view;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
        setOrderItemValue();
        //mainAc.adpOrder.notifyDataSetChanged();
       mainAc.BindOrder();

    }

    private void fillOrderItem() {
        _ordedritem = mainAc.selectOrderItem;
        product _product = general.getProductById(_ordedritem.product_id);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        AutoCompleteTextView actSupplier = (AutoCompleteTextView) view.findViewById(R.id.actSupplier);
        final EditText etQuntity = (EditText) view.findViewById(R.id.etQuntity);
        TextView tvUnit = (TextView) view.findViewById(R.id.tvUnit);
        final EditText etPartialQuntity = (EditText) view.findViewById(R.id.etPartialQuntity);
        TextView tvSubUnit = (TextView) view.findViewById(R.id.tvSubUnit);
        final EditText etPrice = (EditText) view.findViewById(R.id.etPrice);
        TextView tvCurrency = (TextView) view.findViewById(R.id.tvCurrency);
        final EditText etTotal = (EditText) view.findViewById(R.id.etTotal);
        TextView tvTotalCurrency = (TextView) view.findViewById(R.id.tvTotalCurrency);
        EditText etNotes = (EditText) view.findViewById(R.id.etNotes);

        TextView tvInfoBarcode = (TextView) view.findViewById(R.id.tvInfoBarcode);
        TextView tvInfoStoreAvailable = (TextView) view.findViewById(R.id.tvInfoStoreAvailable);
        TextView tvInfoCost = (TextView) view.findViewById(R.id.tvInfoCost);
        TextView tvInfoPrice = (TextView) view.findViewById(R.id.tvInfoPrice);

        tvName.setText(_ordedritem.product_name);
        etQuntity.setText(String.valueOf(_ordedritem.Quntity));
        tvUnit.setText(_product.quntity_name);
        etPartialQuntity.setText(String.valueOf(_ordedritem.Quntity * _product.part_quntity));
        tvSubUnit.setText(_product.part_name);
        etPrice.setText(String.valueOf(_ordedritem.price));
        tvCurrency.setText(general.CurrencyName);
        etTotal.setText(String.valueOf(_ordedritem.getTotalPrice()));
        tvTotalCurrency.setText(general.CurrencyName);
        etNotes.setText(_ordedritem.product_note);
        tvInfoBarcode.setText(_product.product_barcode);
        tvInfoStoreAvailable.setText( String.valueOf(_product.Quntity));
        tvInfoCost.setText(String.valueOf(_product.costprice));
        tvInfoPrice.setText(String.valueOf(_product.price1));

        final String[] arrAccounts = general.getAccountsArray();
        ArrayAdapter<String> adpAcc = new ArrayAdapter<String>(mainAc, android.R.layout.select_dialog_item, arrAccounts);
        actSupplier.setAdapter(adpAcc);
        actSupplier.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String _name = arrAccounts[position];
                account_info acc = general.getAccountByName(_name);
                if (acc != null) {
                    _ordedritem.SupplierId=acc.account_id;
                }
            }
        });

        if(_ordedritem.SupplierId!=null && !_ordedritem.SupplierId.equals("")){
            account_info acc=general.getAccountById(_ordedritem.SupplierId);
            if(acc!=null)
                actSupplier.setText(acc.getlongName());
            else
                actSupplier.setText("");
        }




        etQuntity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                product pro=general.getProductById(_ordedritem.product_id);
                String str=etQuntity.getText().toString();
                if(str.equals(""))
                    str="0";
                Double Quntity=Double.valueOf(str);
                _ordedritem.Quntity=Quntity;
                _ordedritem.Quntity_piece=roundTwoDecimals(Quntity*pro.part_quntity);
                etPartialQuntity.setText(String.valueOf(_ordedritem.Quntity_piece));
                etTotal.setText(String.valueOf(_ordedritem.getTotalPrice()));
                return false;
            }
        });
        etPartialQuntity.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                product pro=general.getProductById(_ordedritem.product_id);
                String str=etPartialQuntity.getText().toString();
                if(str.equals(""))
                    str="0";
                Double PartialQuntity=Double.valueOf(str);
                if(pro.part_quntity!=0)
                    _ordedritem.Quntity=roundThreeDecimals( PartialQuntity / pro.part_quntity);
                else
                    _ordedritem.Quntity=0;
                etQuntity.setText(String.valueOf(_ordedritem.Quntity));
                etTotal.setText(String.valueOf(_ordedritem.getTotalPrice()));
                return false;
            }
        });
        etPrice.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String str=etPrice.getText().toString();
                if(str.equals(""))
                    str="0";
                 Double Price=Double.valueOf(str);
                _ordedritem.price=Price;
                etTotal.setText(String.valueOf(_ordedritem.getTotalPrice()));
                return false;
            }
        });
        etTotal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                String str=etTotal.getText().toString();
                if(str.equals(""))
                    str="0";
                Double total=Double.valueOf(str);
                if(_ordedritem.Quntity!=0)
                    _ordedritem.price=roundTwoDecimals( total/_ordedritem.Quntity);
                else
                    _ordedritem.price=total;
                etPrice.setText(String.valueOf(_ordedritem.price));
                return false;
            }
        });

    }
    private double roundTwoDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.##");
        return Double.valueOf(twoDForm.format(d));
    }
    private double roundThreeDecimals(double d)
    {
        DecimalFormat twoDForm = new DecimalFormat("#.###");
        return Double.valueOf(twoDForm.format(d));
    }
    private void setOrderItemValue() {
        product _product = general.getProductById(_ordedritem.product_id);

        TextView tvName = (TextView) view.findViewById(R.id.tvName);
        AutoCompleteTextView actSupplier = (AutoCompleteTextView) view.findViewById(R.id.actSupplier);
        EditText etQuntity = (EditText) view.findViewById(R.id.etQuntity);
        TextView tvUnit = (TextView) view.findViewById(R.id.tvUnit);
        EditText etPartialQuntity = (EditText) view.findViewById(R.id.etPartialQuntity);
        TextView tvSubUnit = (TextView) view.findViewById(R.id.tvSubUnit);
        EditText etPrice = (EditText) view.findViewById(R.id.etPrice);
        TextView tvCurrency = (TextView) view.findViewById(R.id.tvCurrency);
        EditText etTotal = (EditText) view.findViewById(R.id.etTotal);
        TextView tvTotalCurrency = (TextView) view.findViewById(R.id.tvTotalCurrency);
        EditText etNotes = (EditText) view.findViewById(R.id.etNotes);

        TextView tvInfoBarcode = (TextView) view.findViewById(R.id.tvInfoBarcode);
        TextView tvInfoStoreAvailable = (TextView) view.findViewById(R.id.tvInfoStoreAvailable);
        TextView tvInfoCost = (TextView) view.findViewById(R.id.tvInfoCost);
        TextView tvInfoPrice = (TextView) view.findViewById(R.id.tvInfoPrice);

        _ordedritem.Quntity=Double.valueOf(etQuntity.getText().toString());
        _ordedritem.Quntity_piece=_ordedritem.Quntity * _product.part_quntity;
        _ordedritem.price=Double.valueOf( etPrice.getText().toString());
        _ordedritem.product_note = etNotes.getText().toString();

    }
}
