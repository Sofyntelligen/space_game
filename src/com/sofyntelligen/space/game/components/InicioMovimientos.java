package com.sofyntelligen.space.game.components;

import com.sofyntelligen.space.game.Juego;
import com.sofyntelligen.space.game.JuegoEspacio;
import com.sofyntelligen.space.game.events.MedidasVectores;

import java.awt.Graphics2D;

public abstract class InicioMovimientos {

    protected MedidasVectores posicion;
    protected MedidasVectores velocidad;
    protected double rotacion;
    protected double radio;
    private boolean muda;
    private int terminar_puntacion;

    public InicioMovimientos(MedidasVectores a, MedidasVectores b, double x, int y) {
        this.posicion = a;
        this.velocidad = b;
        this.radio = x;
        this.rotacion = 0.0f;
        this.terminar_puntacion = y;
        this.muda = false;
    }

    public void Rotar(double a) {
        this.rotacion += a;
        this.rotacion %= Math.PI * 2;
    }

    public int NuevaPuntuacion() {
        return terminar_puntacion;
    }

    public void Ritiro() {
        this.muda = true;
    }

    public MedidasVectores Posiciones() {
        return posicion;
    }

    public double Rotaciones() {
        return rotacion;
    }

    public double Radios() {
        return radio;
    }

    public boolean Mudar() {
        return muda;
    }

    public void update(Juego a) {
        posicion.Asigno(velocidad);
        if (posicion.x < 0.0f) {
            posicion.x += JuegoEspacio.tamaño_espacio;
        }
        if (posicion.y < 0.0f) {
            posicion.y += JuegoEspacio.tamaño_espacio;
        }
        posicion.x %= JuegoEspacio.tamaño_espacio;
        posicion.y %= JuegoEspacio.tamaño_espacio;
    }

    public boolean ComprobarChoque(InicioMovimientos a) {
        double radio = a.Radios() + Radios();
        return (posicion.ConsegirDistancia(a.posicion) < radio * radio);
    }

    public abstract void handleCollision(Juego a, InicioMovimientos b);

    public abstract void draw(Graphics2D a, Juego b);
}
