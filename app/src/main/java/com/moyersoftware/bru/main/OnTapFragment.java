package com.moyersoftware.bru.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.adapter.OnTapAdapter;
import com.moyersoftware.bru.main.data.OnTap;
import com.moyersoftware.bru.util.Util;
import com.moyersoftware.bru.util.VerticalSpaceItemDecoration;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

public class OnTapFragment extends Fragment {

    @Bind(R.id.recycler)
    RecyclerView mRecycler;

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
        loadItems();

        return view;
    }

    private void initRecycler() {
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.addItemDecoration(new VerticalSpaceItemDecoration(Util.convertDpToPixel(8)));
        mAdapter = new OnTapAdapter(getActivity(), mOnTapItems);
        mRecycler.setAdapter(mAdapter);
    }

    private void loadItems() {
        mOnTapItems.add(new OnTap("1", OnTap.TYPE_CAN, "Alter Ego", "American IPA - 6.8% ABV ", "6 cans PP", "$3.80 / can", "What we did here is we took a whole bunch of Mosaic and Amarillo hops, a classic punch of citrus purveying goodnes."));
        mOnTapItems.add(new OnTap("2", OnTap.TYPE_CAN, "GREEN", "American IPA - 7.5% ABV", "4 cans PP", "$3.80 / can", "Our cross-continental IPA! Made with Australian and American hops, this tropical heavy IPA opens up in the glass with notes"));
        mOnTapItems.add(new OnTap("3", OnTap.TYPE_CAN, "LIGHTS ON", "American Pale Ale - 5.6% ABV", "2 cans PP ", "$3.30 / can ", "A modern American Pale Ale brewed to celebrate new beginnings in life, and in creative endeavour! Lights On pours"));
        mAdapter.notifyDataSetChanged();
    }
}
