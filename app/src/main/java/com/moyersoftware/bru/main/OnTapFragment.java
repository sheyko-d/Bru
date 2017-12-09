package com.moyersoftware.bru.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.adapter.OnTapAdapter;
import com.moyersoftware.bru.main.data.OnTap;
import com.moyersoftware.bru.main.data.OnTapApi;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;
import com.moyersoftware.bru.util.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OnTapFragment extends Fragment {

    public static final String UPDATE_BEERS_INTENT = "com.moyersoftware.bru.UPDATE_BEERS";

    @Bind(R.id.recycler)
    RecyclerView mRecycler;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;
    @Bind(R.id.placeholder)
    View mPlaceholder;
    @Bind(R.id.swipe_layout)
    SwipeRefreshLayout mSwipeLayout;

    private OnTapAdapter mAdapter;
    private ArrayList<OnTap> mOnTapItems = new ArrayList<>();
    private boolean mInitialLoad = true;

    public OnTapFragment() {
        // Required empty public constructor
    }

    public static OnTapFragment newInstance() {
        return new OnTapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_on_tap, container, false);
        ButterKnife.bind(this, view);

        initRecycler();
        initSwipeLayout();

        return view;
    }

    private void initSwipeLayout() {
        mSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Call<Void> call = ApiFactory.getApiService().updateBeers();
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call,
                                           Response<Void> response) {
                        loadItems();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().registerReceiver(mReceiver, new IntentFilter(UPDATE_BEERS_INTENT));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
    }

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadItems();
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        loadItems();
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new VerticalSpaceItemDecoration(Util.convertDpToPixel(8)));
        mAdapter = new OnTapAdapter(getActivity(), mOnTapItems);
        mRecycler.setAdapter(mAdapter);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void loadItems() {
        if (mInitialLoad) {
            mProgressBar.setVisibility(View.VISIBLE);
            mInitialLoad = false;
        }

        Call<OnTapApi> call = ApiFactory.getApiService().getOnTap();
        call.enqueue(new Callback<OnTapApi>() {
            @Override
            public void onResponse(Call<OnTapApi> call,
                                   Response<OnTapApi> response) {
                if (!isAdded()) return;

                mProgressBar.setVisibility(View.GONE);
                ArrayList<OnTap> onTapItems = response.body().getOnTapItems();
                if (onTapItems != null) {
                    mOnTapItems.clear();
                    if (onTapItems.size() == 0) {
                        mPlaceholder.setVisibility(View.VISIBLE);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    mOnTapItems.add(new OnTap(response.body().getHours(),
                            response.body().getLastUpdated(), OnTapAdapter.TYPE_HOURS));

                    boolean containsCans = false;
                    for (OnTap onTapItem : onTapItems) {
                        if (onTapItem.getType() == OnTap.TYPE_CANS) {
                            containsCans = true;
                        }
                    }

                    if (containsCans) {
                        mOnTapItems.add(new OnTap(getString(R.string.cans),
                                OnTapAdapter.TYPE_HEADER));
                    }

                    boolean addedGrowlers = false;
                    boolean addedPints = false;
                    boolean addedDraft = false;
                    for (OnTap onTapItem : onTapItems) {
                        if (!addedGrowlers && onTapItem.getType() == OnTap.TYPE_GROWLERS) {
                            mOnTapItems.add(new OnTap(getString(R.string.growlers),
                                    OnTapAdapter.TYPE_HEADER));
                            addedGrowlers = true;
                        } else if (!addedPints && onTapItem.getType() == OnTap.TYPE_PINTS) {
                            mOnTapItems.add(new OnTap(getString(R.string.pints),
                                    OnTapAdapter.TYPE_HEADER));
                            addedPints = true;
                        } else if (!addedDraft && onTapItem.getType() == OnTap.TYPE_DRAFT) {
                            mOnTapItems.add(new OnTap(getString(R.string.draft),
                                    OnTapAdapter.TYPE_HEADER));
                            addedDraft = true;
                        }
                        mOnTapItems.add(onTapItem);
                    }

                    mAdapter.notifyDataSetChanged();
                    mPlaceholder.setVisibility(View.GONE);
                } else {
                    Toast.makeText(getActivity(), "Can't get the on tap beers.",
                            Toast.LENGTH_SHORT).show();
                    mPlaceholder.setVisibility(View.VISIBLE);
                }

                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<OnTapApi> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                mPlaceholder.setVisibility(View.VISIBLE);

                if (getActivity() == null) return;

                Toast.makeText(getActivity(), "Can't get the on tap beers.",
                        Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();

                if (mSwipeLayout.isRefreshing()) {
                    mSwipeLayout.setRefreshing(false);
                }
            }
        });
    }
}
