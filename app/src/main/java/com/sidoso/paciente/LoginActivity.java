package com.sidoso.paciente;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sidoso.paciente.model.Paciente;

public class LoginActivity extends AppCompatActivity {

    private final String LOGIN_API = "";
    private EditText et_email;
    private EditText et_password;
    private TextView tv_link_reg;
    private Button btn_login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBar = (ProgressBar) findViewById(R.id.progressBarLogin);

        et_email = (EditText) findViewById(R.id.et_email_login);
        et_password = (EditText) findViewById(R.id.et_password_login);

        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(et_email.getText() == null || et_email.getText().toString() == ""){
                    //
                }else if(et_password.getText() == null || et_password.getText().toString() == ""){
                    //
                }else{
                    Login(LOGIN_API, et_email.getText().toString(), et_password.getText().toString());
                }

            }
        });

        tv_link_reg = (TextView) findViewById(R.id.tv_link_register);
        tv_link_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), RegisterActivity.class));
                finish();
            }
        });
    }

    private void Login(final String address, String email, String password){
        LoadTasks loadTasks = new LoadTasks();
        loadTasks.execute(address, email, password);
    }

    private class LoadTasks extends AsyncTask<String, Void, Paciente> {

        public LoadTasks(){}

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);

            et_email.setActivated(false);
            et_password.setActivated(false);
            btn_login.setActivated(false);
        }

        @Override
        protected Paciente doInBackground(String... strings) {

            return null;
        }

        @Override
        protected void onPostExecute(Paciente paciente) {
            super.onPostExecute(paciente);

            et_email.setActivated(true);
            et_password.setActivated(true);
            btn_login.setActivated(true);

            progressBar.setVisibility(View.INVISIBLE);

            Intent intent;

            if(paciente != null){
            }else{// Redirect to MainActivity
                intent = new Intent(getBaseContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }
    }
}
