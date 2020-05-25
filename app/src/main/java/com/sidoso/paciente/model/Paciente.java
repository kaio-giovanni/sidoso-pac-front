package com.sidoso.paciente.model;

import java.util.Date;

public class Paciente {

    private int id;
    private String token_api;
    private String name;
    private String email;
    private String password;
    private String cpf;
    private Date dt_birth;
    private String phone1;
    private String phone2;

    public Paciente(int id, String name, String email, String password, String cpf, Date dt_birth, String phone1) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.dt_birth = dt_birth;
        this.phone1 = phone1;
    }

    public int getId() {
        return id;
    }

    public void setToken_api(String token) {
        this.token_api = token;
    }

    public String getToken_api() {
        return token_api;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDt_birth() {
        return dt_birth;
    }

    public void setDt_birth(Date dt_birth) {
        this.dt_birth = dt_birth;
    }

    public String getPhone1() {
        return phone1;
    }

    public void setPhone1(String phone1) {
        this.phone1 = phone1;
    }

    public String getPhone2() {
        return phone2;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

}
