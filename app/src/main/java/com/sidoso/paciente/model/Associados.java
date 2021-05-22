package com.sidoso.paciente.model;

public class Associados {
    private int id;
    private String photoUrl;
    private String name;
    private String type;
    private String cnpj;
    private String phone_main;
    private String phone_secondary;
    private String email;
    private double lat, lng;

    public Associados(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getPhone_main() {
        return phone_main;
    }

    public void setPhone_main(String phone_main) {
        this.phone_main = phone_main;
    }

    public String getPhone_secondary() {
        return phone_secondary;
    }

    public void setPhone_secondary(String phone_secondary) {
        this.phone_secondary = phone_secondary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return "Nome: " + name + "\n" +
                "Tipo: " + type + "\n" +
                "Telefone I: " + phone_main + "\n" +
                "Telefone II: " + phone_secondary + "\n" +
                "Email: " + email + "\n";
    }
}
