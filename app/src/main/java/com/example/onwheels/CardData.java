package com.example.onwheels;

public class CardData {
    private String id;  // Añadimos el id del documento
    private String user;
    private String hora;
    private String placa;
    private String ruta;
    private String fecha;
    private int cupos;

    public CardData(String id, String user, String hora, String placa, String ruta, String fecha, int cupos) {
        this.id = id;
        this.user = user;
        this.hora = hora;
        this.placa = placa;
        this.ruta = ruta;
        this.fecha = fecha;
        this.cupos = cupos;
    }

    public String getId() { return id; }
    public String getUser() { return user; }
    public String getHora() { return hora; }
    public String getPlaca() { return placa; }
    public String getRuta() { return ruta; }
    public String getFecha() { return fecha; }
    public int getCupos() { return cupos; }
}