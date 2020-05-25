package com.sidoso.paciente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.sidoso.paciente.controller.PacienteController;
import com.sidoso.paciente.model.Paciente;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SplashScreenActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imgSplash;
    private Animation anim;
    private LoadTasks loadTasks;

    private PacienteController pacienteController;

    private static final int SPLASH_SCREEN_TIME = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        progressBar = (ProgressBar) findViewById(R.id.progress_bar_splash);

        imgSplash = (ImageView) findViewById(R.id.img_splash);
        anim = AnimationUtils.loadAnimation(this, R.anim.anim_splash_screen);
        imgSplash.setAnimation(anim);

        pacienteController = PacienteController.getPacienteController(this);

        loadTasks = new LoadTasks();
        loadTasks.execute("", "");

    }

    // verifica se o usuario esta logado
    private class LoadTasks extends AsyncTask<String, Void, Paciente> {

        public LoadTasks(){

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Paciente doInBackground(String... params) {

            Paciente p = null;
            try {
                Thread.sleep(SPLASH_SCREEN_TIME);
                String email = params[0];
                String password = params[1];
                p = pacienteController.findByLogin(email, password);
            } catch (Exception e){
                e.printStackTrace();
            } finally {
                return p;
            }
        }

        @Override
        protected void onPostExecute(Paciente paciente) {
            super.onPostExecute(paciente);
            Intent intent;

            if(paciente == null){// Redirect to LoginActivity
                intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }else{// Redirect to MainActivity
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}

