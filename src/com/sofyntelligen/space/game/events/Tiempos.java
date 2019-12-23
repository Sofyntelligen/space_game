package com.sofyntelligen.space.game.events;

public class Tiempos {

    private float unidades_tiempo;
    private long ultimo_cambio;
    private int tiempo_transcurrido;
    private float tiempo_exceso;
    private boolean Pausa_ejecutada;

    public Tiempos(float a) {
        TiempoSegundos(a);
        Reinicio();
    }

    public void TiempoSegundos(float a) {
        this.unidades_tiempo = (1.0f / a) * 1000;
    }

    public void Reinicio() {
        this.tiempo_transcurrido = 0;
        this.tiempo_exceso = 0.0f;
        this.ultimo_cambio = TiempoActual();
        this.Pausa_ejecutada = false;
    }

    public void Cambio() {
        long a = TiempoActual();
        float delta = (float) (a - ultimo_cambio) + tiempo_exceso;
        if (!Pausa_ejecutada) {
            this.tiempo_transcurrido += (int) Math.floor(delta / unidades_tiempo);
            this.tiempo_exceso = delta % unidades_tiempo;
        }
        this.ultimo_cambio = a;
    }

    public void JuegoPausa(boolean a) {
        this.Pausa_ejecutada = a;
    }

    public boolean JuegoPausado() {
        return Pausa_ejecutada;
    }

    public boolean TiempoTranscurrido() {
        if (tiempo_transcurrido > 0) {
            this.tiempo_transcurrido--;
            return true;
        }
        return false;
    }

    private static final long TiempoActual() {
        return (System.nanoTime() / 1000000L);
    }

}
