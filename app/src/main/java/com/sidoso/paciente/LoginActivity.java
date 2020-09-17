package com.sidoso.paciente;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.sidoso.paciente.httpRequest.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import static com.sidoso.paciente.config.Constants.API_URL;
import static com.sidoso.paciente.config.Constants.FILE_PREFERENCES;

public class LoginActivity extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    private TextView tv_link_reg;
    private Button btn_login;
    private ProgressBar progressBar;
    private SharedPreferences mUserPref;

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
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();
                if(email.length() < 3 || !email.contains("@")){
                    et_email.setError("Email inválido");
                }else if(password.length() < 1){
                    et_password.setError("Senha inválida");
                }else{
                    Login(API_URL+"login/paciente/", email, password);
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

        mUserPref = getSharedPreferences(FILE_PREFERENCES, MODE_PRIVATE);
    }

    private void Login(String url, final String email, final String password){
        isLoading(true);
        JSONObject object;

        try{
            object = new JSONObject();
            object.put("email", email);
            object.put("password", password);
        }catch (JSONException e){
            object = null;
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                isLoading(false);

                SharedPreferences.Editor prefsEditor = mUserPref.edit();
                try{
                    JSONObject headers = response.getJSONObject("headers");
                    prefsEditor.putString("tokenApi", headers.getString("Authorization"));

                    prefsEditor.putInt("userId", response.getInt("id"));
                    prefsEditor.putString("userName", response.getString("name"));
                    prefsEditor.putString("userPhoto", response.getString("photo"));
                    prefsEditor.putString("userBirth", response.getString("birth"));
                    prefsEditor.putString("userCpf", response.getString("cpf"));
                    prefsEditor.putString("userGenre", response.getString("genre"));
                    prefsEditor.putString("userPhoneMain", response.getString("phone_main"));
                    prefsEditor.putString("userPhoneSeconday", response.getString("phone_secondary"));
                    prefsEditor.putString("userEmail", response.getString("email"));

                    prefsEditor.commit();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                isLoading(false);
                NetworkResponse networkResponse = error.networkResponse;
                if(networkResponse == null){
                    Log.e("Login Error",error.getClass().toString());
                }else{
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);

                        Log.e("Login Error Message", response.toString());
                        Toast.makeText(LoginActivity.this, response.getString("error"), Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }){
            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try{
                    String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put("headers", new JSONObject(response.headers));
                    return Response.success(jsonResponse, HttpHeaderParser.parseCacheHeaders(response));
                }catch (UnsupportedEncodingException e){
                    return Response.error(new ParseError(e));
                }catch(JSONException je){
                    return Response.error(new ParseError(je));
                }
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(request);
    }

    private void isLoading(Boolean y){
        if(y){
            progressBar.setVisibility(View.VISIBLE);

            et_email.setActivated(false);
            et_password.setActivated(false);
            btn_login.setEnabled(false);
        }else{
            progressBar.setVisibility(View.INVISIBLE);

            et_email.setActivated(true);
            et_password.setActivated(true);
            btn_login.setEnabled(true);
        }
    }

}
