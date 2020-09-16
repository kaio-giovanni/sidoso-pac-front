package com.sidoso.paciente.model;

import android.graphics.Bitmap;

import java.util.Date;

public class Paciente {

    private int id;
    private String token_api;
    private Bitmap photo;
    private String name;
    private String dt_birth;
    private String cpf;
    private String genre;
    private String phone1;
    private String phone2;
    private String email;
    private String password;

    public Paciente(int id, Bitmap photo, String name, String genre, String email, String password, String cpf, String dt_birth, String phone1) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.genre = genre;
        this.email = email;
        this.password = password;
        this.cpf = cpf;
        this.dt_birth = dt_birth;
        this.phone1 = phone1;
    }

    public Paciente(){}

    public int getId() {
        return id;
    }

    public Bitmap getImage(){
        return this.photo;
    }

    public void setImage(Bitmap img){
        this.photo = img;
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

    public String getGenre(){
        return this.genre;
    }

    public void setGenre(String genre){
        this.genre = genre;
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

    public String getDt_birth() {
        return dt_birth;
    }

    public void setDt_birth(String dt_birth) {
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

    @Override
    public String toString(){
        String str = "Foto: "+ this.photo.toString() + " \n";
        str += "Nome: " + this.name + "\n ";
        str += "Data de nascimento: " + this.dt_birth + " \n";
        str += "CPF: " + this.cpf + " \n";
        str += "Genero: " + this.genre + " \n";
        str += "Telefone I: " + this.phone1 + " \n";
        str += "Telefone II: " + this.phone2 + " \n";
        str += "Email: " + this.email + " \n";
        str += "Senha: " + this.password + " \n";
        return str;
    }

}
