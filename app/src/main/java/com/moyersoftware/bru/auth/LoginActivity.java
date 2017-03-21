package com.moyersoftware.bru.auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

import com.moyersoftware.bru.R;
import com.moyersoftware.bru.main.MainActivity;
import com.moyersoftware.bru.util.Util;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoginActivity extends AppCompatActivity {

    @Bind(R.id.sign_up)
    TextView mSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initSignUp();
        setTransparentStatusBar();
    }

    private void initSignUp() {
        SpannableStringBuilder sb = new SpannableStringBuilder(getString
                (R.string.create_an_account));
        sb.setSpan(new UnderlineSpan(), 23, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mSignUp.setText(sb);
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
        startActivity(new Intent(this, MainActivity.class));
        super.onBackPressed();
    }

    public void onLoginButtonClicked(View view) {

    }
}
