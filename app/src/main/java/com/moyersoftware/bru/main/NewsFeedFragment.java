package com.moyersoftware.bru.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.adapter.NewsFeedAdapter;
import com.moyersoftware.bru.main.data.NewsFeed;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;
import com.moyersoftware.bru.util.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFeedFragment extends Fragment {

    @Bind(R.id.recycler)
    RecyclerView mRecycler;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private NewsFeedAdapter mAdapter;
    private ArrayList<NewsFeed> mNewsFeedItems = new ArrayList<>();
    private boolean mInitialLoad = true;

    public NewsFeedFragment() {
        // Required empty public constructor
    }

    public static NewsFeedFragment newInstance() {
        return new NewsFeedFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_feed, container, false);
        ButterKnife.bind(this, view);

        initRecycler();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new VerticalSpaceItemDecoration(Util.convertDpToPixel(8)));
        mAdapter = new NewsFeedAdapter(getActivity(), mNewsFeedItems);
        mRecycler.setAdapter(mAdapter);
    }

    private void loadItems() {
        if (mInitialLoad) {
            mProgressBar.setVisibility(View.VISIBLE);
            mInitialLoad = false;
        }

        Call<ArrayList<NewsFeed>> call = ApiFactory.getApiService().getNewsFeed();
        call.enqueue(new Callback<ArrayList<NewsFeed>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsFeed>> call,
                                   Response<ArrayList<NewsFeed>> response) {
                mProgressBar.setVisibility(View.GONE);
                ArrayList<NewsFeed> newsFeedItems = response.body();
                if (newsFeedItems != null) {
                    mNewsFeedItems.clear();
                    mNewsFeedItems.addAll(newsFeedItems);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Can't get the news feed.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<NewsFeed>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Can't get the news feed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
