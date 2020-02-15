package com.example.smartnoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ImageView imageView = findViewById(R.id.imagelogo);
        TextView txtName = findViewById(R.id.txtName);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.textanimation);
        imageView.startAnimation(animation);
        txtName.startAnimation(animation1);

        Thread timer = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(7000);

                    Intent intent = new Intent(getApplicationContext(), Login.class);
                    startActivity(intent);
                    finish();


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }
}
