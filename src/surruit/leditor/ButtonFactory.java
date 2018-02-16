/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surruit.leditor;

import java.awt.Dimension;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Larry
 */
public class ButtonFactory {
    ButtonMonitor buttonMonitor;
    GestorIdiomas gestorIdiomas;
    
    int monitorGroup;
    AbstractButton boton;
    JComboBox<Object> comboBox;
    
    Dimension dimensionButton;

    public ButtonFactory() {
        dimensionButton = new Dimension(25, 25);
    }
    
    //crear botones
    public void newButton(){
        boton = new JButton();
        comboBox = null;
        
        boton.setPreferredSize(dimensionButton);
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
    }
    public void newButton(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newButton();
    }
    public void newMenuItem(){
        boton = new JMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        boton_setAccesorByMonitorGroup();
        
    }
    public void newMenuItem(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newMenuItem();
    }
    public void newCheckBoxMenuItem(){
        boton = new JCheckBoxMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        boton_setAccesorByMonitorGroup();
        
    }
    public void newCheckBoxMenuItem(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newCheckBoxMenuItem();
    }
    public void newRadioButtonMenuItem(ButtonGroup bg){
        boton = new JRadioButtonMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        bg.add(boton);
        
    }
    public void newRadioButtonMenuItem(int monitorGroup, ButtonGroup bg){
        this.monitorGroup = monitorGroup;
        newRadioButtonMenuItem(bg);
    }
    public void newComboBox(Object[] initValues){
        comboBox = new JComboBox<>(initValues);
        boton = null;
        
        boton_mapByMonitorGroup();
        comboBox.addActionListener(buttonMonitor);
        System.out.println(monitorGroup);
    }
    public void newComboBox(int monitorGroup, Object[] initValues){
        this.monitorGroup = monitorGroup;
        newComboBox(initValues);
    }
    
    //añadir extras a botones
    public void boton_setStaticText(String text){
        boton.setText(text);
    }
    public void boton_setIconByPath(String path){
        if (path != null) boton.setIcon(new ImageIcon(this.getClass().getResource(path)));
    }
    public void boton_setTextByLocaleResource(String resourceKey){
        gestorIdiomas.add(boton, resourceKey);
    }
    public void boton_setAccesorByMonitorGroup(){
        switch(monitorGroup){
            case ButtonMonitor.MONITOR_NEGRITA:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_CURSIVA:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_I, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_SUBRAYADO:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_ALINEADO_IZQUIERDA:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_ALINEADO_DERECHA:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_ALINEADO_CENTRADO:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_ALINEADO_JUSTIFICADO:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_J, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_ABRIR:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_GUARDAR:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK)); break;
            case ButtonMonitor.MONITOR_SALIR:
                ((JMenuItem)boton).setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK)); break;
        }
    }
    public void boton_mapByMonitorGroup(){
        if (boton != null) buttonMonitor.map(monitorGroup, boton);
        if (comboBox != null) buttonMonitor.map(monitorGroup, comboBox);
    }
    
     public JLabel addLabelAB(String resourceKey){
        JLabel label = new JLabel();
        label.setLabelFor(boton);
        gestorIdiomas.add(label, resourceKey);
        
        label.addMouseListener(labelClick); //los clicks sobre la etiqueta se reenvian al boton asociado
        return label;
    }

    private final MouseAdapter labelClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AbstractButton boton = ((AbstractButton)((JLabel)e.getSource()).getLabelFor());
                boton.doClick();
            }
        };
    public AbstractButton getAButton(){return boton;}
    public JMenuItem getJMenuItem(){return (JMenuItem) boton;}
    public JComboBox<Object> getJComboBox(){
        return comboBox;
    }
    
    public void setGestorIdiomas(GestorIdiomas gestorIdiomas){
        this.gestorIdiomas = gestorIdiomas;
    }
    public void setMonitor(ButtonMonitor bm){
        buttonMonitor = bm;
    }
    public void setMonitorGroup(int group){
        monitorGroup = group;
    }
    
}
