/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package surruit.leditor;

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Larry
 */
public class LaminaInfo extends JPanel{
    
    GestorIdiomas gestorIdiomas;
    JLabel palabras;
    JLabel caracteres;
    JLabel lineas;
    
    JEditorPane editorPane;
    
    public LaminaInfo(JEditorPane ep) {
        editorPane = ep;
        gestorIdiomas = new GestorIdiomas();
        generarComponentes();
        
        setLocale(Locale.getDefault()); //fuerza una llamada para crear las etiquetas
        editorPane.getDocument().addDocumentListener(docListener);
    }
    
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
    private JLabel genLabel(String resourceKey){
        JLabel label = new JLabel();
        gestorIdiomas.add(label, resourceKey);
        return label;
    }
    
    private void contarCaracteres(){
        String texto= "";
        try {
            texto = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        caracteres.setText(String.valueOf(texto.length()));
    }
    
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
    
    private void contarLineas(){
        String texto= "";
        try {
            texto = editorPane.getDocument().getText(0, editorPane.getDocument().getLength());
        } catch (BadLocationException ex) {
            Logger.getLogger(LaminaInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        lineas.setText(String.valueOf(texto.split("\n").length));
    }
    
    public void update(){
        contarPalabras();
        contarCaracteres();
        contarLineas();
    }

    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        gestorIdiomas.updateLocale(l);
    }
    
    public DocumentListener docListener = new DocumentListener() {
        @Override
        public void insertUpdate(DocumentEvent e) {update();}
        @Override
        public void removeUpdate(DocumentEvent e) {update();}
        @Override
        public void changedUpdate(DocumentEvent e) {update();}
    };
    
}
