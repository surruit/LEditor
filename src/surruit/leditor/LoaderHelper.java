package surruit.leditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;
import javax.swing.text.SimpleAttributeSet;

public class LoaderHelper {
    public static boolean loadRTF(JEditorPane ep, String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ep.read(reader, null);
            reader.close();
        } catch (FileNotFoundException ex) {
            return false;
        } catch (IOException ex) {
            return false;
        }
        
        if (ep.getDocument().getLength() > 0) return true;
        else return false;
    }
    
    public static boolean loadPlane(JEditorPane ep, String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String textoTotal="";
            String linea = reader.readLine();
            while (linea != null){
                textoTotal = textoTotal + linea + System.getProperty("line.separator"); 
                linea = reader.readLine();
            } 
            ep.setText(textoTotal);
            System.out.println(textoTotal);
            reader.close();
            return true;
        } catch (Exception ex) {
            Logger.getLogger(LaminaBotones.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return false;
    }
    
    public static boolean loadPlane2(JEditorPane ep, String path){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            ep.read(reader, null);
            ep.setContentType("text/plain");
            reader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LoaderHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } catch (IOException ex) {
            Logger.getLogger(LoaderHelper.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
        if (ep.getDocument().getLength() > 0) return true;
        else return false;
    }
    
    public static boolean load(JEditorPane ep, String path){
        ep.setContentType("text/rtf");
        if (loadRTF(ep, path)) return true;
        System.out.println("Load RTF fallado");
        
        ep.setContentType("text/plain");
        if (loadPlane2(ep, path)) return true;
        System.out.println("Load plano fallado");
        
        return false;
    }
}
