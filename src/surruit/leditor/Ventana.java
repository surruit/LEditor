package surruit.leditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JFrame;

/**
 * Representa la ventana grafica principal
 * @author Surruit
 */
public class Ventana extends JFrame {
    /**
     * Instancia de Lamina de texto
     */
    Lamina lamina;
    /**
     * Instancia de la lamina que gestiona los botones
     */
    LaminaBotones laminaBotones;
    /**
     * Instancia de la lamina que muestra información del texto
     */
    LaminaInfo laminaInfo;
    /**
     * Referencia a los recursos de texto
     */
    ResourceBundle bundle;
    
    /**
     * Constructor de la ventana
     */
    public Ventana(){
        //configuracion inicial
        super(); //llamada al constructor de JFrame
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setMinimumSize(new Dimension(300, 150));
        bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma");
        setTitle(bundle.getString("APP_NOMBRE"));
        
        //creacion de componentes
        lamina = new Lamina();
        
        laminaBotones = new LaminaBotones();
        laminaBotones.setEditorPane(lamina.getEditorPane());
        
        laminaInfo = new LaminaInfo(lamina.getEditorPane());
        
        //configuracion de componentes
        
        
        //añadir componentes
        add(lamina);
        add(laminaBotones, BorderLayout.NORTH);
        add(laminaInfo, BorderLayout.SOUTH);
        
        //activando menu
        this.setJMenuBar(laminaBotones.getMenuBar());
        
    }
    /**
     * Metodo que gestiona gracias a gestorIdiomas las traducciones
     * @param l nuevo Locale
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma", l);
        setTitle(bundle.getString("APP_NOMBRE"));
        
        //en el primer arranque se llama antes a esta funcion que al constructor (supongo que en el constructor padre)
        if (laminaBotones != null) laminaBotones.setLocale(l);
        if (laminaInfo != null) laminaInfo.setLocale(l);
    }
}
