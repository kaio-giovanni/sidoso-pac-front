package com.sidoso.paciente;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sidoso.paciente.utils.MaskEditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
    }
}
