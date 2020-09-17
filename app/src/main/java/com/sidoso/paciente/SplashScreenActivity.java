package com.sidoso.paciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import static com.sidoso.paciente.config.Constants.SPLASH_SCREEN_TIME;
import static com.sidoso.paciente.config.Constants.FILE_PREFERENCES;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imgSplash;
    private Animation anim;
    private SharedPreferences mUserSaved;
    private LoadTasks loadTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_splash);

        imgSplash = (ImageView) findViewById(R.id.img_splash);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_screen);
        imgSplash.setAnimation(anim);

        mUserSaved = getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);

        loadTasks = new LoadTasks();
        loadTasks.execute("");

    }

    // verifica se o usuario esta logado
    private class LoadTasks extends AsyncTask<String, Void, Boolean> {

        public LoadTasks(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean isLogged = false;
            try{
                Thread.sleep(SPLASH_SCREEN_TIME);

                if(mUserSaved.contains("tokenApi") && mUserSaved.contains("userId"))
                    isLogged = true;

            }catch (InterruptedException ie){
                ie.printStackTrace();
                isLogged = false;
            }finally {
                return isLogged;
            }
        }

        @Override
        protected void onPostExecute(Boolean isLogged) {
            super.onPostExecute(isLogged);
            Intent intent;
            progressBar.setVisibility(View.INVISIBLE);

            if(isLogged){// Redirect to MainActivity
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else{// Redirect to LoginActivity
                intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}

