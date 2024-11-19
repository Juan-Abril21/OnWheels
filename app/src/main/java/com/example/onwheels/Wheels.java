package com.example.onwheels;

public class Wheels {
    private String placa;
    private String fecha;
    private String hora;
    private String inicio;
    private String fin;
    private String usuario;

    // Constructor vacío requerido por Firestore
    public Wheels() {}

    public Wheels(String placa, String fecha, String hora, String inicio, String fin, String usuario) {
        this.placa = placa;
        this.fecha = fecha;
        this.hora = hora;
        this.inicio = inicio;
        this.fin = fin;
        this.usuario = usuario;
    }

    // Getters y setters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getInicio() {
        return inicio;
    }

    public void setInicio(String inicio) {
        this.inicio = inicio;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
}
