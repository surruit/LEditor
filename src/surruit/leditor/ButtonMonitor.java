package surruit.leditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Gestiona los eventos mediante redirecciones a Updaters y grupos.
 * @author Surruit
 */
public class ButtonMonitor implements ItemListener, ActionListener, ChangeListener, CaretListener{
    /**
     * Lista de los grupos de objetos. El indice en esta lista se corresponde a una lista
     * de objetos de la misma funcion. Cada grupo tiene una constante MONITOR_
     */
    List<List<JComponent>> botones;
    /**
     * Lista de Updaters, cada indice de la lista es una constante MONITOR_
     */
    List<Updater> familyUpdaters;
    /**
     * Campo de texto activo, se utiliza para algun arregle estetico relativo a los eventos
     */
    JEditorPane editorPane;
    /**
     * Referencia a la lamina de botones para algunas acciones relativas al popUp
     */
    LaminaBotones laminaBotones;
    /**
     * Si es true, los eventos recibidos son ignorados. Algunas actualizaciones de controles
     * graficos pueden provocar reenvios de eventos.
     */
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
    
    /**
     * Constructor de ButtonMonitor.
     * @param lb LaminaBotones del programa
     */
    public ButtonMonitor(LaminaBotones lb){
        laminaBotones = lb;
        botones = new ArrayList<>(20);
        familyUpdaters = new ArrayList<>(20);
        
        for (int i=0; i<20; i++){ //cantidad de familias/monitores que hay
            botones.add(new ArrayList<>());
            familyUpdaters.add(null);
        }
    }
    /**
     * Mapea un objeto a un monitor, cualquier acción en ese grupo actualizara todos los componentes
     * de ese grupo.
     * @param indexGroup indice del grupo, debe ser una constante MONITOR_
     * @param boton boton a añadir al monitor
     * 
     * @see #map(int, javax.swing.JComponent, surruit.leditor.ButtonMonitor.Updater)
     * @see #addUpdater(int, surruit.leditor.ButtonMonitor.Updater)
     */
    public void map (int indexGroup, JComponent boton){
        botones.get(indexGroup).add(boton);
    }
    /**
     * Asigna un Updater a un grupo. Cualquier evento recibido por este monitor disparará el Updater
     * con una lista de todos os objetos que se deben actualizar.
     * @param indexGroup indice del grupo, debe ser una constante MONITOR_
     * @param updater Updater que recibe todos los eventos de este grupo
     * 
     * @see #map(int, javax.swing.JComponent)
     * @see #updateFamily(javax.swing.JComponent)
     * @see Updater
     */
    public void addUpdater(int indexGroup, Updater updater){
        familyUpdaters.set(indexGroup, updater);
    }
    /**
     * Metodo compacto para mapear un objeto.
     * @param indexGroup indice del grupo, debe ser una constante MONITOR_
     * @param boton boton a añadir al monitor
     * @param updater Updater que recibe todos los eventos de este grupo
     * 
     * @see #map(int, javax.swing.JComponent)
     * @see #addUpdater(int, surruit.leditor.ButtonMonitor.Updater)
     */
    public void map(int indexGroup, JComponent boton, Updater updater){ //hay que añadir indices en orden, crear el 2 antes que el 1 daria error
        if (botones.size() <= indexGroup) botones.add(indexGroup, new ArrayList<>());
        botones.get(indexGroup).add(boton);

        if (familyUpdaters.size() <= indexGroup) familyUpdaters.add(indexGroup, updater);
    }
    /**
     * Llama al metodo Update del objeto Updater asociado al grupo al que pertenece el boton recibido.
     * @param boton componente que ha lanzado el evento
     * 
     * @see Updater
     */
    public void updateFamily(JComponent boton){
        int family = getFamilyIndexOf(boton);
        System.out.println("Familia: "+ family);
        familyUpdaters.get(family).update(botones.get(family), boton); 
    }
    /**
     * Metodo de ayuda para obtener la familia a la que pertenece un boton
     * @param boton componente del cual obtener el grupo MONITOR_ al que pertenece
     * @return valor MONITOR_ o -1 si no se encuentra
     */
    private int getFamilyIndexOf(JComponent boton){
        for(int i=0; i< botones.size(); i++){
            if (botones.get(i).contains(boton)) return i;
        }
        
        return -1; //el objeto no se encuentra en nuestro monitor
    }
    /**
     * Recibe los eventos de tipo ItemListener y llama a al Updater adecuado. Este metodo no debe usarse manualmente.
     * @param e evento lanzado por el usuario
     */
    @Override
    public void itemStateChanged(ItemEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
    }
    /**
     * Recibe los eventos de tipo ActionListener y llama a al Updater adecuado. Este metodo no debe usarse manualmente.
     * @param e evento lanzado por el usuario
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
        editorPane.requestFocusInWindow();
        reSelect();
        if (e.getSource() instanceof JButton){
            if (((AbstractButton)e.getSource()).getActionCommand().equals("popUp")) laminaBotones.popupMenu.setVisible(false);
        }
    }
    /**
     * Recibe los eventos de tipo ChangeListener y llama a al Updater adecuado. Este metodo no debe usarse manualmente.
     * @param e evento lanzado por el usuario
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        if (!noEvents) this.updateFamily((JComponent)e.getSource());
    }
    /**
     * recibe eventos de tipo caretListener y actualiza los componentes según la posición
     * actual del puntero. Este metodo no debe usarse manualmente.
     * @param e evento lanzado por el usuario
     */
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
    
    /**
     * Clase abstracta que implementa los metodos necesarios para realizar actualizaciones de los componentes.
     * Todo MONITOR_ debe tener un Updater para saber como responder ante los eventos.
     */
    static public abstract class Updater{
        /**
         * Metodo al que llama el ButtonMonitor que tiene este Updater cuando recibe un evento.
         * @param botones Lista de componentes mapeados a este Updater
         * @param source Objeto que ha desencadenado el evento original
         */
        abstract public void update(List<JComponent> botones, JComponent source);
        /**
         * Metodo que actualiza el estado de todos los botones del MONITOR_ al que esta mapeado este Updater
         * @param botones Lista de componentes mapeados a este Updater
         * @param source Objeto que ha desencadenado el evento original
         * @param nuevoEstado Nuevo estado a asignar a todos los componentes de este grupo
         */
        abstract public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado);
        /**
         * Metodo debe obtener información importante para el Updater del texto seleccionado o posicion del cursor.
         * @return Objeto con información necesaria para el Updater, normalmente se corresponde al parametro nuevoEstado
         * de updateAllFamily.
         * 
         * @see #updateAllFamily(java.util.List, javax.swing.JComponent, java.lang.Object)
         */
        abstract public Object caretUpdateValue();
    }
    /**
     * Setter para el JEditorPane.
     * @param c nuevo JEditorPane
     * 
     * @see #editorPane
     */
    public void setEditorPane(JEditorPane c){
        editorPane = c;
    }
    /**
     * Metodo al que se llama en algunas ocasiones para refrescar la información del cursor cuando se realizan
     * cambios mediante los Updaters
     */
    public void reSelect(){ //algunas cosas no se actualizan correctamente si no actualizas la seleccion
        editorPane.select(editorPane.getSelectionStart(), editorPane.getSelectionEnd());
    }
}
