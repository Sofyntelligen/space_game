package com.sofyntelligen.space.game;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.JPanel;

import com.sofyntelligen.space.game.components.InicioMovimientos;
import com.sofyntelligen.space.game.events.MedidasVectores;

public class JuegoEspacio extends JPanel{

    public static final int tamaño_espacio=550;
    private static final Font titulos_fondo=new Font("Dialogo",Font.PLAIN,25);
    private static final Font subtitulos_fondo=new Font("Dialogo",Font.PLAIN,15);
    private Juego juego;
    
    public JuegoEspacio(Juego a){
        this.juego=a;
        setPreferredSize(new Dimension(tamaño_espacio,tamaño_espacio));
        setBackground(Color.WHITE);
    }

    @Override
    public void paintComponent(Graphics a){
        super.paintComponent(a);
        Graphics2D obj=(Graphics2D)a;
        obj.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        obj.setColor(Color.BLACK);
        AffineTransform b=obj.getTransform();
        for(InicioMovimientos c:juego.Movimientos()){
            if(c!=juego.JugadorNuevo()||juego.DibujarJugador()){
                MedidasVectores pos=c.Posiciones();
                LlamarGraficos(obj,c,pos.x,pos.y);
                obj.setTransform(b);
                double radius=c.Radios();
                double x=(pos.x<radius)?pos.x+tamaño_espacio:(pos.x>tamaño_espacio-radius)?pos.x -tamaño_espacio:pos.x;
                double y=(pos.y<radius)?pos.y+tamaño_espacio:(pos.y>tamaño_espacio-radius)?pos.y-tamaño_espacio:pos.y;
                if(x!=pos.x||y!=pos.y){
                    LlamarGraficos(obj,c,x,y);
                    obj.setTransform(b);
                }
            }
        }
        if(!juego.FinJuego()){
            obj.drawString("Record: "+juego.Record(),10,15);
        }
        if(juego.FinJuego()){
            TextoPintado("GAME OVER",titulos_fondo,obj,-25);
            TextoPintado("Record Final: "+juego.Record(),subtitulos_fondo,obj,10);
        }else 
            if(juego.JuegoPausado()){
                TextoPintado("PAUSA",titulos_fondo,obj,-25);
            }else 
                if(juego.MostrarNIvel()){
                    TextoPintado("NIVEL: "+juego.Nivel(),titulos_fondo,obj,-25);
                }
        obj.translate(15,30);
        obj.scale(0.85,0.85);
        for(int i=0;i<juego.Vidas();i++){
            obj.drawLine(-8,10,0,-10);
            obj.drawLine(8,10,0,-10);
            obj.drawLine(-6,6,6,6);
            obj.translate(30,0);
        }
    }

    private void TextoPintado(String texto,Font a,Graphics2D b,int x){
        b.setFont(a);
        b.drawString(texto,tamaño_espacio/2-b.getFontMetrics().stringWidth(texto)/2,tamaño_espacio/2+x);
    }
    
    private void LlamarGraficos(Graphics2D a,InicioMovimientos b,double x,double y){
        a.translate(x,y);
        double rotation=b.Rotaciones();
        if(rotation!=0.0f){
            a.rotate(b.Rotaciones());
        }
        b.draw(a,juego);
    }

}