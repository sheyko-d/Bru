package com.moyersoftware.bru.main.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.data.Bru;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BruAdapter extends RecyclerView.Adapter<BruAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<Bru> mBrus;

    public BruAdapter(Context context, ArrayList<Bru> brus) {
        mContext = context;
        mBrus = brus;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_bru, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Bru bru = mBrus.get(position);
        holder.mIcon.getDrawable().setColorFilter(Color.parseColor(bru.getColor()),
                PorterDuff.Mode.SRC_ATOP);
        holder.mName.setText(bru.getName());
        holder.mContent.setText(bru.getContent());
        holder.mDescription.setText(bru.getDescription());
        holder.mRating.setText(mContext.getString(R.string.rating,
                String.valueOf(bru.getRating())));
        holder.mRatingBar.setRating(bru.getRating());
    }

    @Override
    public int getItemCount() {
        return mBrus.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.icon)
        ImageView mIcon;
        @Bind(R.id.name)
        TextView mName;
        @Bind(R.id.content)
        TextView mContent;
        @Bind(R.id.rating)
        TextView mRating;
        @Bind(R.id.rating_bar)
        RatingBar mRatingBar;
        @Bind(R.id.description)
        TextView mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}