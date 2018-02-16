package surruit.leditor;

import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.ResourceBundle;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class VentanaBuscar extends JFrame{
    JLabel texto1;
    JTextField textField1;
    JLabel texto2;
    JTextField textField2;
    JPanel lamina;
    
    JButton buscar;
    JButton reemplazar;
    
    BuscarListener bListener;
    
    GestorIdiomas gestorIdiomas;

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

    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        ResourceBundle bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma", l);
        setTitle(bundle.getString("BUSCARYREEMPLAZAR"));
        
        if (gestorIdiomas != null) gestorIdiomas.updateLocale(l);
        this.pack(); //ajusta al tamaño minimo
    }
    
    
    
    public void crearComponentes(){
        lamina = new JPanel();
        texto1 = new JLabel();
        textField1 = new JTextField();
        texto2 = new JLabel();
        textField2 = new JTextField();
        buscar = new JButton();
        reemplazar = new JButton();
        
        textField1.setPreferredSize(new Dimension(100, 25));
        textField2.setPreferredSize(new Dimension(100, 25));
        
        lamina.add(texto1);
        lamina.add(textField1);
        lamina.add(texto2);
        lamina.add(textField2);
        lamina.add(buscar);
        lamina.add(reemplazar);
        add(lamina);
        
        //configuracion lenguaje
        gestorIdiomas.add(texto1, "BUSCAR_");
        gestorIdiomas.add(texto2, "REEMPLAZAR_CON_");
        gestorIdiomas.add(buscar, "BUSCAR");
        gestorIdiomas.add(reemplazar, "REEMPLAZAR");
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
