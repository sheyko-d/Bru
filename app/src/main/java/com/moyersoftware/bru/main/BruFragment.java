package com.moyersoftware.bru.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import android.text.TextUtils;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BruFragment extends Fragment {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    @BindView(R.id.progress_bar)
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
        mAdapter = new BruAdapter(this, mBrus);
        mRecycler.setAdapter(mAdapter);
        ((SimpleItemAnimator) mRecycler.getItemAnimator()).setSupportsChangeAnimations(false);
    }

    private void loadItems() {
        if (mInitialLoad) {
            mProgressBar.setVisibility(View.VISIBLE);
            mInitialLoad = false;
        }

        Call<ArrayList<Bru>> call = ApiFactory.getApiService().getBrus
                (Util.getProfile().getToken());
        call.enqueue(new Callback<ArrayList<Bru>>() {
            @Override
            public void onResponse(Call<ArrayList<Bru>> call,
                                   Response<ArrayList<Bru>> response) {
                if (!isAdded()) return;

                try {
                    mProgressBar.setVisibility(View.GONE);
                    ArrayList<Bru> brus = response.body();
                    mBrus.clear();
                    if (brus != null) {
                        for (Bru bru : brus) {
                            if (!TextUtils.isEmpty(bru.getName())) {
                                mBrus.add(bru);
                            }
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    // Can't retrieve the beers
                }
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

    public void rateBru(int pos, Float rating) {
        Call<Void> call = ApiFactory.getApiService().rateBru
                (Util.getProfile().getToken(), mBrus.get(pos).getId(), rating);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                Toast.makeText(getActivity(), "Thanks for your feedback!",
                        Toast.LENGTH_SHORT).show();
                loadItems();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getActivity(), "Can't rate this beer.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
