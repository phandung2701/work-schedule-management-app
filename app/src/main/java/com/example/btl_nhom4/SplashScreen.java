package com.example.btl_nhom4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        imageView = findViewById(R.id.SplashLogo);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                imageView.animate().rotationBy(360).withEndAction(this).setDuration(10000).setInterpolator(new LinearInterpolator()).start();
            }
        };
        imageView.animate().rotationBy(360).withEndAction(runnable).setDuration(10000).setInterpolator(new LinearInterpolator()).start();



        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this,login.class);
                startActivity(intent);
                finish();
            }
        }, 5000);

    }
}