package com.moyersoftware.bru.addpost;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.moyersoftware.bru.R;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.util.Util;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AddPostActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text)
    EditText mText;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.image_layout)
    View mImageLayout;
    @BindView(R.id.delete)
    View mDelete;

    private String mImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        ButterKnife.bind(this);

        initActionBar();
        initDelete();
    }

    private void initDelete() {
        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImagePath = null;
                mImageLayout.setVisibility(View.GONE);
                mImage.setImageResource(0);
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
        mToolbar.setNavigationIcon(R.drawable.close);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra
                    (ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            mImagePath = images.get(0).getPath();
            mImageLayout.setVisibility(View.VISIBLE);
            Glide.with(this).load(mImagePath).into(mImage);
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
        if (TextUtils.isEmpty(mText.getText())) {
            Toast.makeText(this, "Post can't be empty.", Toast.LENGTH_SHORT).show();
            return;
        }


        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.show();

        MultipartBody.Part imagePart = null;
        if (mImagePath != null) {
            File file = new File(mImagePath);
            imagePart = MultipartBody.Part.createFormData("file",
                    file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        }

        Call<Void> call = ApiFactory.getApiService().addPost
                (mText.getText().toString(), Util.getProfile().getToken(), imagePart);
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
