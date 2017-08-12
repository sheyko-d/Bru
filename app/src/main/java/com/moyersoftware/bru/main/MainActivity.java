package com.moyersoftware.bru.main;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.iid.FirebaseInstanceId;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.addpost.AddPostActivity;
import com.moyersoftware.bru.main.adapter.MainPagerAdapter;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.settings.SettingsActivity;
import com.moyersoftware.bru.user.LoginActivity;
import com.moyersoftware.bru.util.Util;
import com.moyersoftware.bru.warning.WarningActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    public static final String OPEN_ON_TAP_EXTRA = "OpenOnTap";

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
    @Bind(R.id.drawer_brus)
    View mDrawerBrus;
    @Bind(R.id.drawer_settings)
    View mDrawerSettings;
    @Bind(R.id.log_in)
    TextView mLogIn;

    private ActionBarDrawerToggle mDrawerToggle;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initActionBar();
        initNavigationDrawer();
        initPager();
        initFab();
        initGoogleClient();
        updateGoogleToken();
        initAds();

        Util.Log("Token: "+FirebaseInstanceId.getInstance().getToken());

        if (!Util.isTutorialShown()) {
            startActivity(new Intent(this, WarningActivity.class));
            finish();
        } else if (!Util.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }
    }

    private void initAds() {
        MobileAds.initialize(getApplicationContext(), "ca-app-pub-1761468736156699~1132205161");
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    protected void onStop() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    private void initGoogleClient() {
        if (Util.isLoggedIn() && requestLocationPermission()) {
            // Create an instance of GoogleAPIClient.
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }
        }
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
                    startActivity(new Intent(MainActivity.this, AddPostActivity.class));
                }
            }
        });
    }

    private boolean requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initGoogleClient();
        }
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

        if (getIntent().getBooleanExtra(OPEN_ON_TAP_EXTRA, false)) {
            mPager.setCurrentItem(1);
        }
    }

    private void updateTabStyle(int pos) {
        TextView tv = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0))
                .getChildAt(0)).getChildAt(1));
        tv.setTypeface(null, pos == 0 ? Typeface.BOLD : Typeface.NORMAL);
        tv = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0))
                .getChildAt(1)).getChildAt(1));
        tv.setTypeface(null, pos == 1 ? Typeface.BOLD : Typeface.NORMAL);
        tv = (TextView) (((LinearLayout) ((LinearLayout) mTabLayout.getChildAt(0))
                .getChildAt(2)).getChildAt(1));
        tv.setTypeface(null, pos == 2 ? Typeface.BOLD : Typeface.NORMAL);
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
        mDrawerBrus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.closeDrawers();
                mPager.setCurrentItem(2);
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestLocationPermission();
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (lastLocation != null) {
            updateLocation(lastLocation.getLatitude(), lastLocation.getLongitude());
        } else {
            LocationRequest locationRequest = new LocationRequest();
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(5000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, this);
        }
    }

    private void updateLocation(double latitude, double longitude) {
        Call<Void> call = ApiFactory.getApiService().updateLocation
                (latitude, longitude, Util.getProfile().getToken());
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

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private void updateGoogleToken() {
        String googleToken = FirebaseInstanceId.getInstance().getToken();

        if (TextUtils.isEmpty(googleToken) || !Util.isLoggedIn()) return;

        Call<Void> call = ApiFactory.getApiService().updateGoogleToken(googleToken,
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

    @Override
    public void onLocationChanged(Location location) {
        updateLocation(location.getLatitude(), location.getLongitude());
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }
}
