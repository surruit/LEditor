package surruit.leditor;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 * Panel que muestra información del texto
 * 
 * @author Surruit
 */
public class LaminaInfo extends JPanel{
    /**
     * Gestor de idiomas de esta clase
     */
    GestorIdiomas gestorIdiomas;
    /**
     * texto "Palabras:"
     */
    JLabel palabras;
    /**
     * Texto "Caracteres:"
     */
    JLabel caracteres;
    /**
     * Texto "Lineas:"
     */
    JLabel lineas;
    
    /**
     * referencia al panel del que se mostrará información
     */
    JEditorPane editorPane;
    
    /**
     * Constructor
     * @param ep panel del que se mostrará información
     */
    public LaminaInfo(JEditorPane ep) {
        editorPane = ep;
        gestorIdiomas = new GestorIdiomas();
        generarComponentes();
        
        setLocale(Locale.getDefault()); //fuerza una llamada para crear las etiquetas
        editorPane.getDocument().addDocumentListener(docListener);
    }
    
    /**
     * Crea y configura los elementos gráficos
     */
    private void generarComponentes(){
        add(genLabel("PALABRAS")); // Palabras:
        palabras = new JLabel(); add(palabras);
        
        add(Box.createHorizontalStrut(15));
        
        add(genLabel("CHAR")); // Caracteres: 
        caracteres = new JLabel(); add(caracteres);
        
        add(Box.createHorizontalStrut(15));
        
        add(genLabel("LINEAS")); // Lineas: 
        lineas = new JLabel(); add(lineas);
    }
    /**
     * Genera un label gestionado por el gestor de idiomas
     * @param resourceKey clave del recurso de texto
     * @return nuevo label
     */
    private JLabel genLabel(String resourceKey){
        JLabel label = new JLabel();
        gestorIdiomas.add(label, resourceKey);
        return label;
    }
    
    /**
     * Metodo que cuenta y muestra los caracteres que hay en el texto
     */
    private void contarCaracteres(){
        String texto= "";
        try {
            texto = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        caracteres.setText(String.valueOf(texto.length()));
    }
    /**
     * Metodo que cuenta y muestra las palabras que hay en el texto
     */
    private void contarPalabras(){ //Referencia: https://regexr.com
        String texto= "";
        int contador= 0;
        try {
            texto = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        contador = texto.split("\\w+").length;
        
        if (texto.equals("")) contador = 0;
        
        palabras.setText(String.valueOf(contador));
    }
    /**
     * Metodo que cuenta y muestra las lineas de texto que hay en el texto
     */
    private void contarLineas(){
        String texto= "";
        try {
            texto = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lineas.setText(String.valueOf(texto.split("\n").length));
    }
    
    /**
     * Metodo al que llamar cuando hay un cambio en el texto
     */
    public void update(){
        contarPalabras();
        contarCaracteres();
        contarLineas();
    }
    
    /**
     * Metodo que gestiona gracias a gestorIdiomas las traducciones
     * @param l nuevo Locale
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        gestorIdiomas.updateLocale(l);
    }
    
    /**
     * Listener para recibir avisos de cambios en el texto y realizar la actualización de la información mostrada
     */
    public DocumentListener docListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {update();}
        @Override
        public void removeUpdate(DocumentEvent e) {update();}
        @Override
        public void changedUpdate(DocumentEvent e) {update();}
    };
    
}
