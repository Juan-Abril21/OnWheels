package com.example.onwheels;

public class ReservationData {
    private String id;
    private String user;
    private String hora;
    private String placa;
    private String ruta;
    private String fecha;

    public ReservationData(String id, String user, String hora, String placa, String ruta, String fecha) {
        this.id = id;
        this.user = user;
        this.hora = hora;
        this.placa = placa;
        this.ruta = ruta;
        this.fecha = fecha;
    }

    public String getId() { return id; }
    public String getUser() { return user; }
    public String getHora() { return hora; }
    public String getPlaca() { return placa; }
    public String getRuta() { return ruta; }
    public String getFecha() { return fecha; }
}
