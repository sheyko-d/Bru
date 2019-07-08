package com.moyersoftware.bru.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.MainActivity;
import com.moyersoftware.bru.network.ApiFactory;
import com.moyersoftware.bru.user.model.Profile;
import com.moyersoftware.bru.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class RegistrationActivity extends AppCompatActivity {

    @BindView(R.id.sign_in)
    TextView mSignIn;
    @BindView(R.id.name)
    EditText mName;
    @BindView(R.id.email)
    EditText mEmail;
    @BindView(R.id.password)
    EditText mPassword;
    @BindView(R.id.repeat_password)
    EditText mRepeatPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ButterKnife.bind(this);

        initSignUp();
        setTransparentStatusBar();
    }

    private void initSignUp() {
        SpannableStringBuilder sb = new SpannableStringBuilder(getString
                (R.string.log_in_account));
        sb.setSpan(new UnderlineSpan(), 25, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mSignIn.setText(sb);
    }

    /**
     * Required for the calligraphy library.
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    /**
     * Makes status bar transparent, instead of opaque primary color.
     */
    private void setTransparentStatusBar() {
        // Check if Android version is 5.0 or higher
        if (Util.isLollipop()) {
            // Make status bar transparent
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void onRegistrationButtonClicked(View view) {
        if (TextUtils.isEmpty(mName.getText()) || TextUtils.isEmpty(mEmail.getText())
                || TextUtils.isEmpty(mPassword.getText()) || TextUtils.isEmpty(mName.getText())) {
            Toast.makeText(this, "Not all fields are filled in.", Toast.LENGTH_SHORT).show();
            return;
        } else if (!mPassword.getText().toString().equals(mRepeatPassword.getText().toString())){
            Toast.makeText(this, "Passwords don't match.", Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dlg = new ProgressDialog(this);
        dlg.setMessage("Loading...");
        dlg.show();

        Call<Profile> call = ApiFactory.getApiService().register
                (mName.getText().toString(), mEmail.getText().toString(),
                        mPassword.getText().toString());
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call,
                                   Response<Profile> response) {
                dlg.dismiss();
                Profile profile = response.body();
                if (profile != null) {
                    Util.setProfile(profile);
                    startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegistrationActivity.this, "Can't create a new account.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                dlg.dismiss();
                Toast.makeText(RegistrationActivity.this, "Can't create a new account.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onLoginButtonClicked(View view) {
        finish();
        startActivity(new Intent(this, LoginActivity.class));
    }
}
