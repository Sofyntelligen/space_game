package com.sofyntelligen.space.game;

import com.sofyntelligen.space.game.components.FiguraPentagono;
import com.sofyntelligen.space.game.components.InicioMovimientos;
import com.sofyntelligen.space.game.components.Jugador;
import com.sofyntelligen.space.game.events.Tiempos;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;

public class Juego extends JFrame {

    private static final long tiempo = (long) (1000000000.0 / 60);
    private static final int limite = 100;
    private static final int invulnerable_limite = 0;
    private JuegoEspacio espacio;
    private Tiempos logica_tiempo;
    private Random numero_azar;
    private List<InicioMovimientos> movimiento;
    private List<InicioMovimientos> movimiento_pendiente;
    private Jugador jugador;
    private int deathCooldown;
    private int showLevelCooldown;
    private int restartCooldown;
    private int record;
    private int vidas;
    private int nivel;
    private boolean fin_juego;
    private boolean reiniciar_juego;

    private Juego() {
        super("SPACE GAME");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(this.espacio = new JuegoEspacio(this), BorderLayout.CENTER);
        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        if (!ComprobarReinicio()) {
                            jugador.Establecer(true);
                        }
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        if (!ComprobarReinicio()) {
                            jugador.RotarIzquierda(true);
                        }
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        if (!ComprobarReinicio()) {
                            jugador.RotarDerecha(true);
                        }
                        break;
                    case KeyEvent.VK_SPACE:
                        if (!ComprobarReinicio()) {
                            jugador.Disparo(true);
                        }
                        break;
                    case KeyEvent.VK_P:
                        if (!ComprobarReinicio()) {
                            logica_tiempo.JuegoPausa(!logica_tiempo.JuegoPausado());
                        }
                        break;
                    default:
                        ComprobarReinicio();
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_W:
                    case KeyEvent.VK_UP:
                        jugador.Establecer(false);
                        break;
                    case KeyEvent.VK_A:
                    case KeyEvent.VK_LEFT:
                        jugador.RotarIzquierda(false);
                        break;
                    case KeyEvent.VK_D:
                    case KeyEvent.VK_RIGHT:
                        jugador.RotarDerecha(false);
                        break;
                    case KeyEvent.VK_SPACE:
                        jugador.Disparo(false);
                        break;
                }
            }
        });
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean ComprobarReinicio() {
        boolean restart = (fin_juego && restartCooldown <= 0);
        if (restart) {
            reiniciar_juego = true;
        }
        return restart;
    }

    private void InicioJuego() {
        this.numero_azar = new Random();
        this.movimiento = new LinkedList<>();
        this.movimiento_pendiente = new ArrayList<>();
        this.jugador = new Jugador();
        ReinicioJuego();
        this.logica_tiempo = new Tiempos(60);
        while (true) {
            long a = System.nanoTime();
            logica_tiempo.Cambio();
            for (int i = 0; i < 5 && logica_tiempo.TiempoTranscurrido(); i++) {
                AvancesJuego();
            }
            espacio.repaint();
            long b = tiempo - (System.nanoTime() - a);
            if (b > 0) {
                try {
                    Thread.sleep(b / 1000000L, (int) b % 1000000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void AvancesJuego() {
        movimiento.addAll(movimiento_pendiente);
        movimiento_pendiente.clear();
        if (restartCooldown > 0) {
            this.restartCooldown--;
        }
        if (showLevelCooldown > 0) {
            this.showLevelCooldown--;
        }
        if (fin_juego && reiniciar_juego) {
            ReinicioJuego();
        }
        if (!fin_juego && Muertos()) {
            this.nivel++;
            this.showLevelCooldown = 60;
            RestablecerMovimientos();
            jugador.Reinicio();
            jugador.Activa(true);
            for (int i = 0; i < nivel + 2; i++) {
                RegistroMovimientos(new FiguraPentagono(numero_azar));
            }
        }
        if (deathCooldown > 0) {
            this.deathCooldown--;
            switch (deathCooldown) {
                case limite:
                    jugador.Reinicio();
                    jugador.Activa(false);
                    break;
                case invulnerable_limite:
                    jugador.Activa(true);
                    break;
            }
        }
        if (showLevelCooldown == 0) {
            for (InicioMovimientos d : movimiento) {
                d.update(this);
            }
            for (int i = 0; i < movimiento.size(); i++) {
                InicioMovimientos a = movimiento.get(i);
                for (int j = i + 1; j < movimiento.size(); j++) {
                    InicioMovimientos b = movimiento.get(j);
                    if (i != j && a.ComprobarChoque(b) && ((a != jugador && b != jugador) || deathCooldown <= invulnerable_limite)) {
                        a.handleCollision(this, b);
                        b.handleCollision(this, a);
                    }
                }
            }
            Iterator<InicioMovimientos> e = movimiento.iterator();
            while (e.hasNext()) {
                if (e.next().Mudar()) {
                    e.remove();
                }
            }
        }
    }

    private void ReinicioJuego() {
        this.record = 0;
        this.nivel = 0;
        this.vidas = 3;
        this.deathCooldown = 0;
        this.fin_juego = false;
        this.reiniciar_juego = false;
        RestablecerMovimientos();
    }

    private void RestablecerMovimientos() {
        movimiento_pendiente.clear();
        movimiento.clear();
        movimiento.add(jugador);
    }

    private boolean Muertos() {
        for (InicioMovimientos e : movimiento) {
            if (e.getClass() == FiguraPentagono.class) {
                return false;
            }
        }
        return true;
    }

    public void MuertesJugador() {
        this.vidas--;
        if (vidas == 0) {
            this.fin_juego = true;
            this.restartCooldown = 120;
            this.deathCooldown = Integer.MAX_VALUE;
        } else {
            this.deathCooldown = 200;
        }
        jugador.Activa(false);
    }

    public void Puntuacion(int a) {
        this.record += a;
    }

    public void RegistroMovimientos(InicioMovimientos a) {
        movimiento_pendiente.add(a);
    }

    public boolean FinJuego() {
        return fin_juego;
    }

    public boolean Invulnerable() {
        return (deathCooldown > invulnerable_limite);
    }

    public boolean DibujarJugador() {
        return (deathCooldown <= limite);
    }

    public int Record() {
        return record;
    }

    public int Vidas() {
        return vidas;
    }

    public int Nivel() {
        return nivel;
    }

    public boolean JuegoPausado() {
        return logica_tiempo.JuegoPausado();
    }

    public boolean MostrarNIvel() {
        return (showLevelCooldown > 0);
    }

    public Random JuegoAzar() {
        return numero_azar;
    }

    public List<InicioMovimientos> Movimientos() {
        return movimiento;
    }

    public Jugador JugadorNuevo() {
        return jugador;
    }

    public static void main(String args[]) {
        Juego obj = new Juego();
        obj.InicioJuego();
    }

}
