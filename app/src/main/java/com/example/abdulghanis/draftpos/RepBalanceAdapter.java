package com.example.abdulghanis.draftpos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RepBalanceAdapter extends RecyclerView.Adapter<RepBalanceAdapter.RepViewHolder> {
    private ArrayList<repBalanceDataset> aarylist ;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RepViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvCol1, tvCol2;
        public RepViewHolder(View v) {
            super(v);
            tvCol1 = v.findViewById(R.id.tvCol1);
            tvCol2 = v.findViewById(R.id.tvCol2);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RepBalanceAdapter(ArrayList<repBalanceDataset> myDataset) {
        aarylist = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RepBalanceAdapter.RepViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rep_row_balance, parent, false);
        RepViewHolder recycleViewHolder = new RepViewHolder(view);
        return recycleViewHolder;
   }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RepViewHolder holder, int position) {
        repBalanceDataset dataProvider = aarylist.get(position);
        holder.tvCol1.setText(dataProvider.account_name);
        holder.tvCol2.setText(String.valueOf(dataProvider.price_net));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return aarylist.size();
    }
}