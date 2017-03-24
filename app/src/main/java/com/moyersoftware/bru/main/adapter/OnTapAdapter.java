package com.moyersoftware.bru.main.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.data.OnTap;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OnTapAdapter extends RecyclerView.Adapter<OnTapAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<OnTap> mOnTapItems;

    public OnTapAdapter(Context context, ArrayList<OnTap> onTapsItems) {
        mContext = context;
        mOnTapItems = onTapsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_on_tap, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OnTap onTapItem = mOnTapItems.get(position);
        holder.mName.setText(onTapItem.getName());
        holder.mContent.setText(onTapItem.getContent());
        holder.mPrice.setText(onTapItem.getPrice());
        holder.mAmount.setText(onTapItem.getAmount());
        holder.mText.setText(onTapItem.getText());
    }

    @Override
    public int getItemCount() {
        return mOnTapItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.content)
        TextView mContent;
        @Bind(R.id.price)
        TextView mPrice;
        @Bind(R.id.amount)
        TextView mAmount;
        @Bind(R.id.text)
        TextView mText;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext, "Clicking doesn't work yet \uD83D\uDE48", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }
}