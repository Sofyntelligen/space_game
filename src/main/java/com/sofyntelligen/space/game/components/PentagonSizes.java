package com.sofyntelligen.space.game.components;

import com.sofyntelligen.space.game.util.Constants;

import java.awt.Polygon;

public enum PentagonSizes {

    SMALL(15.0, 100),
    MEDIUM(25.0, 50),
    LARGE(40.0, 20);

    private final Polygon polygon;
    private final double radius;
    private final int initValue;

    PentagonSizes(double radio, int a) {
        this.polygon = createPolygon(radio);
        this.radius = radio + 1.0;
        this.initValue = a;
    }

    private Polygon createPolygon(double radio) {

        int[] x = new int[Constants.NUMBER_OF_SIDES];
        int[] y = new int[Constants.NUMBER_OF_SIDES];

        double angulo = (2 * Math.PI / Constants.NUMBER_OF_SIDES);

        for (int i = 0; i < Constants.NUMBER_OF_SIDES; i++) {
            x[i] = (int) (radio * Math.sin(i * angulo));
            y[i] = (int) (radio * Math.cos(i * angulo));
        }

        return new Polygon(x, y, Constants.NUMBER_OF_SIDES);
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public double getRadius() {
        return radius;
    }

    public int getInitValue() {
        return initValue;
    }
}