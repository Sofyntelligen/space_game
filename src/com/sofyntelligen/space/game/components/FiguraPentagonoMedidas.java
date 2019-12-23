package com.sofyntelligen.space.game.components;

import java.awt.Polygon;

public enum FiguraPentagonoMedidas {

    Small(15.0, 100),
    Medium(25.0, 50),
    Large(40.0, 20);

    private static final int numero_lados = 5;
    public final Polygon poligono;
    public final double radio;
    public final int iniciar_valor;

    FiguraPentagonoMedidas(double radio, int a) {
        this.poligono = GenracionPoligono(radio);
        this.radio = radio + 1.0;
        this.iniciar_valor = a;
    }

    private static Polygon GenracionPoligono(double radio) {
        int[] x = new int[numero_lados];
        int[] y = new int[numero_lados];
        double angulo = (2 * Math.PI / numero_lados);
        for (int i = 0; i < numero_lados; i++) {
            x[i] = (int) (radio * Math.sin(i * angulo));
            y[i] = (int) (radio * Math.cos(i * angulo));
        }
        return new Polygon(x, y, numero_lados);
    }

}
