package com.example.attendance;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;

public class SplashActivity extends Activity {

    // SPLASH_DISPLAY_LENGTH helps to display the logo of GeoAttend for how many seconds. 1000= 1sec
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_splash);

            new Handler().postDelayed(() -> {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
                finish();
            }, SPLASH_DISPLAY_LENGTH);

    }

}
