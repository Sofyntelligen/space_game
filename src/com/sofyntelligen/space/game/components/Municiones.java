package com.sofyntelligen.space.game.components;

import com.sofyntelligen.space.game.Juego;
import com.sofyntelligen.space.game.events.MedidasVectores;

import java.awt.Graphics2D;

public class Municiones extends InicioMovimientos {

    private int vidas_espacio;

    public Municiones(InicioMovimientos a, double angulo) {
        super(new MedidasVectores(a.posicion), new MedidasVectores(angulo).Escala(6.75), 2.0, 0);
        this.vidas_espacio = 60;
    }

    @Override
    public void update(Juego a) {
        super.update(a);
        this.vidas_espacio--;
        if (vidas_espacio <= 0) {
            Ritiro();
        }
    }

    @Override
    public void handleCollision(Juego a, InicioMovimientos b) {
        if (b.getClass() != Jugador.class) {
            Ritiro();
        }
    }

    @Override
    public void draw(Graphics2D b, Juego a) {
        b.drawOval(-1, -1, 2, 2);
    }

}
