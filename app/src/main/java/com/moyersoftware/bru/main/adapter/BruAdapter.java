package com.moyersoftware.bru.main.adapter;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.BruFragment;
import com.moyersoftware.bru.main.data.Bru;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BruAdapter extends RecyclerView.Adapter<BruAdapter.ViewHolder> {

    private final BruFragment mFragment;
    private ArrayList<Bru> mBrus;
    private ArrayList<String> mSelectedItems = new ArrayList<>();
    private ArrayList<String> mChangeRatingItems = new ArrayList<>();

    public BruAdapter(BruFragment fragment, ArrayList<Bru> brus) {
        mFragment = fragment;
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
        holder.mIcon.getDrawable().mutate().clearColorFilter();
        holder.mIcon.getDrawable().setColorFilter(Color.parseColor(bru.getColor()),
                PorterDuff.Mode.SRC_ATOP);
        holder.mName.setText(bru.getName());
        holder.mContent.setText(bru.getContent());
        holder.mDescription.setText(bru.getDescription());

        if (mChangeRatingItems.contains(bru.getId())) {
            holder.mRating.setText(mFragment.getString(R.string.rating,
                    String.valueOf(bru.getRating())));
            holder.mRating.setTypeface(null, Typeface.NORMAL);
            holder.mRatingBar.setVisibility(View.VISIBLE);
            holder.mRatingBar.setRating(bru.getRating());
            holder.mMyRating.setVisibility(View.GONE);
            holder.mVotes.setVisibility(View.GONE);
        } else if (bru.getMyRating() != null) {
            holder.mMyRating.setVisibility(View.VISIBLE);
            holder.mMyRating.setText(String.valueOf(bru.getRating()));
            holder.mRatingBar.setVisibility(View.GONE);
            holder.mRating.setText(R.string.change_rating);
            holder.mRating.setTypeface(null, Typeface.ITALIC);
            holder.mVotes.setVisibility(View.VISIBLE);
            holder.mVotes.setText(mFragment.getString(R.string.votes, bru.getVotes(),
                    mFragment.getResources().getQuantityString(R.plurals.votes, bru.getVotes())));
        } else {
            holder.mMyRating.setVisibility(View.GONE);
            holder.mRatingBar.setVisibility(View.VISIBLE);
            holder.mRatingBar.setRating(0);
            if (bru.getRating() != 0) {
                holder.mRating.setText(mFragment.getString(R.string.rating,
                        String.valueOf(bru.getRating())));
            } else {
                holder.mRating.setText(R.string.no_ratings);
            }
            holder.mRating.setTypeface(null, Typeface.NORMAL);
            holder.mVotes.setVisibility(View.GONE);
        }

        if (mSelectedItems.contains(bru.getId())) {
            holder.mDescription.setMaxLines(Integer.MAX_VALUE);
        } else {
            holder.mDescription.setMaxLines(2);
        }
    }

    @Override
    public int getItemCount() {
        return mBrus.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.icon)
        ImageView mIcon;
        @BindView(R.id.name)
        TextView mName;
        @BindView(R.id.content)
        TextView mContent;
        @BindView(R.id.rating)
        TextView mRating;
        @BindView(R.id.rating_bar)
        RatingBar mRatingBar;
        @BindView(R.id.my_rating)
        TextView mMyRating;
        @BindView(R.id.votes)
        TextView mVotes;
        @BindView(R.id.description)
        TextView mDescription;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mSelectedItems.contains(mBrus.get(getAdapterPosition()).getId())) {
                        mSelectedItems.remove(mBrus.get(getAdapterPosition()).getId());
                    } else {
                        mSelectedItems.add(mBrus.get(getAdapterPosition()).getId());
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(final RatingBar ratingBar, final float rating,
                                            boolean fromUser) {
                    if (!fromUser) return;

                    if (mChangeRatingItems.contains(mBrus.get(getAdapterPosition()).getId())) {
                        mChangeRatingItems.remove(mBrus.get(getAdapterPosition()).getId());
                    }

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder
                            (mFragment.getActivity(), R.style.MaterialDialog);
                    dialogBuilder.setMessage("Rate this beer " + rating + " stars?");
                    dialogBuilder.setNegativeButton("Cancel", new DialogInterface
                            .OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ratingBar.setRating(0);
                        }
                    });
                    dialogBuilder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mFragment.rateBru(getAdapterPosition(), rating);
                        }
                    });
                    dialogBuilder.create().show();
                }
            });

            mRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBrus.get(getAdapterPosition()).getMyRating() == null) {
                        return;
                    }

                    if (mChangeRatingItems.contains(mBrus.get(getAdapterPosition()).getId())) {
                        mChangeRatingItems.remove(mBrus.get(getAdapterPosition()).getId());
                    } else {
                        mChangeRatingItems.add(mBrus.get(getAdapterPosition()).getId());
                    }
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}