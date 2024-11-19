package com.example.onwheels;

public class CardData {
    private String user;
    private String hora;
    private String placa;
    private String ruta;
    private String fecha;

    public CardData(String user, String hora, String placa, String ruta, String fecha) {
        this.user = user;
        this.hora = hora;
        this.placa = placa;
        this.ruta = ruta;
        this.fecha = fecha;
    }

    public String getUser() { return user; }
    public String getHora() { return hora; }
    public String getPlaca() { return placa; }
    public String getRuta() { return ruta; }
    public String getFecha() { return fecha; }
}

