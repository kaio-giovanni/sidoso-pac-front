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

    public void insert(Paciente paciente) throws Exception{
        pacienteDAO.insert(paciente);
    }

    public void update(Paciente paciente) throws Exception{
        pacienteDAO.update(paciente);
    }

    public Paciente findById(Integer id) throws Exception{
        return pacienteDAO.findById(id);
    }

    public List<Paciente> findAll() throws Exception{
        return pacienteDAO.findAll();
    }

    public Paciente findByLogin(String email, String password) throws Exception{
        return pacienteDAO.findByLogin(email, password);
    }
}
