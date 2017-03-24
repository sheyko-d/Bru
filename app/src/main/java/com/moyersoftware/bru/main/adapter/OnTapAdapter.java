package com.moyersoftware.bru.main.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.data.OnTap;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OnTapAdapter extends RecyclerView.Adapter<OnTapAdapter.ViewHolder> {

    public static final int TYPE_HOURS = 0;
    public static final int TYPE_ITEM = 1;
    public static final int TYPE_HEADER = 2;

    private final Context mContext;
    private ArrayList<OnTap> mOnTapItems;
    private ArrayList<String> mSelectedItems = new ArrayList<>();

    public OnTapAdapter(Context context, ArrayList<OnTap> onTapsItems) {
        mContext = context;
        mOnTapItems = onTapsItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (viewType == TYPE_ITEM ? R.layout.item_on_tap : (viewType == TYPE_HOURS
                                ? R.layout.item_on_tap_hours : R.layout.item_on_tap_header),
                        parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OnTap onTapItem = mOnTapItems.get(position);
        if (getItemViewType(position) == TYPE_ITEM) {
            assert holder.mContent != null;
            assert holder.mPrice != null;
            assert holder.mAmount != null;
            assert holder.mText != null;

            if (mSelectedItems.contains(onTapItem.getId())) {
                holder.mText.setMaxLines(Integer.MAX_VALUE);
            } else {
                holder.mText.setMaxLines(2);
            }

            holder.mName.setText(onTapItem.getName());
            holder.mContent.setText(onTapItem.getContent());
            holder.mPrice.setText(onTapItem.getPrice());
            holder.mAmount.setText(onTapItem.getAmount());
            holder.mText.setText(onTapItem.getText());
        } else if (getItemViewType(position) == TYPE_HOURS) {
            assert holder.mText != null;
            holder.mText.setText(onTapItem.getText());
            holder.mName.setText(onTapItem.getName());
        } else {
            holder.mName.setText(onTapItem.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mOnTapItems.get(position).getAdapterType();
    }

    @Override
    public int getItemCount() {
        return mOnTapItems.size();
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
                    if (mOnTapItems.get(getAdapterPosition()).getAdapterType() != TYPE_ITEM) {
                        return;
                    }

                    if (mSelectedItems.contains(mOnTapItems.get(getAdapterPosition()).getId())) {
                        mSelectedItems.remove(mOnTapItems.get(getAdapterPosition()).getId());
                    } else {
                        mSelectedItems.add(mOnTapItems.get(getAdapterPosition()).getId());
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}