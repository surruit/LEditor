package surruit.leditor;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Ventana para buscar y buscar y reemplazar
 * @author Surruit
 */
public class VentanaBuscar extends JFrame{
    /**
     * Label con el texto "Buscar:"
     */
    JLabel buscarTexto;
    /**
     * TextField donde escribir el parametro de busqueda
     */
    JTextField buscarTextField;
    /**
     * Label con el texto "Reemplazar:"
     */
    JLabel reemplazarTexto;
    /**
     * TextField donde se escribe el parametro por el que se reemplazará el texto
     * 
     * @see #buscarTextField
     */
    JTextField reemplazarTextField;
    
    /**
     * JPanel que contiene todos los elementos gráficos
     */
    JPanel lamina;
    
    /**
     * Botón que desencadena la busqueda de texto
     */
    JButton buscar;
    /**
     * Botón que desencadena el reemplazo del texto seleccionado o la busqueda si no
     * hay ningun texto seleccionado
     */
    JButton reemplazar;
    
    /**
     * Listener para los eventos de busqueda o reemplazo de texto
     * 
     * @see BuscarListener
     */
    BuscarListener bListener;
    
    /**
     * Gestor de idiomas para los textos de este objeto.
     * 
     * @see GestorIdiomas
     */
    GestorIdiomas gestorIdiomas;
    
    /**
     * Constructor de la VentanaBuscar. El constructor no hace visible la ventana.
     */
    public VentanaBuscar(){
        gestorIdiomas = new GestorIdiomas();
        crearComponentes();

        //Eventos
        bListener = new BuscarListener() {
            public boolean buscar(String busqueda) {return false;}
            public void reemplazar(String busqueda, String reempazo) {}
        };
        buscar.addActionListener(clicBotones);
        reemplazar.addActionListener(clicBotones);
        setLocale(Locale.getDefault());
    }
    /**
     * Metodo que gestiona gracias a gestorIdiomas las traducciones
     * @param l nuevo Locale
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma", l);
        setTitle(bundle.getString("BUSCARYREEMPLAZAR"));
        
        if (gestorIdiomas != null) gestorIdiomas.updateLocale(l);
        this.pack(); //ajusta al tamaño minimo
    }
    
    /**
     * Crea y configura los elementos gráficos de la ventana
     */
    public void crearComponentes(){
        lamina = new JPanel();
        buscarTexto = new JLabel();
        buscarTextField = new JTextField();
        reemplazarTexto = new JLabel();
        reemplazarTextField = new JTextField();
        buscar = new JButton();
        reemplazar = new JButton();
        
        buscarTextField.setPreferredSize(new Dimension(100, 25));
        reemplazarTextField.setPreferredSize(new Dimension(100, 25));
        
        lamina.add(buscarTexto);
        lamina.add(buscarTextField);
        lamina.add(reemplazarTexto);
        lamina.add(reemplazarTextField);
        lamina.add(buscar);
        lamina.add(reemplazar);
        add(lamina);
        
        //configuracion lenguaje
        gestorIdiomas.add(buscarTexto, "BUSCAR_");
        gestorIdiomas.add(reemplazarTexto, "REEMPLAZAR_CON_");
        gestorIdiomas.add(buscar, "BUSCAR");
        gestorIdiomas.add(reemplazar, "REEMPLAZAR");
    }
    /**
     * Evento para la pulsación de los 2 botones de esta clase.
     */
    ActionListener clicBotones = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == buscar){
                bListener.buscar(buscarTextField.getText());
            }
            if (e.getSource() == reemplazar){
                bListener.reemplazar(buscarTextField.getText(), reemplazarTextField.getText());
            }
        }
    };
    
    /**
     * Listener para los eventos producidos por VentanaBuscar
     */
    public static abstract class BuscarListener {
        /**
         * Metodo que es llamado al pulsar el boton buscar.
         * @param busqueda texto que desea buscar el usuario
         * @return si se ha encontrado o no el texto
         */
        public abstract boolean buscar(String busqueda);
        /**
         * Metodo que es llamado al pulsar el boton reemplazar
         * @param busqueda texto que desea reemplazar el usuario
         * @param reempazo nuevo texto
         */
        public abstract void reemplazar(String busqueda, String reempazo);
    }
    /**
     * Cambia el listener BuscarListener
     * @param bl nuevo BuscarListener
     */
    public void setBuscarListener(BuscarListener bl){
        bListener = bl;
    }
    
    
}
