package com.moyersoftware.bru.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
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

    private static final int TYPE_HOURS = 0;
    private static final int TYPE_ITEM = 1;

    private final Context mContext;
    private ArrayList<OnTap> mOnTapItems;
    private String mHours;

    public OnTapAdapter(Context context, ArrayList<OnTap> onTapsItems) {
        mContext = context;
        mOnTapItems = onTapsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (viewType == TYPE_ITEM ? R.layout.item_on_tap : R.layout.item_on_tap_hours,
                        parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM) {
            assert holder.mContent != null;
            assert holder.mPrice != null;
            assert holder.mAmount != null;
            assert holder.mText != null;

            position = position - 1;

            OnTap onTapItem = mOnTapItems.get(position);
            holder.mName.setText(onTapItem.getName());
            holder.mContent.setText(onTapItem.getContent());
            holder.mPrice.setText(onTapItem.getPrice());
            holder.mAmount.setText(onTapItem.getAmount());
            holder.mText.setText(onTapItem.getText());
        } else {
            holder.mName.setText(mHours);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? TYPE_HOURS : TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        if (mOnTapItems.size() > 0) {
            return mOnTapItems.size() + 1;
        } else {
            return 0;
        }
    }

    public void setHours(String hours) {
        mHours = hours;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name)
        TextView mName;
        @Nullable
        @Bind(R.id.content)
        TextView mContent;
        @Nullable
        @Bind(R.id.price)
        TextView mPrice;
        @Nullable
        @Bind(R.id.amount)
        TextView mAmount;
        @Nullable
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