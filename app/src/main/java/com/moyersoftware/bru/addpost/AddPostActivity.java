package com.moyersoftware.bru.addpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddPostActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.text)
    EditText mText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);

        initActionBar();
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
        mToolbar.setNavigationIcon(R.drawable.close);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra
                    (ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            Util.Log("path = " + images.get(0).getPath());
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
        if (item.getItemId() == R.id.attach) {
            ImagePicker.create(this)
                    .imageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .showCamera(true) // show camera or not (true by default)
                    .start(0); // start image picker activity with request code
        } else if (item.getItemId() == R.id.send) {
            addPost();
        } else if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return false;
    }

    private void addPost() {
        if (TextUtils.isEmpty(mText.getText())){
            Toast.makeText(this, "Post can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }


        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.show();

        Call<Void> call = ApiFactory.getApiService().addPost
                (mText.getText().toString(), Util.getProfile().getToken());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call,
                                   Response<Void> response) {
                finish();
                dlg.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                dlg.dismiss();
                Toast.makeText(AddPostActivity.this, "Can't add a post.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add_post, menu);
        return true;
    }
}
