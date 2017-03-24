package com.moyersoftware.bru.main.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.ImageFullScreen;
import com.moyersoftware.bru.main.data.NewsFeed;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedAdapter extends RecyclerView.Adapter<NewsFeedAdapter.ViewHolder> {

    private final Context mContext;
    private ArrayList<NewsFeed> mNewsFeedItems;

    public NewsFeedAdapter(Context context, ArrayList<NewsFeed> newsFeedItems) {
        mContext = context;
        mNewsFeedItems = newsFeedItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item_news_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewsFeed newsFeedItem = mNewsFeedItems.get(position);
        Glide.with(mContext).load(newsFeedItem.getUserPhoto())
                .error(R.drawable.avatar_placeholder).centerCrop().into(holder.mUserPhoto);
        holder.mUserName.setText(newsFeedItem.getUserName());
        holder.mText.setText(newsFeedItem.getText());
        holder.mLocation.setText(newsFeedItem.getLocation());
        if (!TextUtils.isEmpty(newsFeedItem.getImage())) {
            holder.mImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(newsFeedItem.getImage())
                    .error(R.color.image_placeholder).centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.mImage);
        } else {
            holder.mImage.setVisibility(View.GONE);
        }
        holder.mTime.setText(newsFeedItem.getTime());
    }

    @Override
    public int getItemCount() {
        return mNewsFeedItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_photo)
        ImageView mUserPhoto;
        @Bind(R.id.user_name)
        TextView mUserName;
        @Bind(R.id.text)
        TextView mText;
        @Bind(R.id.location)
        TextView mLocation;
        @Bind(R.id.image)
        ImageView mImage;
        @Bind(R.id.time)
        TextView mTime;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (getAdapterPosition() == -1 || !mNewsFeedItems.get(getAdapterPosition())
                            .getUserId().equals(Util.getProfile().getId())) {
                        return true;
                    }
                    final CharSequence[] items = {"Delete"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

                    builder.setItems(items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            int pos = getAdapterPosition();
                            deleteNewsFeedItem(mNewsFeedItems.get(pos).getId());
                            notifyItemRemoved(pos);
                            mNewsFeedItems.remove(pos);
                        }
                    });
                    builder.show();
                    return true;
                }
            });

            mImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, ImageFullScreen.class)
                            .putExtra(ImageFullScreen.IMAGE_EXTRA,
                                    mNewsFeedItems.get(getAdapterPosition()).getImage()));
                }
            });
        }
    }

    private void deleteNewsFeedItem(String newsFeedId) {
        Call<Void> call = ApiFactory.getApiService().deleteNewsFeedItem(newsFeedId,
                Util.getProfile().getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }
}