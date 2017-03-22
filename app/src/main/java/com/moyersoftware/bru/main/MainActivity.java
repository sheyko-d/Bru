package com.moyersoftware.bru.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.adapter.MainPagerAdapter;
import com.moyersoftware.bru.settings.SettingsActivity;
import com.moyersoftware.bru.user.LoginActivity;
import com.moyersoftware.bru.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @Bind(R.id.pager)
    ViewPager mPager;
    @Bind(R.id.tab_layout)
    TabLayout mTabLayout;
    @Bind(R.id.fab)
    FloatingActionButton mFab;
    @Bind(R.id.drawer_news_feed)
    View mDrawerNewsFeed;
    @Bind(R.id.drawer_on_tap)
    View mDrawerOnTap;
    @Bind(R.id.drawer_settings)
    View mDrawerSettings;
    @Bind(R.id.log_in)
    TextView mLogIn;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActionBar();
        initNavigationDrawer();
        initPager();
        initFab();
    }

    private void initFab() {
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Util.isLoggedIn()) {
                    Toast.makeText(MainActivity.this, R.string.access_denied, Toast.LENGTH_SHORT)
                            .show();
                    finish();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    // TODO: Show add news layout
                }
            }
        });
    }

    private void initPager() {
        mPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
        mTabLayout.setupWithViewPager(mPager);
        updateTabStyle(0);
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateTabStyle(position);
                if (position == 0) {
                    mFab.show();
                } else {
                    mFab.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateTabStyle(int pos) {
        TextView tv = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0))
                .getChildAt(pos == 1 ? 1 : 0)).getChildAt(1));
        tv.setTypeface(null, Typeface.BOLD);
        tv = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0))
                .getChildAt(pos == 1 ? 0 : 1)).getChildAt(1));
        tv.setTypeface(null, Typeface.NORMAL);
    }

    /**
     * Initializes the action bar.
     */
    private void initActionBar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    /**
     * Attaches the navigation drawer.
     */
    private void initNavigationDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar,
                R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerNewsFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                mPager.setCurrentItem(0);
            }
        });
        mDrawerOnTap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                mPager.setCurrentItem(1);
            }
        });

        mLogIn.setText(Util.isLoggedIn() ? R.string.log_out : R.string.log_in);
        mLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    }
                }, 250);
            }
        });

        mDrawerSettings.setVisibility(Util.isLoggedIn() ? View.VISIBLE : View.GONE);
        mDrawerSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                    }
                }, 250);
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Required for the calligraphy library.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
