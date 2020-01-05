package com.example.smartnoticeboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash_screen );
        ImageView imageView = findViewById( R.id.imagelogo );
        Animation animation = AnimationUtils.loadAnimation( getApplicationContext(),R.anim.fade );
        imageView.startAnimation( animation );

        Thread timer = new Thread(  ) {
            @Override
            public void run() {
                try {
                    sleep( 4000 );

                    SharedPreferences prefs = getSharedPreferences("Log", MODE_PRIVATE);
                    boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
                    if (isLoggedIn) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class );
                        startActivity( intent );
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), Login.class );
                        startActivity( intent );
                        finish();
                    }



                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        timer.start();
    }
}
