package com.sidoso.paciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import static com.sidoso.paciente.config.Constants.FILE_PREFERENCES;
import static com.sidoso.paciente.config.Constants.SPLASH_SCREEN_TIME;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private SharedPreferences mUserSaved;
    private ImageView imgSplash;
    private Animation anim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_splash);

        mUserSaved = getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);

        imgSplash = (ImageView) findViewById(R.id.img_splash);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_splash_screen);
        imgSplash.setAnimation(anim);

        SplashAnimTask sp = new SplashAnimTask();
        sp.execute();

    }

    private class SplashAnimTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.currentThread();
                Thread.sleep(SPLASH_SCREEN_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Intent intent;

            if (mUserSaved.contains("userEmail") && mUserSaved.contains("userPassword")) {
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else{
                intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }

}

