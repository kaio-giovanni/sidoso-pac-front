package com.sidoso.paciente.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.sidoso.paciente.database.DataBase;
import com.sidoso.paciente.database.PacienteReadContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteDAO extends DataBase {

    public PacienteDAO(Context context) {
        super(context);
    }

    public void insert(Paciente paciente) throws  Exception{
        ContentValues values = new ContentValues();

        values.put(PacienteReadContract.PacienteEntry.COLUMN_NAME, paciente.getName());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_TOKEN_API, paciente.getToken_api());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_EMAIL, paciente.getEmail());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PASSWORD, paciente.getPassword());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_CPF, paciente.getCpf());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_DT_BIRTH, paciente.getDt_birth().toString());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PHONE1, paciente.getPhone1());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PHONE2, paciente.getPhone2());

        getDataBase().insert(PacienteReadContract.PacienteEntry.TABLE_NAME, null, values);
    }

    public void update(Paciente paciente) throws Exception{
        ContentValues values = new ContentValues();

        values.put(PacienteReadContract.PacienteEntry.COLUMN_NAME, paciente.getName());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_TOKEN_API, paciente.getToken_api());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_EMAIL, paciente.getEmail());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PASSWORD, paciente.getPassword());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_CPF, paciente.getCpf());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_DT_BIRTH, paciente.getDt_birth().toString());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PHONE1, paciente.getPhone1());
        values.put(PacienteReadContract.PacienteEntry.COLUMN_PHONE2, paciente.getPhone2());

        getDataBase().update(PacienteReadContract.PacienteEntry.TABLE_NAME, values,
                PacienteReadContract.PacienteEntry.COLUMN_ID + " = ?", new String[]{""+ paciente.getId() });
    }

    public Paciente findById(Integer id) throws Exception{
        String sql = "SELECT * FROM " + PacienteReadContract.PacienteEntry.TABLE_NAME + " WHERE " +
                PacienteReadContract.PacienteEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = new String[]{"" + id};
        Cursor cursor = getDataBase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return getPaciente(cursor);
    }

    public List<Paciente> findAll() throws Exception{
        List<Paciente> pacienteList = new ArrayList<Paciente>();
        String sql = "SELECT * FROM " + PacienteReadContract.PacienteEntry.TABLE_NAME;
        Cursor cursor = getDataBase().rawQuery(sql, null);
        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            pacienteList.add(getPaciente(cursor));
            cursor.moveToNext();
        }
        return pacienteList;
    }

    public Paciente findByLogin(String email, String password) throws Exception{
        String sql = "SELECT * FROM " + PacienteReadContract.PacienteEntry.TABLE_NAME +
                " WHERE " + PacienteReadContract.PacienteEntry.COLUMN_EMAIL + " = ? AND " +
                PacienteReadContract.PacienteEntry.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = new String[]{email, password};
        Cursor cursor = getDataBase().rawQuery(sql, selectionArgs);
        cursor.moveToFirst();

        return getPaciente(cursor);
    }

    private Paciente getPaciente(Cursor cursor){
        if(cursor.getCount() == 0){
            return null;
        }
        Integer id      = cursor.getInt(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_ID));
        String token    = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_TOKEN_API));
        String name     = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_NAME));
        String email    = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_EMAIL));
        String password = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_PASSWORD));
        String cpf      = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_CPF));
        //Date dt_birth = new Date(cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_DT_BIRTH)));
        String phone1   = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_PHONE1));
        String phone2   = cursor.getString(cursor.getColumnIndex(PacienteReadContract.PacienteEntry.COLUMN_PHONE2));

        Paciente paciente = new Paciente(id, name, email, password, cpf, new Date(), phone1);
        paciente.setToken_api(token);
        paciente.setPhone2(phone2);

        return paciente;
    }
}
