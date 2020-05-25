package com.sidoso.paciente.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sidoso_paciente.sqlite";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_TABLE = "CREATE TABLE "   + PacienteReadContract.PacienteEntry.TABLE_NAME +
            PacienteReadContract.PacienteEntry.COLUMN_ID        + " INTEGER PRIMARY KEY AUTOINCREMENT, "      +
            PacienteReadContract.PacienteEntry.COLUMN_TOKEN_API + " VARCHAR(120) NOT NULL, "                  +
            PacienteReadContract.PacienteEntry.TABLE_NAME       + " VARCHAR(40) NOT NULL, "                   +
            PacienteReadContract.PacienteEntry.COLUMN_EMAIL     + " VARCHAR(30) NOT NULL UNIQUE, "            +
            PacienteReadContract.PacienteEntry.COLUMN_PASSWORD  + " VARCHAR(30) NOT NULL, "                   +
            PacienteReadContract.PacienteEntry.COLUMN_CPF       + " VARCHAR(14) NOT NULL UNIQUE, "            +
            PacienteReadContract.PacienteEntry.COLUMN_DT_BIRTH  + " DATE NOT NULL, "                          +
            PacienteReadContract.PacienteEntry.COLUMN_PHONE1    + " VARCHAR(18) NOT NULL, "                   +
            PacienteReadContract.PacienteEntry.COLUMN_PHONE2    + " VARCHAR(18) );";
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS" + PacienteReadContract.PacienteEntry.TABLE_NAME;
    protected SQLiteDatabase database;

    public DataBase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public SQLiteDatabase getDataBase(){
        if(this.database == null){
            database = getWritableDatabase();
        }
        return this.database;
    }
}
