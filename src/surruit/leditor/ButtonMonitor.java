package surruit.leditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class ButtonMonitor implements ItemListener, ActionListener, ChangeListener, CaretListener{
    List<List<JComponent>> botones;
    List<Updater> familyUpdaters;
    JEditorPane editorPane;
    LaminaBotones laminaBotones;
    boolean noEvents = false;
    
    static final int MONITOR_FUENTE = 0;
    static final int MONITOR_NEGRITA = 1;
    static final int MONITOR_CURSIVA = 2;
    static final int MONITOR_FUENTE_TAMANIO = 3;
    static final int MONITOR_SUBRAYADO = 4;
    static final int MONITOR_ALINEADO_IZQUIERDA = 5;
    static final int MONITOR_ALINEADO_DERECHA = 6;
    static final int MONITOR_ALINEADO_CENTRADO = 7;
    static final int MONITOR_ALINEADO_JUSTIFICADO = 8;
    static final int MONITOR_COLOR = 9;
    static final int MONITOR_ABRIR = 10;
    static final int MONITOR_GUARDAR = 11;
    static final int MONITOR_SALIR = 12;
    static final int MONITOR_ACERCA_DE = 13;
    static final int MONITOR_IDIOMA_ESPANOL = 14;
    static final int MONITOR_IDIOMA_INGLES = 15;
    static final int MONITOR_COPIAR = 16;
    static final int MONITOR_PEGAR = 17;
    static final int MONITOR_CORTAR = 18;
    static final int MONITOR_BUSCAR = 19;
    
    public ButtonMonitor(LaminaBotones lb){
        this.laminaBotones = lb;
        botones = new ArrayList<>(20);
        familyUpdaters = new ArrayList<>(20);
        
        for (int i=0; i<20; i++){ //cantidad de familias/monitores que hay
            botones.add(new ArrayList<>());
            familyUpdaters.add(null);
        }
    }
    public void map (int indexGroup, JComponent boton){
        botones.get(indexGroup).add(boton);
    }
    public void addUpdater(int indexGroup, Updater updater){
        familyUpdaters.set(indexGroup, updater);
    }
    public void map(int indexGroup, JComponent boton, Updater updater){ //hay que añadir indices en orden, crear el 2 antes que el 1 daria error
        if (botones.size() <= indexGroup) botones.add(indexGroup, new ArrayList<>());
        botones.get(indexGroup).add(boton);

        if (familyUpdaters.size() <= indexGroup) familyUpdaters.add(indexGroup, updater);
    }
    public void updateFamily(JComponent boton){
        int family = getFamilyIndexOf(boton);
        System.out.println("Familia: "+ family);
        familyUpdaters.get(family).update(botones.get(family), boton);
        
    }
    private int getFamilyIndexOf(JComponent boton){
        for(int i=0; i< botones.size(); i++){
            if (botones.get(i).contains(boton)) return i;
        }
        
        return -1; //el objeto no se encuentra en nuestro monitor
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
        editorPane.requestFocusInWindow();
        reSelect();
        if (e.getSource() instanceof JButton){
            if (((AbstractButton)e.getSource()).getActionCommand().equals("popUp")) laminaBotones.popupMenu.setVisible(false);
        }
    }
    @Override
    public void stateChanged(ChangeEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        
        //SwingUtilities.invokeLater llamara al metodo despues de actualizar graficamente, sin esto se producen bugs menores
        //como que los actualizadores de componentes (propios) se activan antes de que el puntero se mueva y van valores erroneos
        if (!noEvents) SwingUtilities.invokeLater(() -> {
            for (int i=0; i< botones.size(); i++){
                familyUpdaters.get(i).updateAllFamily(botones.get(i), null, familyUpdaters.get(i).caretUpdateValue());
            }
        });
        
    }
     
    static public abstract class Updater{
        abstract public void update(List<JComponent> botones, JComponent source);
        abstract public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado);
        abstract public Object caretUpdateValue();
    }
    
    public void setEditorPane(JEditorPane c){
        editorPane = c;
    }
    
    public void reSelect(){ //algunas cosas no se actualizan correctamente si no actualizas la seleccion
        editorPane.select(editorPane.getSelectionStart(), editorPane.getSelectionEnd());
    }
}
