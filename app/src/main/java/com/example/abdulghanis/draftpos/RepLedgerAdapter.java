package com.example.abdulghanis.draftpos;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RepLedgerAdapter  extends RecyclerView.Adapter<RepLedgerAdapter.RepViewHolder> {
    private ArrayList<repLedgerDataset> aarylist ;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class RepViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView tvDate, tvExplantion, tvIn,tvOut,tvBalance;
        public RepViewHolder(View v) {
            super(v);
            tvDate = v.findViewById(R.id.tvDate);
            tvExplantion = v.findViewById(R.id.tvExplantion);
            tvIn = v.findViewById(R.id.tvIn);
            tvOut = v.findViewById(R.id.tvOut);
            tvBalance = v.findViewById(R.id.tvBalance);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RepLedgerAdapter(ArrayList<repLedgerDataset> myDataset) {
        aarylist = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RepLedgerAdapter.RepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rep_row_ledger, parent, false);
        RepLedgerAdapter.RepViewHolder recycleViewHolder = new RepLedgerAdapter.RepViewHolder(view);
        return recycleViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RepLedgerAdapter.RepViewHolder holder, int position) {
        repLedgerDataset dataProvider = aarylist.get(position);
        holder.tvDate.setText(dataProvider.transaction_no);
        holder.tvExplantion.setText(String.valueOf(dataProvider.explantion));
        holder.tvIn.setText(String.valueOf(dataProvider.price_in));
        holder.tvOut.setText(String.valueOf(dataProvider.price_out));
        holder.tvBalance.setText(String.valueOf(dataProvider.new_balance));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return aarylist.size();
    }
}