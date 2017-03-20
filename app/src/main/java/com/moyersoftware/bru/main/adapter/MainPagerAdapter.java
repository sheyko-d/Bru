package com.moyersoftware.bru.main.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.app.BruApp;
import com.moyersoftware.bru.main.NewsFeedFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final String[] mTitles;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
        mTitles = BruApp.getContext().getResources().getStringArray(R.array.main_tabs);
    }

    @Override
    public Fragment getItem(int position) {
        return new NewsFeedFragment();
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }
}