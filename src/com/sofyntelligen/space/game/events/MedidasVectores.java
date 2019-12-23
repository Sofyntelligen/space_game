package com.sofyntelligen.space.game.events;

public class MedidasVectores {

    public double x;
    public double y;

    public MedidasVectores(double angulo) {
        this.x = Math.cos(angulo);
        this.y = Math.sin(angulo);
    }

    public MedidasVectores(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public MedidasVectores(MedidasVectores vector) {
        this.x = vector.x;
        this.y = vector.y;
    }

    public MedidasVectores Establesco(double x, double y) {
        this.x = x;
        this.y = y;
        return this;
    }

    public MedidasVectores Asigno(MedidasVectores vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }

    public MedidasVectores Escala(double escala) {
        this.x *= escala;
        this.y *= escala;
        return this;
    }

    public MedidasVectores Normaliza() {
        double longitud = ConseguirLongitud();
        if (longitud != 0.0f && longitud != 1.0f) {
            longitud = Math.sqrt(longitud);
            this.x /= longitud;
            this.y /= longitud;
        }
        return this;
    }

    public double ConseguirLongitud() {
        return (x * x + y * y);
    }

    public double ConsegirDistancia(MedidasVectores vector) {
        double dx = this.x - vector.x;
        double dy = this.y - vector.y;
        return (dx * dx + dy * dy);
    }

}