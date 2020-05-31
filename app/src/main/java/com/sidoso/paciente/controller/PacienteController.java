package com.sidoso.paciente.controller;

import android.content.Context;

import com.sidoso.paciente.model.Paciente;
import com.sidoso.paciente.model.PacienteDAO;

import java.util.List;

public class PacienteController {

    private static PacienteDAO pacienteDAO;
    private static PacienteController pacienteController;

    public static PacienteController getPacienteController(Context context){
        if(pacienteController == null){
            pacienteController = new PacienteController();
            pacienteDAO = new PacienteDAO(context);
        }
        return pacienteController;
    }

    public boolean insert(Paciente paciente) {
        boolean success = false;
        try{
            pacienteDAO.insert(paciente);
            success = true;
        }catch(Exception ex){
            ex.printStackTrace();
            success = false;
        }finally{
            return success;
        }
    }

    public boolean update(Paciente paciente) {
        boolean success = false;
        try{
            pacienteDAO.update(paciente);
            success = true;
        }catch(Exception ex){
            ex.printStackTrace();
            success = false;
        }finally{
            return success;
        }
    }

    public Paciente findById(Integer id) {
        Paciente paciente = null;
        try{
            paciente = pacienteDAO.findById(id);
        }catch(Exception ex){
            ex.printStackTrace();
            paciente = null;
        }finally{
            return paciente;
        }
    }

    public List<Paciente> findAll() {
        List<Paciente> pacientes = null;
        try{
            pacientes = pacienteDAO.findAll();
        }catch(Exception ex){
            ex.printStackTrace();
            pacientes = null;
        }finally{
            return pacientes;
        }
    }

    public Paciente findByLogin(String email, String password) {
        Paciente paciente = null;
        try{
            paciente = pacienteDAO.findByLogin(email, password);
        }catch(Exception ex){
            ex.printStackTrace();
            paciente = null;
        }finally{
            return paciente;
        }
    }
}
