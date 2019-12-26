package com.sofyntelligen.space.game.components;

import com.sofyntelligen.space.game.Juego;
import com.sofyntelligen.space.game.JuegoEspacio;
import com.sofyntelligen.space.game.events.MedidasVectores;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Jugador extends InicioMovimientos {

    private static final double rotacion_definida = -Math.PI / 2.0;
    private static final double alta_velocidad = 6.5;
    private static final int maxima_municion = 5;
    private boolean precionar;
    private boolean boton_izquierda;
    private boolean boton_derecha;
    private boolean boton_disparo;
    private boolean actiavdo_disparo;
    private int rafagas;
    private int reutiliza;
    private int reutiliza_sobrecargado;
    private int animacion;
    private List<Municiones> bullets;

    public Jugador() {
        super(new MedidasVectores(JuegoEspacio.tamaño_espacio / 2.0, JuegoEspacio.tamaño_espacio / 2.0), new MedidasVectores(0.0, 0.0), 10.0, 0);
        this.bullets = new ArrayList<>();
        this.rotacion = rotacion_definida;
        this.precionar = false;
        this.boton_izquierda = false;
        this.boton_derecha = false;
        this.boton_disparo = false;
        this.actiavdo_disparo = true;
        this.reutiliza = 0;
        this.reutiliza_sobrecargado = 0;
        this.animacion = 0;
    }

    public void Establecer(boolean a) {
        this.precionar = a;
    }

    public void RotarIzquierda(boolean a) {
        this.boton_izquierda = a;
    }

    public void RotarDerecha(boolean a) {
        this.boton_derecha = a;
    }

    public void Disparo(boolean a) {
        this.boton_disparo = a;
    }

    public void Activa(boolean a) {
        this.actiavdo_disparo = a;
    }

    public void Reinicio() {
        this.rotacion = rotacion_definida;
        posicion.Establesco(JuegoEspacio.tamaño_espacio / 2.0, JuegoEspacio.tamaño_espacio / 2.0);
        velocidad.Establesco(0.0, 0.0);
        bullets.clear();
    }

    @Override
    public void update(Juego a) {
        super.update(a);
        this.animacion++;
        if (boton_izquierda != boton_derecha) {
            Rotar(boton_izquierda ? -0.052 : 0.052);
        }
        if (precionar) {
            velocidad.Asigno(new MedidasVectores(rotacion).Escala(0.0385));
            if (velocidad.ConseguirLongitud() >= alta_velocidad * alta_velocidad) {
                velocidad.Normaliza().Escala(alta_velocidad);
            }
        }
        if (velocidad.ConseguirLongitud() != 0.0) {
            velocidad.Escala(0.995);
        }
        Iterator<Municiones> iter = bullets.iterator();
        while (iter.hasNext()) {
            Municiones b = iter.next();
            if (b.Mudar()) {
                iter.remove();
            }
        }
        this.reutiliza--;
        this.reutiliza_sobrecargado--;
        if (actiavdo_disparo && boton_disparo && reutiliza <= 0 && reutiliza_sobrecargado <= 0) {
            if (bullets.size() < maxima_municion) {
                this.reutiliza = 9;
                Municiones c = new Municiones(this, rotacion);
                bullets.add(c);
                a.RegistroMovimientos(c);
            }
            this.rafagas++;
            if (rafagas == 8) {
                this.rafagas = 0;
                this.reutiliza_sobrecargado = 30;
            }
        } else if (rafagas > 0) {
            this.rafagas--;
        }
    }

    @Override
    public void handleCollision(Juego game, InicioMovimientos other) {
        if (other.getClass() == FiguraPentagono.class) {
            game.MuertesJugador();
        }
    }

    @Override
    public void draw(Graphics2D g, Juego game) {
        if (!game.Invulnerable() || game.JuegoPausado() || animacion % 20 < 10) {
            g.drawLine(-10, -8, 10, 0);
            g.drawLine(-10, 8, 10, 0);
            g.drawLine(-6, -6, -6, 6);
            if (!game.JuegoPausado() && precionar && animacion % 6 < 3) {
                g.drawLine(-6, -6, -12, 0);
                g.drawLine(-6, 6, -12, 0);
            }
        }
    }

}
