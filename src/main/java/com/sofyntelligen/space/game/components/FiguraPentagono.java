package com.sofyntelligen.space.game.components;

import com.sofyntelligen.space.game.Juego;
import com.sofyntelligen.space.game.JuegoEspacio;
import com.sofyntelligen.space.game.events.MedidasVectores;

import java.awt.Graphics2D;
import java.util.Random;

public class FiguraPentagono extends InicioMovimientos {

    private static final double minima_rotacion = 0.0075;
    private static final double variacion_rotacion = 0.0175 - minima_rotacion;
    private static final double minima_velocidad = 0.75;
    private static final double variacion_velocidad = 1.65 - minima_velocidad;
    private static final double minima_distancia = 200.0;
    private static final double maxima_distancia = JuegoEspacio.tamaño_espacio / 2.0;
    private static final double varaicion_distancia = maxima_distancia - minima_distancia;
    private PentagonSizes tamaño;
    private double velucidad_ritacion;

    public FiguraPentagono(Random a) {
        super(CalculoPosicion(a), CalculoVelocidad(a), PentagonSizes.LARGE.getRadius(), PentagonSizes.LARGE.getInitValue());
        this.velucidad_ritacion = -minima_rotacion + (a.nextDouble() * variacion_rotacion);
        this.tamaño = PentagonSizes.LARGE;
    }

    public FiguraPentagono(FiguraPentagono a, PentagonSizes b, Random c) {
        super(new MedidasVectores(a.posicion), CalculoVelocidad(c), b.getRadius(), b.getInitValue());
        this.velucidad_ritacion = minima_rotacion + (c.nextDouble() * variacion_rotacion);
        this.tamaño = b;
        for (int i = 0; i < 10; i++) {
            update(null);
        }
    }

    private static MedidasVectores CalculoPosicion(Random a) {
        MedidasVectores vec = new MedidasVectores(JuegoEspacio.tamaño_espacio / 2.0, JuegoEspacio.tamaño_espacio / 2.0);
        return vec.Asigno(new MedidasVectores(a.nextDouble() * Math.PI * 2).Escala(minima_distancia + a.nextDouble() * varaicion_distancia));
    }

    private static MedidasVectores CalculoVelocidad(Random a) {
        return new MedidasVectores(a.nextDouble() * Math.PI * 2).Escala(minima_velocidad + a.nextDouble() * variacion_velocidad);
    }

    @Override
    public void update(Juego a) {
        super.update(a);
        Rotar(velucidad_ritacion);
    }

    @Override
    public void draw(Graphics2D a, Juego b) {
        a.drawPolygon(tamaño.getPolygon());
    }

    @Override
    public void handleCollision(Juego a, InicioMovimientos b) {
        if (b.getClass() != FiguraPentagono.class) {
            if (tamaño != PentagonSizes.SMALL) {
                PentagonSizes spawnSize = PentagonSizes.values()[tamaño.ordinal() - 1];
                for (int i = 0; i < 2; i++) {
                    a.RegistroMovimientos(new FiguraPentagono(this, spawnSize, a.JuegoAzar()));
                }
            }
            Ritiro();
            a.Puntuacion(NuevaPuntuacion());
        }
    }

}
