package surruit.leditor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JEditorPane;

/**
 * Clase de apoyo para realizar funciones de carga de archivos
 * @author Surruit
 */
public class LoaderHelper {
    /**
     * Intenta cargar un archivo como RTF.
     * @param ep JEditorPane objetivo de la carga del texto
     * @param path ruta del archivo a cargar
     * @return
     *      true: Carga como RTF completa y correcta <br>
     *      false: Carga como RTF erronea, archivo no encontrado o formato distinto a RTF
     */
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
    /**
     * Intenta cargar un archivo como Texto plano.
     * @param ep JEditorPane objetivo de la carga del texto
     * @param path ruta del archivo a cargar
     * @return
     *      true: Carga como texto plano completa y correcta <br>
     *      false: Carga erronea, archivo no encontrado
     * @see #loadPlane2(javax.swing.JEditorPane, java.lang.String) 
     */
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
    /**
     * Intenta cargar un archivo como Texto plano. Modifica el formato de texto del JEditorPane a
     * texto plano si no lo es ya.
     * @param ep JEditorPane objetivo de la carga del texto
     * @param path ruta del archivo a cargar
     * @return
     *      true: Carga como texto plano completa y correcta <br>
     *      false: Carga erronea, archivo no encontrado
     * @see #loadPlane(javax.swing.JEditorPane, java.lang.String) 
     */
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
    /**
     * Intenta cargar un archivo empleando los metodos loadRTF y loadPlane2.
     * @param ep JEditorPane objetivo de la carga del texto
     * @param path ruta del archivo a cargar
     * @return
     *      true: Carga correcta <br>
     *      false: Carga erronea, archivo no encontrado.
     */
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
