package com.moyersoftware.bru.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private NewsFeedAdapter mAdapter;
    private ArrayList<NewsFeed> mNewsFeedItems = new ArrayList<>();

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
        loadItems();

        return view;
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new VerticalSpaceItemDecoration(Util.convertDpToPixel(8)));
        mAdapter = new NewsFeedAdapter(getActivity(), mNewsFeedItems);
        mRecycler.setAdapter(mAdapter);
    }

    private void loadItems() {
        /*mNewsFeedItems.add(new NewsFeed("1", "John Smith", "https://lh5.googleusercontent.com/-sHPcZPbhGiM/AAAAAAAAAAI/AAAAAAAAAAA/6Z0lXbDJPZA/w80-h80/photo.jpg", "Monson, MA", "The line isn't that bad actually. Just got here and it's moving pretty well.", "https://static1.squarespace.com/static/501bb93ec4aa651f100e3b0f/50e9b630e4b07dba6009671a/50e9c2fee4b0e6a1b5e2b655/1357497094092/IMG_8768.jpg", "5 min"));
        mNewsFeedItems.add(new NewsFeed("2", "Sean White", "https://lh4.googleusercontent.com/-KtMMhAGWGX4/AAAAAAAAAAI/AAAAAAAAAAA/1fG8_Lej6tg/w80-h80/photo.jpg", "Hartford, CT ", "Is anyone there? How long is the line?", "", "10 min"));
        mAdapter.notifyDataSetChanged();*/
        Call<ArrayList<NewsFeed>> call = ApiFactory.getApiService().getNewsFeed();
        call.enqueue(new Callback<ArrayList<NewsFeed>>() {
            @Override
            public void onResponse(Call<ArrayList<NewsFeed>> call,
                                   Response<ArrayList<NewsFeed>> response) {
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
                Toast.makeText(getActivity(), "Can't get the news feed.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
