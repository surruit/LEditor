/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surruit.leditor;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Larry
 */
public class VentanaBuscar extends JFrame{
    JLabel texto1;
    JTextField textField1;
    JLabel texto2;
    JTextField textField2;
    JPanel lamina;
    
    JButton buscar;
    JButton reemplazar;
    
    BuscarListener bListener;

    public VentanaBuscar(){
        this.setTitle("Buscar");
        
        //this.setMinimumSize(new Dimension(300, 150));
        
        lamina = new JPanel();
        texto1 = new JLabel();
        textField1 = new JTextField();
        texto2 = new JLabel();
        textField2 = new JTextField();
        buscar = new JButton();
        reemplazar = new JButton();
        
        texto1.setText("Buscar: ");
        texto2.setText("Reemplazar con: ");
        buscar.setText("Buscar");
        reemplazar.setText("Reemplazar");
        textField1.setPreferredSize(new Dimension(100, 25));
        textField2.setPreferredSize(new Dimension(100, 25));
        
        lamina.add(texto1);
        lamina.add(textField1);
        lamina.add(texto2);
        lamina.add(textField2);
        lamina.add(buscar);
        lamina.add(reemplazar);
        add(lamina);
        this.pack(); //ajusta al tamaño minimo
        
        bListener = new BuscarListener() {
            public boolean buscar(String busqueda) {return false;}
            public void reemplazar(String busqueda, String reempazo) {}
        };
        buscar.addActionListener(clicBotones);
        reemplazar.addActionListener(clicBotones);
    }
    
    ActionListener clicBotones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buscar){
                bListener.buscar(textField1.getText());
            }
            if (e.getSource() == reemplazar){
                bListener.reemplazar(textField1.getText(), textField2.getText());
            }
        }
    };
    
    public static abstract class BuscarListener {
        public abstract boolean buscar(String busqueda);
        public abstract void reemplazar(String busqueda, String reempazo);
    }
    public void setBuscarListener(BuscarListener bl){
        bListener = bl;
    }
    
    
}
