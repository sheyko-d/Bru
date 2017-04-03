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
import com.moyersoftware.bru.main.adapter.BruAdapter;
import com.moyersoftware.bru.main.data.Bru;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;
import com.moyersoftware.bru.util.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BruFragment extends Fragment {

    @Bind(R.id.recycler)
    RecyclerView mRecycler;
    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    private BruAdapter mAdapter;
    private ArrayList<Bru> mBrus = new ArrayList<>();
    private boolean mInitialLoad = true;

    public BruFragment() {
        // Required empty public constructor
    }

    public static BruFragment newInstance() {
        return new BruFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_brus, container, false);
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
        mAdapter = new BruAdapter(getActivity(), mBrus);
        mRecycler.setAdapter(mAdapter);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void loadItems() {
        if (mInitialLoad) {
            mProgressBar.setVisibility(View.VISIBLE);
            mInitialLoad = false;
        }

        Call<ArrayList<Bru>> call = ApiFactory.getApiService().getBrus();
        call.enqueue(new Callback<ArrayList<Bru>>() {
            @Override
            public void onResponse(Call<ArrayList<Bru>> call,
                                   Response<ArrayList<Bru>> response) {
                if (!isAdded()) return;

                mProgressBar.setVisibility(View.GONE);
                ArrayList<Bru> brus = response.body();
                mBrus.addAll(brus);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArrayList<Bru>> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Can't get the brüs.",
                        Toast.LENGTH_SHORT).show();
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
