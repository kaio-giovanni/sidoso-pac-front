package com.sidoso.paciente;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.sidoso.paciente.httpRequest.VolleyMultipartRequest;
import com.sidoso.paciente.httpRequest.VolleySingleton;
import com.sidoso.paciente.model.Paciente;
import com.sidoso.paciente.utils.Helper;
import com.sidoso.paciente.utils.MaskEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sidoso.paciente.config.Constants.API_URL;
import static com.sidoso.paciente.config.Constants.IMG_NAME;
import static com.sidoso.paciente.config.Constants.PICK_IMAGE_CODE;

public class RegisterActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private ImageView imgView_user;
    private EditText et_name;
    private EditText et_birth;
    private EditText et_cpf;
    private EditText et_phone1;
    private EditText et_phone2;
    private EditText et_email;
    private EditText et_password;
    private EditText et_confirm_pass;
    private Button btn_register;
    private Button btn_cancel;
    private String genre = "M";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = (ProgressBar) findViewById(R.id.progressBarRegister);

        imgView_user = (ImageView) findViewById(R.id.img_user);

        et_name = (EditText) findViewById(R.id.et_reg_name);

        et_birth = (EditText) findViewById(R.id.et_reg_dt_birth);
        et_birth.addTextChangedListener(MaskEditText.mask(et_birth, MaskEditText.FORMAT_DATE));

        et_cpf = (EditText) findViewById(R.id.et_reg_cpf);
        et_cpf.addTextChangedListener(MaskEditText.mask(et_cpf, MaskEditText.FORMAT_CPF));

        et_phone1 = (EditText) findViewById(R.id.et_reg_phone1);
        et_phone1.addTextChangedListener(MaskEditText.mask(et_phone1, MaskEditText.FORMAT_PHONE));

        et_phone2 = (EditText) findViewById(R.id.et_reg_phone2);
        et_phone2.addTextChangedListener(MaskEditText.mask(et_phone2, MaskEditText.FORMAT_PHONE));

        et_email = (EditText) findViewById(R.id.et_reg_email);
        et_password = (EditText) findViewById(R.id.et_reg_password);
        et_confirm_pass = (EditText) findViewById(R.id.et_reg_confirm_pass);

        btn_register = (Button) findViewById(R.id.btn_reg_register);
        btn_cancel = (Button) findViewById(R.id.btn_reg_cancel);

        btn_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(checkFields()){
                    Paciente p = new Paciente();

                    p.setImage(((BitmapDrawable)imgView_user.getDrawable()).getBitmap());
                    p.setName(et_name.getText().toString());
                    p.setDt_birth(et_birth.getText().toString());
                    p.setCpf(et_cpf.getText().toString());
                    p.setGenre(genre);
                    p.setPhone1(et_phone1.getText().toString());
                    p.setPhone2(et_phone2.getText().toString());
                    p.setEmail(et_email.getText().toString().toLowerCase());
                    p.setPassword(et_password.getText().toString());

                    LoadTasks loadTasks = new LoadTasks();
                    loadTasks.execute(p);
                }
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void onRadioButtonGenreClicked(View view){
        boolean checked = ((RadioButton) view).isChecked();

        switch (view.getId()){
            case R.id.rd_masculino:
                if (checked) {
                    this.genre = "M";
                    break;
                }
            case R.id.rd_feminino:
                if (checked) {
                    this.genre = "F";
                    break;
                }
            default:
                this.genre = "M";
        }
    }

    private Date stringToDate(String aDate, String aFormat){
        if (aDate == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(aFormat);
        dateFormat.setLenient(false);

        try {
            Date date = dateFormat.parse(aDate);
            return date;
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }

    public boolean checkFields(){
        if (et_name.getText().toString() == "" || et_name.getText().toString().split(" ").length < 2){
            et_name.setError("Digite seu nome completo!");
            et_name.setFocusable(true);
            return false;
        } else if (stringToDate(et_birth.getText().toString(), "dd-MM-yyyy") == null){
            et_birth.setError("Data incorreta");
            et_birth.setFocusable(true);
            return false;
        }else if(et_cpf.getText().toString().length() < 11) {
            et_cpf.setError("CPF inválido");
            et_cpf.setFocusable(true);
            return false;
        } else if (et_phone1.getText().toString().length() < 15) {
            et_phone1.setError("Telefone inválido");
            et_phone1.setFocusable(true);
            return false;
        } else if(et_phone2.getText().toString().length() < 15 && et_phone2.getText().toString().length() > 0) {
            et_phone2.setError("Telefone inválido");
            et_phone2.setFocusable(true);
            return false;
        } else if(!et_email.getText().toString().contains("@")) {
            et_email.setError("Email inválido");
            et_email.setFocusable(true);
            return false;
        } else if (et_password.getText().toString().length() < 6) {
            et_password.setError("Senha muita curta!");
            et_password.setFocusable(true);
            return false;
        } else if (!et_confirm_pass.getText().toString().equals(et_password.getText().toString())){
            et_confirm_pass.setError("Senhas precisam ser iguais");
            et_confirm_pass.setFocusable(true);
            return false;
        } else {
            return true;
        }
    }

    public void loadImageFromGallery(View view){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_CODE && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imgView_user.setImageBitmap(bitmap);
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void savePaciente(final Paciente p, String url){
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);

                isLoading(false);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    Toast.makeText(RegisterActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getBaseContext(), LoginActivity.class));
                    finish();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;

                isLoading(false);
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    } else {
                        errorMessage = error.getClass().toString();
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);

                        Log.e("Error Message", response.toString());
                        errorMessage = response.getString("error");

                        //if (networkResponse.statusCode == 404) {}

                    } catch (JSONException e) {
                        errorMessage = e.getMessage();
                        e.printStackTrace();
                    } finally {
                        Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                }
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("name", p.getName());
                params.put("birth", p.getDt_birth());
                params.put("cpf", p.getCpf());
                params.put("genre", p.getGenre());
                params.put("phone_main", p.getPhone1());
                params.put("phone_secondary", p.getPhone2());
                params.put("email", p.getEmail());
                params.put("password", p.getPassword());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                params.put("photo", new DataPart(IMG_NAME, Helper.getFileDataFromDrawable(getBaseContext(), imgView_user.getDrawable()), "image/jpeg"));
                //params.put("photo", new VolleyMultipartRequest.DataPart("android_user.jpg", data, "image/jpeg"));

                return params;
            }
        };
        multipartRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                Log.i("RetryPolicyVolleyError", error.toString());
            }
        });

        VolleySingleton.getInstance(getBaseContext()).addToRequestQueue(multipartRequest);
    }

    private void isLoading(Boolean y){
        if(y){
            progressBar.setVisibility(View.VISIBLE);
            btn_cancel.setEnabled(false);
            btn_register.setEnabled(false);
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            btn_cancel.setEnabled(true);
            btn_register.setEnabled(true);
        }
    }

    private class LoadTasks extends AsyncTask<Paciente, Void, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading(true);
        }

        @Override
        protected Boolean doInBackground(Paciente... pacientes) {
            Paciente p = pacientes[0];
            savePaciente(p, API_URL.concat("registrar/paciente/"));
            return true;
        }

        @Override
        protected void onPostExecute(Boolean s) {
            super.onPostExecute(s);
        }
    }

}