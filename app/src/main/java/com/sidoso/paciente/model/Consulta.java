package com.sidoso.paciente.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Consulta {
    private int id;
    private String title;
    private String date;
    private Profissional profissional;
    private Paciente paciente;
    private String obs;
    private String status;
    private Double lat;
    private Double lng;

    private DateFormat df;

    public Consulta(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = stringToDate(date, "dd-MM-YYYY HH:mm:ss");
    }

    public Profissional getProfissional() {
        return profissional;
    }

    public void setProfissional(Profissional profissional) {
        this.profissional = profissional;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getStatus() {
        switch (status){
            case "001":
                return "MARCADA";
            case "002":
                return "CANCELADA";
            case "003":
                return "CONCLUIDA";
            default:
                return "nda";
        }
    }

    public void setStatus(String status) {
        this.status = status.replace("[","")
                .replace("]","")
                .replace("\"", "");
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    private String stringToDate(String aDate, String aFormat){
        if (aDate == null) return null;
        df = new SimpleDateFormat(aFormat);
        df.setLenient(false);

        try {
            Date date = df.parse(aDate);
            return df.format(date);
        }catch(ParseException e){
            e.printStackTrace();
            return null;
        }
    }
}
