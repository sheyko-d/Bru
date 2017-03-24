package com.moyersoftware.bru.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

    @Bind(R.id.recycler)
    RecyclerView mRecycler;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private OnTapAdapter mAdapter;
    private ArrayList<OnTap> mOnTapItems = new ArrayList<>();

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
        mAdapter = new OnTapAdapter(getActivity(), mOnTapItems);
        mRecycler.setAdapter(mAdapter);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void loadItems() {
        mOnTapItems.clear();
        mProgressBar.setVisibility(View.VISIBLE);

        Call<OnTapApi> call = ApiFactory.getApiService().getOnTap();
        call.enqueue(new Callback<OnTapApi>() {
            @Override
            public void onResponse(Call<OnTapApi> call,
                                   Response<OnTapApi> response) {
                mProgressBar.setVisibility(View.GONE);
                ArrayList<OnTap> onTapItems = response.body().getOnTapItems();
                if (onTapItems != null) {
                    mOnTapItems.add(new OnTap(response.body().getHours(), OnTapAdapter.TYPE_HOURS));

                    boolean containsCans = false;
                    for (OnTap onTapItem : onTapItems) {
                        if (onTapItem.getType() == OnTap.TYPE_CAN) {
                            containsCans = true;
                        }
                    }

                    if (containsCans) {
                        mOnTapItems.add(new OnTap(getString(R.string.cans),
                                OnTapAdapter.TYPE_HEADER));
                    }

                    boolean addedGrowlers = false;
                    for (OnTap onTapItem : onTapItems) {
                        if (!addedGrowlers && onTapItem.getType() == OnTap.TYPE_GROWLERS) {
                            mOnTapItems.add(new OnTap(getString(R.string.growlers),
                                    OnTapAdapter.TYPE_HEADER));
                            addedGrowlers = true;
                        }
                        mOnTapItems.add(onTapItem);
                    }

                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Can't get the on tap beers.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OnTapApi> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Can't get the on tap beers.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
