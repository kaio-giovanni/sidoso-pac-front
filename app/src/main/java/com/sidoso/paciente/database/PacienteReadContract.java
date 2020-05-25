package com.sidoso.paciente.database;

import android.provider.BaseColumns;

public class PacienteReadContract {

    // Para previnir que a classe de contrato seja instanciada acidentalmente
    // o construtor deve ser mantido privado
    private PacienteReadContract(){}

    // classe que define as colunas da tabela pacient
    public static class PacienteEntry implements BaseColumns {
        public static final String TABLE_NAME       = "pacientes";
        public static final String COLUMN_ID        = "id";
        public static final String COLUMN_TOKEN_API = "token_api";
        public static final String COLUMN_NAME      = "name";
        public static final String COLUMN_EMAIL     = "email";
        public static final String COLUMN_PASSWORD  = "password";
        public static final String COLUMN_CPF       = "cpf";
        public static final String COLUMN_DT_BIRTH  = "dt_birth";
        public static final String COLUMN_PHONE1    = "phone1";
        public static final String COLUMN_PHONE2    = "phone2";
    }

}
