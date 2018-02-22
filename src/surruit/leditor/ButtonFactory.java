package surruit.leditor;

import java.awt.Dimension;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
 * Clase que crea los botones y otros controles gráficos
 * @author Surruit
 */
public class ButtonFactory {
    /**
     * Monitor empleado por ButtonFactory.
     * Todo objeto creado es directamente mapeado a este
     * ButtonMonitor y el ButtonMonitor es añadido como listener.
     * @see ButtonMonitor
     */
    private ButtonMonitor buttonMonitor;
    /**
     * GestorIdiomas empleado por el ButtonFactory.
     * Es posible agregar los botones generados por el ButtonFactory de
     * una forma sencilla mediante algunos métodos de esta clase.
     * @see GestorIdiomas
     * @see #addLabelAB(java.lang.String)
     * @see #boton_setTextByLocaleResource(java.lang.String)
     */
    private GestorIdiomas gestorIdiomas;
    
    /**
     * Variable interna que hace referencia a una constante MONITOR de ButtonMonitor.
     * Es empleada internamente para el mapeo del boton al ButtonMonitor y para asignar
     * el atajo de teclado adecuado a cada función.
     * @see ButtonMonitor
     */
    private int monitorGroup;
    /**
     * Referencia al botón creado por el ButtonFactory.
     * @see #newButton()
     * @see #getAButton()
     */
    private AbstractButton boton;
    /**
     * Referencia al ComboBox creado por el ButtonFactory.
     * @see #newComboBox(java.lang.Object[])
     * @see #getJComboBox()
     */
    private JComboBox<Object> comboBox;
    
    /**
     * Tamaño maximo estetico asignado a todos los botones en su constructor.
     * Nota: No se aplica a los JComboBox
     */
    private final Dimension dimensionButton;
    
    /**
     * Constructor de ButtonFactory
     */
    public ButtonFactory() {
        dimensionButton = new Dimension(25, 25);
    }
    
    /**
     * Crea un nuevo JButton, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @see #newButton(int)
     * @see #getAButton()
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #dimensionButton
     */
    public void newButton(){
        boton = new JButton();
        comboBox = null;
        
        boton.setPreferredSize(dimensionButton);
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
    }
    /**
     * Crea un nuevo JButton, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param monitorGroup nuevo monitorGroup para el boton y el factory
     * @see #newButton() 
     * @see #getAButton()
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #dimensionButton
     */
    public void newButton(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newButton();
    }
    /**
     * Crea un nuevo JMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @see #newMenuItem(int)
     * @see #getJMenuItem()
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #boton_setAccesorByMonitorGroup()
     */
    public void newMenuItem(){
        boton = new JMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        boton_setAccesorByMonitorGroup();
        
    }
    /**
     * Crea un nuevo JMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param monitorGroup nuevo monitorGroup para el boton y el factory
     * @see #newMenuItem(int)
     * @see #getJMenuItem() 
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #boton_setAccesorByMonitorGroup()
     */
    public void newMenuItem(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newMenuItem();
    }
    /**
     * Crea un nuevo JCheckBoxMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @see #newCheckBoxMenuItem(int) 
     * @see #getJMenuItem() 
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #boton_setAccesorByMonitorGroup()
     */
    public void newCheckBoxMenuItem(){
        boton = new JCheckBoxMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        boton_setAccesorByMonitorGroup();
        
    }
    /**
     * Crea un nuevo JCheckBoxMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param monitorGroup nuevo monitorGroup para el boton y el factory
     * @see #newMenuItem(int)
     * @see #getJMenuItem() 
     * @see #monitorGroup
     * @see #buttonMonitor
     * @see #boton_setAccesorByMonitorGroup()
     */
    public void newCheckBoxMenuItem(int monitorGroup){
        this.monitorGroup = monitorGroup;
        newCheckBoxMenuItem();
    }
    /**
     * Crea un nuevo JRadioButtonMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param bg ButtonGroup al que pertenede el RadioButton
     * @see #newRadioButtonMenuItem(int, javax.swing.ButtonGroup)
     * @see #getJMenuItem() 
     * @see #monitorGroup
     * @see #buttonMonitor
     */
    public void newRadioButtonMenuItem(ButtonGroup bg){
        boton = new JRadioButtonMenuItem();
        comboBox = null;
        
        boton_mapByMonitorGroup();
        boton.addActionListener(buttonMonitor);
        bg.add(boton);
        
    }
    /**
     * Crea un nuevo JRadioButtonMenuItem, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param bg ButtonGroup al que pertenede el RadioButton
     * @param monitorGroup nuevo monitorGroup para el boton y el factory
     * @see #newRadioButtonMenuItem(int, javax.swing.ButtonGroup)
     * @see #getJMenuItem() 
     * @see #monitorGroup
     * @see #buttonMonitor
     */
    public void newRadioButtonMenuItem(int monitorGroup, ButtonGroup bg){
        this.monitorGroup = monitorGroup;
        newRadioButtonMenuItem(bg);
    }
    /**
     * Crea un nuevo JComboBox, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param initValues valores con los que se inicializará el JComboBox
     * @see #newComboBox(int, java.lang.Object[]) 
     * @see #getJComboBox() 
     * @see #monitorGroup
     * @see #buttonMonitor
     */
    public void newComboBox(Object[] initValues){
        comboBox = new JComboBox<>(initValues);
        boton = null;
        
        boton_mapByMonitorGroup();
        comboBox.addActionListener(buttonMonitor);
    }
    /**
     * Crea un nuevo JComboBox, cualquier otro tipo de boton existente es
     * destruido al llamar a este metodo.
     * @param initValues valores con los que se inicializará el JComboBox
     * @param monitorGroup nuevo monitorGroup para el boton y el factory
     * @see #newComboBox(java.lang.Object[])
     * @see #getJComboBox() 
     * @see #monitorGroup
     * @see #buttonMonitor
     */
    public void newComboBox(int monitorGroup, Object[] initValues){
        this.monitorGroup = monitorGroup;
        newComboBox(initValues);
    }
    
    /**
     * Añade un texto a un boton (no a comboBoxs) que no es gestionado por un GestorIdiomas.
     * 
     * @param text texto que va a ser puesto al boton de forma estatica.
     * @see #boton_setTextByLocaleResource(java.lang.String) 
     */
    public void boton_setStaticText(String text){
        boton.setText(text);
    }
    /**
     * Asigna una imagen al boton.
     * 
     * @param path ruta de la imagen en los recursos
     */
    public void boton_setIconByPath(String path){
        if (path != null) boton.setIcon(new ImageIcon(this.getClass().getResource(path)));
    }
    /**
     * Añade el boton actual al gestor de idiomas con la clave de recurso dada.
     * @param resourceKey clave del recurso de texto
     * @see GestorIdiomas
     * @see #setGestorIdiomas(surruit.leditor.GestorIdiomas)
     */
    public void boton_setTextByLocaleResource(String resourceKey){
        gestorIdiomas.add(boton, resourceKey);
    }
    /**
     * Asigna un atajo de teclado al boton actual.
     * Para identificar que atajo se asigna, se utiliza el monitorGroup del ButtonFactory
     * 
     * @see #monitorGroup
     * @see ButtonMonitor
     * @see KeyStroke
     */
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
    /**
     * Mapea el boton al ButtonMonitor empleando el monitorGroup del ButtonFactory
     * 
     * @see ButtonMonitor
     * @see ButtonMonitor#map(int, javax.swing.JComponent)
     */
    private void boton_mapByMonitorGroup(){
        if (boton != null) buttonMonitor.map(monitorGroup, boton);
        if (comboBox != null) buttonMonitor.map(monitorGroup, comboBox);
    }
    /**
     * Crea y añade un label con un texto gestionado por el Gestor de idimas del ButtonFactory.
     * Añade una redireccion de eventos de click al ultimo boton creado.
     * @param resourceKey clave del recurso de texto
     * @return JLabel creado
     */
    public JLabel addLabelAB(String resourceKey){
        JLabel label = new JLabel();
        label.setLabelFor(boton);
        gestorIdiomas.add(label, resourceKey);
        
        label.addMouseListener(labelClick); //los clicks sobre la etiqueta se reenvian al boton asociado
        return label;
    }

    /**
     * Listener para los label, redirecciona los clicks sobre el label al boton
     * para el que son label
     */
    private final MouseAdapter labelClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                AbstractButton boton = ((AbstractButton)((JLabel)e.getSource()).getLabelFor());
                boton.doClick();
            }
        };
    /**
     * Devuelve el ultimo boton creado o null si lo ultimo no es un boton
     * @return AbstractButton del ultimo boton creado
     */
    public AbstractButton getAButton(){return boton;}
    /**
     * Devuelve el ultimo boton creado como un JMenuItem o null si lo ultimo no es un boton
     * @return JMenuItem
     */
    public JMenuItem getJMenuItem(){return (JMenuItem) boton;}
    /**
     * Devuelve el ultimo JComboBox creado o null si lo ultimo no es un JComboBox
     * @return JComboBox
     */
    public JComboBox<Object> getJComboBox(){
        return comboBox;
    }
    /**
     * Asigna el gestor de idiomas
     * @param gestorIdiomas nuevo gestor de idiomas a emplear
     */
    public void setGestorIdiomas(GestorIdiomas gestorIdiomas){
        this.gestorIdiomas = gestorIdiomas;
    }
    /**
     * Asigna el nuevo ButtonMonitor a usar por la clase
     * @param bm nuevo ButtonMonitor
     */
    public void setMonitor(ButtonMonitor bm){
        buttonMonitor = bm;
    }
    /**
     * Cambia el monitorGroup del ButtonFactory
     * @param group nuevo MONITOR a usar
     * @see ButtonMonitor
     */
    public void setMonitorGroup(int group){
        monitorGroup = group;
    }
}