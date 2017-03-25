package com.moyersoftware.bru.settings;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.user.model.Profile;
import com.moyersoftware.bru.util.Util;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SettingsActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.name)
    TextView mName;
    @Bind(R.id.email)
    TextView mEmail;
    @Bind(R.id.photo)
    ImageView mPhoto;
    @Bind(R.id.notifications)
    CheckBox notifications;

    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        initActionBar();
        initSettings();
    }

    private void initSettings() {
        Profile profile = Util.getProfile();
        mName.setText(profile.getName());
        mEmail.setText(profile.getEmail());
        Glide.with(this).load(profile.getPhoto()).error(R.drawable.avatar_placeholder).centerCrop()
                .into(mPhoto);

        notifications.setChecked(Util.notificationsEnabled());
        notifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                Util.setNotificationsEnabled(checked);
            }
        });
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
     * Required for the calligraphy library.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return false;
    }

    public void onEditNameButtonClicked(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MaterialDialog);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_edit_name, null);
        dialog.setTitle(R.string.edit_name);
        final EditText name = (EditText) dialogView.findViewById(R.id.name);
        name.setText(Util.getProfile().getName());
        name.setSelection(Util.getProfile().getName().length());
        dialog.setView(dialogView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Profile profile = Util.getProfile();
                profile.setName(name.getText().toString());
                Util.setProfile(profile);
                mName.setText(profile.getName());
                updateProfile(profile);
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public void onEditEmailButtonClicked(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MaterialDialog);
        @SuppressLint("InflateParams") View dialogView = LayoutInflater.from(this)
                .inflate(R.layout.dialog_edit_email, null);
        dialog.setTitle(R.string.edit_email);
        final EditText email = (EditText) dialogView.findViewById(R.id.email);
        email.setText(Util.getProfile().getEmail());
        email.setSelection(Util.getProfile().getEmail().length());
        dialog.setView(dialogView);
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Profile profile = Util.getProfile();
                profile.setEmail(email.getText().toString());
                Util.setProfile(profile);
                mEmail.setText(profile.getEmail());
                updateProfile(profile);
            }
        });
        dialog.setNegativeButton("Cancel", null);
        dialog.show();
    }

    public void onEditPhotoButtonClicked(View view) {
        ImagePicker.create(this)
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .showCamera(true) // show camera or not (true by default)
                .start(0); // start image picker activity with request code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra
                    (ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            mImagePath = images.get(0).getPath();
            Glide.with(this).load(mImagePath).into(mPhoto);
            Profile profile = Util.getProfile();
            profile.setPhoto(mImagePath);
            updateProfile(profile);
        }
    }

    private void updateProfile(Profile profile) {
        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.show();

        MultipartBody.Part imagePart = null;
        if (mImagePath != null) {
            File file = new File(mImagePath);
            imagePart = MultipartBody.Part.createFormData("file",
                    file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }
        Call<Void> call = ApiFactory.getApiService().updateProfile
                (profile.getName(), profile.getEmail(), profile.getToken(), imagePart);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                Toast.makeText(SettingsActivity.this, "Profile is updated.", Toast.LENGTH_SHORT)
                        .show();
                dlg.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dlg.dismiss();
                Toast.makeText(SettingsActivity.this, "Can't update a profile.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onRateButtonClicked(View view) {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    public void onAboutButtonClicked(View view) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this, R.style.MaterialDialog);
        @SuppressLint("InflateParams")
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_about, null);
        TextView aboutVersionTxt = (TextView) dialogView.findViewById(R.id.aboutVersionTxt);
        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            aboutVersionTxt.setText(version);
        } catch (PackageManager.NameNotFoundException e) {
            aboutVersionTxt.setVisibility(View.GONE);
        }

        dialog.setView(dialogView);
        dialog.setPositiveButton("Close", null);
        dialog.show();
    }
}
