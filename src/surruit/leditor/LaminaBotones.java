package surruit.leditor;

import com.sun.org.apache.xpath.internal.axes.RTFIterator;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

public class LaminaBotones extends JToolBar{
    String[] fuentes;
    
    JMenuBar menuBar;
    JMenu menuFuente;
    JMenu menuEstilo;
    JMenu menuTamanio;
    JMenu menuAlineado;
    JMenu menuArchivo;
    JMenu menuColor;
    JMenu menuInformacion;
    JMenu menuIdioma;
    JMenu menuEditar;
    
    ButtonGroup menuTamanioButtonGroup;
    
    JPopupMenu popupMenu;
    
    JEditorPane editorPane; //JEditorPane al que afectan los botones
    
    ButtonMonitor buttonMonitor;
    
    RTFEditorKit editorKit;
    
    Clipboard portapapeles;
    
    
    
    ButtonFactory buttonFactory;
    GestorIdiomas gestorIdiomas;
    
    VentanaBuscar ventanaBuscar;
    
    
    public LaminaBotones(){
        buttonMonitor = new ButtonMonitor(this);
        buttonFactory = new ButtonFactory();
        gestorIdiomas = new GestorIdiomas();
        
        buttonFactory.setGestorIdiomas(gestorIdiomas);
        buttonFactory.setMonitor(buttonMonitor);
        editorKit = new RTFEditorKit();
        
        fuentes = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
        
        crearMenus();
        setUpdaters();
        generarBotones();
        gestorIdiomas.updateLocale(Locale.getDefault());
        
        //this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
        
        
        portapapeles = Toolkit.getDefaultToolkit().getSystemClipboard();
        
        ventanaBuscar = new VentanaBuscar();
        ventanaBuscar.setBuscarListener(buscarListener);
        
    }
    
    private void crearMenus(){
        menuBar = new JMenuBar();
        menuFuente = new JMenu();
        menuEstilo = new JMenu();
        menuTamanio = new JMenu();
        menuAlineado = new JMenu();
        menuArchivo = new JMenu();
        menuColor = new JMenu();
        menuInformacion = new JMenu();
        menuIdioma = new JMenu();
        menuTamanioButtonGroup = new ButtonGroup();
        menuEditar = new JMenu();
        popupMenu = new JPopupMenu();

        gestorIdiomas.add(menuFuente, "FUENTE");
        gestorIdiomas.add(menuEstilo, "ESTILO");
        gestorIdiomas.add(menuTamanio, "TAMAÑO");
        gestorIdiomas.add(menuAlineado, "ALINEADO");
        gestorIdiomas.add(menuArchivo, "ARCHIVO");
        gestorIdiomas.add(menuColor, "COLOR");
        gestorIdiomas.add(menuInformacion, "INFO");
        gestorIdiomas.add(menuIdioma, "IDIOMA");
        gestorIdiomas.add(menuEditar, "EDITAR");

        menuBar.add(menuArchivo);
        menuBar.add(menuEditar);
        menuBar.add(menuFuente);
        menuBar.add(menuEstilo);
        menuBar.add(menuTamanio);
        menuBar.add(menuAlineado);
        menuBar.add(menuColor);
        menuBar.add(menuInformacion);
        menuBar.add(menuIdioma);
    }
    private void generarBotones(){
        //crear botones de barra
        generarBoton(ButtonMonitor.MONITOR_NEGRITA, "/icons/format-bold.png", this);
        generarBoton(ButtonMonitor.MONITOR_CURSIVA, "/icons/format-italic.png", this);
        generarBoton(ButtonMonitor.MONITOR_SUBRAYADO, "/icons/format-underline.png", this);
        this.addSeparator();
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_IZQUIERDA, "/icons/format-align-left.png", this);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_DERECHA, "/icons/format-align-right.png", this);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_CENTRADO, "/icons/format-align-center.png", this);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_JUSTIFICADO, "/icons/format-align-justify.png", this);
        this.addSeparator();
        
        buttonFactory.newComboBox(ButtonMonitor.MONITOR_FUENTE, fuentes);
        buttonFactory.getJComboBox().setMaximumSize(new Dimension(75, 25));
        this.add(buttonFactory.getJComboBox());
        
        buttonFactory.newComboBox(ButtonMonitor.MONITOR_FUENTE_TAMANIO, new Integer[]{3, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 22, 24, 28, 32, 36, 40, 44, 48});
        buttonFactory.getJComboBox().setMaximumSize(new Dimension(50, 25));
        this.add(buttonFactory.getJComboBox());
        
        this.add(Box.createGlue());
        
        
        generarBoton(ButtonMonitor.MONITOR_NEGRITA, "NEGRITA", "/icons/format-bold.png", popupMenu);
        generarBoton(ButtonMonitor.MONITOR_CURSIVA, "CURSIVA", "/icons/format-italic.png", popupMenu);
        generarBoton(ButtonMonitor.MONITOR_SUBRAYADO, "SUBRAYADO", "/icons/format-underline.png", popupMenu);
        popupMenu.add(Box.createVerticalStrut(3));
        popupMenu.addSeparator();
        popupMenu.add(Box.createVerticalStrut(3));
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_IZQUIERDA, "IZQUIERDA", "/icons/format-align-left.png", popupMenu);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_DERECHA, "DERECHA", "/icons/format-align-right.png", popupMenu);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_CENTRADO, "CENTRADO", "/icons/format-align-center.png", popupMenu);
        generarBoton(ButtonMonitor.MONITOR_ALINEADO_JUSTIFICADO, "JUSTIFICADO", "/icons/format-align-justify.png", popupMenu);
        popupMenu.add(Box.createVerticalStrut(3));
        
        generarCheckBoxMenuItem(ButtonMonitor.MONITOR_NEGRITA, "NEGRITA", "/icons/format-bold.png", menuEstilo);
        generarCheckBoxMenuItem(ButtonMonitor.MONITOR_CURSIVA, "CURSIVA", "/icons/format-italic.png", menuEstilo);
        generarCheckBoxMenuItem(ButtonMonitor.MONITOR_SUBRAYADO, "SUBRAYADO", "/icons/format-underline.png", menuEstilo);
        generarMenuItem(ButtonMonitor.MONITOR_ALINEADO_IZQUIERDA, "IZQUIERDA", "/icons/format-align-left.png", menuAlineado);
        generarMenuItem(ButtonMonitor.MONITOR_ALINEADO_DERECHA, "DERECHA", "/icons/format-align-right.png", menuAlineado);
        generarMenuItem(ButtonMonitor.MONITOR_ALINEADO_CENTRADO, "CENTRADO", "/icons/format-align-center.png", menuAlineado);
        generarMenuItem(ButtonMonitor.MONITOR_ALINEADO_JUSTIFICADO, "JUSTIFICADO", "/icons/format-align-justify.png", menuAlineado);
        generarMenuItem(ButtonMonitor.MONITOR_ABRIR, "ABRIR", "/icons/folder-outline.png", menuArchivo);
        generarMenuItem(ButtonMonitor.MONITOR_GUARDAR, "GUARDAR", "/icons/content-save.png", menuArchivo);
        generarMenuItem(ButtonMonitor.MONITOR_SALIR, "SALIR", "/icons/close-circle-outline.png", menuArchivo);
        generarMenuItem(ButtonMonitor.MONITOR_ACERCA_DE, "ACERCA_DE", null, menuInformacion);
        generarMenuItem(ButtonMonitor.MONITOR_COLOR, "COLOR_DE_FUENTE", "/icons/format-color-text.png", menuColor);
        generarMenuItem(ButtonMonitor.MONITOR_IDIOMA_ESPANOL, "IDIOMA_ESPAÑOL", "/icons/idioma_español_ico.png", menuIdioma);
        generarMenuItem(ButtonMonitor.MONITOR_IDIOMA_INGLES, "IDIOMA_INGLES", "/icons/idioma_ingles_ico.png", menuIdioma);
        generarMenuItem(ButtonMonitor.MONITOR_COPIAR, "COPIAR", "/icons/content-copy.png", menuEditar);
        generarMenuItem(ButtonMonitor.MONITOR_PEGAR, "PEGAR", "/icons/content-paste.png", menuEditar);
        generarMenuItem(ButtonMonitor.MONITOR_CORTAR, "CORTAR", "/icons/content-cut.png", menuEditar);
        generarMenuItem(ButtonMonitor.MONITOR_BUSCAR, "BUSCARYREEMPLAZAR", "/icons/magnify.png", menuEditar);
        
        
        
        buttonFactory.newMenuItem(ButtonMonitor.MONITOR_FUENTE);
        buttonFactory.boton_setStaticText("Arial");
        menuFuente.add(buttonFactory.getAButton());
        
        buttonFactory.newMenuItem(ButtonMonitor.MONITOR_FUENTE);
        buttonFactory.boton_setStaticText("Verdana");
        menuFuente.add(buttonFactory.getAButton());
        
        buttonFactory.newMenuItem(ButtonMonitor.MONITOR_FUENTE);
        buttonFactory.boton_setStaticText("Courier");
        menuFuente.add(buttonFactory.getAButton());
        
        buttonFactory.newMenuItem(ButtonMonitor.MONITOR_FUENTE);
        buttonFactory.boton_setStaticText("Impact");
        menuFuente.add(buttonFactory.getAButton());
        
        
        
        buttonFactory.newRadioButtonMenuItem(ButtonMonitor.MONITOR_FUENTE_TAMANIO, menuTamanioButtonGroup);
        buttonFactory.boton_setStaticText("10pt");
        menuTamanio.add(buttonFactory.getAButton());
        
        buttonFactory.newRadioButtonMenuItem(ButtonMonitor.MONITOR_FUENTE_TAMANIO, menuTamanioButtonGroup);
        buttonFactory.boton_setStaticText("14pt");
        menuTamanio.add(buttonFactory.getAButton());
        
        buttonFactory.newRadioButtonMenuItem(ButtonMonitor.MONITOR_FUENTE_TAMANIO, menuTamanioButtonGroup);
        buttonFactory.boton_setStaticText("18pt");
        menuTamanio.add(buttonFactory.getAButton());
        
        buttonFactory.newRadioButtonMenuItem(ButtonMonitor.MONITOR_FUENTE_TAMANIO, menuTamanioButtonGroup);
        buttonFactory.boton_setStaticText("22pt");
        menuTamanio.add(buttonFactory.getAButton()); 
    }
    private void generarBoton(Integer monitorCode, String iconPath, JComponent addTo){
        buttonFactory.newButton(monitorCode);
        buttonFactory.boton_setIconByPath(iconPath);
        addTo.add(buttonFactory.getAButton());
    }
    private void generarBoton(Integer monitorCode, String resourceKey, String iconPath, JComponent addTo){
        Box caja = Box.createHorizontalBox();
        
        buttonFactory.newButton(monitorCode);
        buttonFactory.boton_setIconByPath(iconPath);
        buttonFactory.getAButton().setActionCommand("popUp");
        caja.add(Box.createHorizontalStrut(1));
        caja.add(buttonFactory.getAButton());
        caja.add(buttonFactory.addLabelAB(resourceKey));
        caja.add(Box.createGlue());
        caja.add(Box.createHorizontalStrut(5));
        
        addTo.add(caja);
    }
    private void generarMenuItem(Integer monitorCode, String resourceKey, String iconPath, JComponent addTo){
        buttonFactory.newMenuItem(monitorCode);
        buttonFactory.boton_setTextByLocaleResource(resourceKey);
        buttonFactory.boton_setIconByPath(iconPath);
        addTo.add(buttonFactory.getAButton());
    }
    private void generarCheckBoxMenuItem(Integer monitorCode, String resourceKey, String iconPath, JComponent addTo){
        buttonFactory.newCheckBoxMenuItem(monitorCode);
        buttonFactory.boton_setTextByLocaleResource(resourceKey);
        buttonFactory.boton_setIconByPath(iconPath);
        addTo.add(buttonFactory.getAButton());
    }
    
    private void setUpdaters(){
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_FUENTE, fontUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_NEGRITA, negritaUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_CURSIVA, cursivaUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_FUENTE_TAMANIO, fuenteTamanioUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_SUBRAYADO, subrayadoUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ALINEADO_IZQUIERDA, alineado_izquierda_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ALINEADO_DERECHA, alineado_derecha_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ALINEADO_CENTRADO, alineado_centrado_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ALINEADO_JUSTIFICADO, alineado_justificado_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_COLOR, coloresUpdater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ABRIR, abrir_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_GUARDAR, guardar_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_SALIR, salir_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_ACERCA_DE, acercaDe_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_IDIOMA_ESPANOL, idiomaEspanol_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_IDIOMA_INGLES, idiomaIngles_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_COPIAR, copiar_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_PEGAR, pegar_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_CORTAR, cortar_Updater);
        buttonMonitor.addUpdater(ButtonMonitor.MONITOR_BUSCAR, buscar_Updater);
    }
    //Funciones publicas
    public void setEditorPane(JEditorPane ep){
        editorPane = ep;
        ep.setEditorKit(editorKit);
        editorPane.addCaretListener(buttonMonitor);
        editorPane.setComponentPopupMenu(popupMenu); //añade el popup como menu contextual
        buttonMonitor.setEditorPane(ep);
    }
    public JMenuBar getMenuBar(){ return menuBar;}
    public void setToDefauld(){
        //buttonMonitor.updateFamily(combo_tamanioFuente); //actualiza el numero al predefinido en el Updater
        //testText();
    }

    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        System.out.println(l.getDisplayLanguage());
        gestorIdiomas.updateLocale(l);
        JColorChooser.setDefaultLocale(l); //debido a un bug hay que especificarle a los chooser los cambios
        ventanaBuscar.setLocale(l);
    }

    //Funciones generales
    private boolean updateButtonGroupTextSize(int size, ButtonGroup bg){ //comprueba si el tamaño de letra size esta en alguno de los botones del grupo
        Enumeration<AbstractButton> enu = bg.getElements();
        int radioTextSize = 0;
        JRadioButtonMenuItem button;
        
        while(enu.hasMoreElements()){
            button = (JRadioButtonMenuItem)enu.nextElement();
            radioTextSize = Integer.parseInt(button.getText().substring(0, 2)); //objetiene el tamaño de letra mediante el texto del boton
            if (size == radioTextSize){
                button.setSelected(true);
                return true;
            }
        }
        bg.clearSelection();
        return false;
    }
    private void loadtextFile(String path){
        LoaderHelper.load(editorPane, path);
    }
    private void saveTextFile(String path){
        try {
            FileOutputStream writer = new FileOutputStream(path);
            editorKit.write(writer, editorPane.getDocument(), 0, editorPane.getDocument().getLength());
            writer.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void deleteSelectedText(){
        try {
            editorPane.getDocument().remove(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    //listeners
    VentanaBuscar.BuscarListener buscarListener = new VentanaBuscar.BuscarListener() {
        @Override
        public boolean buscar(String busqueda) {
            System.out.println(".buscar()");
            int posicion_puntero = editorPane.getSelectionEnd();
            
            String stringBusqueda = "";
            try {
                stringBusqueda = editorPane.getDocument().getText(posicion_puntero, editorPane.getDocument().getLength()-posicion_puntero);
            } catch (BadLocationException ex) {}
            
            int posicion_ocurrencia = stringBusqueda.indexOf(busqueda);
            if (posicion_ocurrencia == -1) return false;
            posicion_ocurrencia += posicion_puntero;
            editorPane.select(posicion_ocurrencia, posicion_ocurrencia+busqueda.length());
            
            editorPane.getCaret().setSelectionVisible(true);
            return true;
        }

        @Override
        public void reemplazar(String busqueda, String reempazo) {
            if (busqueda.equals(editorPane.getSelectedText())){
                try {
                    deleteSelectedText();
                    editorPane.getDocument().insertString(editorPane.getSelectionEnd(), reempazo, new SimpleAttributeSet());
                } catch (BadLocationException ex) {
                    Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
                }
            }else{
                buscar(busqueda);
            }
        }
    };
    
    //funciones Updaters
    ButtonMonitor.Updater fontUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            String nuevaFuente = "Arial";
            
            //fase obtencion de datos
            if (source instanceof JMenuItem) nuevaFuente = ((JMenuItem) source).getText();
            if (source instanceof JComboBox) nuevaFuente = (String) ((JComboBox) source).getSelectedItem();
            
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevaFuente);
            
            //actualizacion de la fuente
            for (String fuente : fuentes){
                if (fuente.equals(nuevaFuente)){
                    MutableAttributeSet set =  new SimpleAttributeSet();
                    StyledDocument doc = (StyledDocument) editorPane.getDocument();
                    StyleConstants.setFontFamily(set, fuente);
                    doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
                } //si la fuente esta en el sistema la asigna
            }
            
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevaFuente) {
            for (JComponent boton: botones){
                if (boton instanceof JComboBox && boton != source){
                    JComboBox comboBox = ((JComboBox)boton);
                    buttonMonitor.noEvents = true;
                    comboBox.setSelectedItem(nuevaFuente);
                    buttonMonitor.noEvents = false;
                }
            }
        }

        @Override
        public Object caretUpdateValue() {
            return StyleConstants.getFontFamily(editorKit.getInputAttributes());
        }
    };
    ButtonMonitor.Updater negritaUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            Boolean nuevoEstado = false;
            
            //fase obtencion de datos
            if (source.getClass() == JMenuItem.class) nuevoEstado = !((boolean)caretUpdateValue()); //algunos objetos usados heredan de JMenuItem, pero este codigo solo debe ejecutarse con las instancias directas de JMenuItem
            if (source instanceof JButton) nuevoEstado = !((boolean)caretUpdateValue());
            if (source instanceof JCheckBox) nuevoEstado = ((JCheckBox)source).isSelected();
            if (source instanceof JCheckBoxMenuItem) nuevoEstado = ((JCheckBoxMenuItem)source).isSelected(); //Al parecer JCheckBoxMenuItem no hereda de JCheckButton pero si de JMenuItem.
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevoEstado);
            
            //actualizacion del estilo
            MutableAttributeSet set = new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setBold(set, nuevoEstado);
            doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
            
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            if (nuevoEstado == null) nuevoEstado = false;
            for (JComponent boton: botones){
                if (boton instanceof JCheckBox && boton != source){
                    JCheckBox checkBox = ((JCheckBox)boton);
                    checkBox.setSelected((boolean)nuevoEstado);
                }
                
                if (boton instanceof JCheckBoxMenuItem && boton != source){
                    ((JCheckBoxMenuItem)boton).setSelected((boolean)nuevoEstado);
                }
            }
        }

        @Override
        public Object caretUpdateValue() {
            return StyleConstants.isBold(editorKit.getInputAttributes());
        }
    };
    ButtonMonitor.Updater cursivaUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            Boolean nuevoEstado = false;
            
            //fase obtencion de datos
            if (source.getClass() == JMenuItem.class) nuevoEstado = !((boolean)caretUpdateValue()); //algunos objetos usados heredan de JMenuItem, pero este codigo solo debe ejecutarse con las instancias directas de JMenuItem
            if (source instanceof JButton) nuevoEstado = !((boolean)caretUpdateValue());
            if (source instanceof JCheckBox) nuevoEstado = ((JCheckBox)source).isSelected();
            if (source instanceof JCheckBoxMenuItem) nuevoEstado = ((JCheckBoxMenuItem)source).isSelected(); //Al parecer JCheckBoxMenuItem no hereda de JCheckButton pero si de JMenuItem.
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevoEstado);
            
            //actualizacion del estilo
            MutableAttributeSet set =  new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setItalic(set, nuevoEstado);
            doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            for (JComponent boton: botones){
                if (boton instanceof JCheckBox && boton != source){
                    JCheckBox checkBox = ((JCheckBox)boton);
                    checkBox.setSelected((boolean)nuevoEstado);
                }
                
                if (boton instanceof JCheckBoxMenuItem && boton != source){
                    ((JCheckBoxMenuItem)boton).setSelected((boolean)nuevoEstado);
                }
            }
        }
        @Override
        public Object caretUpdateValue() {
            return StyleConstants.isItalic(editorKit.getInputAttributes());
        }
    };
    ButtonMonitor.Updater fuenteTamanioUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            int nuevoTamanio = 12;
            
            //fase obtencion de datos
            if (source instanceof JMenuItem) nuevoTamanio = Integer.parseInt(((JMenuItem) source).getText().substring(0, 2)); //los 2 primeros caracteres del texto contienen el tamaño
            if (source instanceof JSpinner) nuevoTamanio = (int) ((JSpinner)source).getValue(); //no usado actualmente
            if (source instanceof JComboBox) nuevoTamanio = (int) ((JComboBox) source).getSelectedItem();
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevoTamanio);
            
            //actualizacion del estilo
            MutableAttributeSet set =  new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setFontSize(set, nuevoTamanio);
            doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoTamanio) {
            for (JComponent boton: botones){
                if (boton instanceof JSpinner && boton != source){ //no usado actualmente
                    JSpinner spinner = ((JSpinner)boton);
                    spinner.setValue(nuevoTamanio);
                }
                
                if (boton instanceof JComboBox && boton != source){
                    JComboBox comboBox = ((JComboBox)boton);
                    buttonMonitor.noEvents = true;
                    comboBox.setSelectedItem(nuevoTamanio);
                    buttonMonitor.noEvents = false;
                }
                
                if (boton instanceof JRadioButtonMenuItem && boton != source){ //la unica forma de quitar todos los seleccionados en un ButtonGroup es mediante la llamada al metodo .clearSelection() del buttonGroup
                    
                    //no existe forma de obtener el grupo al que pertenece un boton, asi que si se añade otro grupo debe añadirse otra linea mas aqui
                    updateButtonGroupTextSize((int) nuevoTamanio, menuTamanioButtonGroup);
                }
            }
        }

        @Override
        public Object caretUpdateValue() {
            return StyleConstants.getFontSize(editorKit.getInputAttributes());
        }
    };
    ButtonMonitor.Updater coloresUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            int nuevoColor_sRGB = 0; //negro
            int viejoColor_sRGB = (int) caretUpdateValue();
            String color = "";
            
            //fase obtencion de datos
            if (source instanceof JSlider) nuevoColor_sRGB = ((JSlider) source).getValue(); //valor 0-255 sin clasificar, aun no se sabe que color es
            if (source instanceof JSlider) color = ((JSlider) source).getName(); //valor 0-255 sin clasificar, aun no se sabe que color es
            if (source.getClass() == JMenuItem.class){
                Color nuevoColor = JColorChooser.showDialog(null, gestorIdiomas.getString("SELECCIONA_UN_COLOR"), new Color(viejoColor_sRGB));
                if (nuevoColor != null) nuevoColor_sRGB = nuevoColor.getRGB();
            }
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevoColor_sRGB);
            
            //fase procesado de datos
            if (source instanceof JSlider){
                switch(color){
                    //Referencia sobre las operaciones con bits en el JDoc de Color.getRGB()
                    case "Rojo": //NOI18N
                        viejoColor_sRGB = viejoColor_sRGB & 0b000000001111111111111111; // mascara para quitar el color
                        nuevoColor_sRGB = viejoColor_sRGB | (nuevoColor_sRGB<<16);
                        break;
                    case "Verde": //NOI18N
                        viejoColor_sRGB = viejoColor_sRGB & 0b111111110000000011111111; // mascara para quitar el color
                        nuevoColor_sRGB = viejoColor_sRGB | (nuevoColor_sRGB<<8);
                        break;
                    case "Azul": //NOI18N
                        viejoColor_sRGB = viejoColor_sRGB & 0b111111111111111100000000; // mascara para quitar el color
                        nuevoColor_sRGB = viejoColor_sRGB | (nuevoColor_sRGB);
                        break;
                }
            }
            
            //actualizacion del estilo
            MutableAttributeSet set =  new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setForeground(set, new Color(nuevoColor_sRGB));
            doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
        
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoColor) {
            for (JComponent boton: botones){
                if (boton instanceof JSlider && boton != source){ //no usado actualmente
                    JSlider slider = ((JSlider)boton);
                    int color = 0;
                    switch(slider.getName()){
                        case "Rojo": //NOI18N
                            color = ((int) nuevoColor >> 16) & 0b11111111;
                            break;
                        case "Verde": //NOI18N
                            color = ((int) nuevoColor >> 8) & 0b11111111;
                            break;
                        case "Azul": //NOI18N
                            color = (int) nuevoColor & 0b11111111;
                            break;
                    }
                    slider.setValue(color);
                }
            }
        }

        @Override
        public Object caretUpdateValue() {
            return StyleConstants.getForeground(editorKit.getInputAttributes()).getRGB();
        }
    };
    ButtonMonitor.Updater subrayadoUpdater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            Boolean nuevoEstado = false;
            
            //fase obtencion de datos
            if (source.getClass() == JMenuItem.class) nuevoEstado = !((boolean)caretUpdateValue()); //algunos objetos usados heredan de JMenuItem, pero este codigo solo debe ejecutarse con las instancias directas de JMenuItem
            if (source instanceof JButton) nuevoEstado = !((boolean)caretUpdateValue());
            if (source instanceof JCheckBox) nuevoEstado = ((JCheckBox)source).isSelected();
            if (source instanceof JCheckBoxMenuItem) nuevoEstado = ((JCheckBoxMenuItem)source).isSelected(); //Al parecer JCheckBoxMenuItem no hereda de JCheckButton pero si de JMenuItem.
            
            //fase actualizacion de componentes
            updateAllFamily(botones, source, nuevoEstado);
            
            //actualizacion del estilo
            MutableAttributeSet set =  new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setUnderline(set, nuevoEstado);
            doc.setCharacterAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
        }

        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            for (JComponent boton: botones){
                if (boton instanceof JCheckBoxMenuItem && boton != source){
                    ((JCheckBoxMenuItem)boton).setSelected((boolean)nuevoEstado);
                }
            }
        }
        @Override
        public Object caretUpdateValue() {
            return StyleConstants.isUnderline(editorKit.getInputAttributes());
        }
    };
    ButtonMonitor.Updater alineado_izquierda_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            //fase obtencion de datos
                //no importan los datos de origen
            
            //fase actualizacion de componentes
                //no es necesario actualizar nada
            
            //actualizacion del estilo
            MutableAttributeSet set = new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_LEFT);
            doc.setParagraphAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater alineado_derecha_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            //fase obtencion de datos
                //no importan los datos de origen
            
            //fase actualizacion de componentes
                //no es necesario actualizar nada
            
            //actualizacion del estilo
            MutableAttributeSet set = new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_RIGHT);
            doc.setParagraphAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater alineado_centrado_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            //fase obtencion de datos
                //no importan los datos de origen
            
            //fase actualizacion de componentes
                //no es necesario actualizar nada
            
            //actualizacion del estilo
            MutableAttributeSet set = new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
            doc.setParagraphAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater alineado_justificado_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            //fase obtencion de datos
                //no importan los datos de origen
            
            //fase actualizacion de componentes
                //no es necesario actualizar nada
            
            //actualizacion del estilo
            MutableAttributeSet set = new SimpleAttributeSet();
            StyledDocument doc = (StyledDocument) editorPane.getDocument();
            StyleConstants.setAlignment(set, StyleConstants.ALIGN_JUSTIFIED);
            doc.setParagraphAttributes(editorPane.getSelectionStart(), editorPane.getSelectionEnd()-editorPane.getSelectionStart(), set, false);
            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater abrir_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            System.out.println(".update()");
            JFileChooser chooser = new JFileChooser();
            int operacion = chooser.showOpenDialog(null);
            
            if (operacion == JFileChooser.APPROVE_OPTION){
                loadtextFile(chooser.getSelectedFile().getPath());
            }
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater guardar_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            JFileChooser chooser = new JFileChooser();
            int operacion = chooser.showSaveDialog(null);
            
            if (operacion == JFileChooser.APPROVE_OPTION){
                saveTextFile(chooser.getSelectedFile().getPath());
            }
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater acercaDe_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            JOptionPane.showMessageDialog(null, gestorIdiomas.getString("AcercaDe"));            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater salir_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            int resultado = JOptionPane.showConfirmDialog(null, gestorIdiomas.getString("CERRAR_MENSAJE"), gestorIdiomas.getString("SALIR"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
            
            if (resultado == JOptionPane.OK_OPTION) JFrame.getFrames()[0].dispose();
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater idiomaEspanol_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            ((Ventana)SwingUtilities.getWindowAncestor(menuBar)).setLocale(new Locale("es"));
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater idiomaIngles_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            System.out.println(".update()");
            ((Ventana)SwingUtilities.getWindowAncestor(menuBar)).setLocale(Locale.ENGLISH);
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater copiar_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            
            StringSelection texto = new StringSelection(editorPane.getSelectedText());
            portapapeles.setContents(texto, null);
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater pegar_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            String texto = "";
            deleteSelectedText();
            try {
                DataFlavor dataFlavor = DataFlavor.stringFlavor;
                if (portapapeles.isDataFlavorAvailable(dataFlavor)) texto = (String) portapapeles.getData(dataFlavor);
                editorPane.getDocument().insertString(editorPane.getSelectionStart(), texto, null);
            } catch (IOException ex) {
                Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnsupportedFlavorException ex) {
                Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadLocationException ex) {
                Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater cortar_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            StringSelection texto = new StringSelection(editorPane.getSelectedText());
            portapapeles.setContents(texto, null);
            
            deleteSelectedText();
            
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
    ButtonMonitor.Updater buscar_Updater = new ButtonMonitor.Updater() {
        @Override
        public void update(List<JComponent> botones, JComponent source) {
            ventanaBuscar.setVisible(true);
        }
        @Override
        public void updateAllFamily(List<JComponent> botones, JComponent source, Object nuevoEstado) {
            //no hay fase de actualizacion de componentes, solo son botones
        }
        @Override
        public Object caretUpdateValue() {return true;}
    };
}
