package surruit.leditor;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class Ventana extends JFrame {
    Lamina lamina;
    LaminaBotones laminaBotones;
    LaminaInfo laminaInfo;
    ResourceBundle bundle;
    
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
        laminaBotones.setToDefauld();
        
        laminaInfo = new LaminaInfo(lamina.getEditorPane());
        
        //configuracion de componentes
        
        
        //añadir componentes
        add(lamina);
        add(laminaBotones, BorderLayout.NORTH);
        add(laminaInfo, BorderLayout.SOUTH);
        
        //activando menu
        this.setJMenuBar(laminaBotones.getMenuBar());
        
    }

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
