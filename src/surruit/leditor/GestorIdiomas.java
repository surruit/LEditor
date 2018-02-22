package surruit.leditor;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
/**
 * Clase que gestiona la asignación de texto según el Locale actual.
 * 
 * @author Surruit
 */
public class GestorIdiomas {
    /**
     * HashMap con los botones y las claves de recurso
     */
    private Map<JComponent, String> valores;
    /**
     * ResourceBundle empleado para obtener el texto de la clave. Es
     * cambiado según el idioma en updateLocale
     * @see #updateLocale(java.util.Locale) 
     * @see java.util.ResourceBundle#getBundle(java.lang.String, java.util.Locale)
     */
    private ResourceBundle bundle;
    
    /**
     * Constructor del Gestor de idiomas
     */
    public GestorIdiomas() {
        valores = new HashMap<>();
    }
    /**
     * Añade un nuevo componente al gestor.
     * @param c componente que será añadido al gestor
     * @param resourceKey clave para ResourceBundle
     * @see #bundle
     * @see #valores
     */
    public void add(JComponent c, String resourceKey){
        valores.put(c, resourceKey);
    }
    /**
     * Asigna los textos a los componentes dentro del gestor según el Locale dado
     * @param newLocale Locale del que seran asignados los textos
     */
    public void updateLocale(Locale newLocale){
        bundle = java.util.ResourceBundle.getBundle("surruit.leditor/idioma", newLocale);
        for(JComponent c :valores.keySet()){
            if (c instanceof AbstractButton) ((AbstractButton)c).setText(bundle.getString(valores.get(c)));
            if (c instanceof JLabel) ((JLabel)c).setText(bundle.getString(valores.get(c)));
            if (c instanceof JMenu) ((JMenu)c).setText(bundle.getString(valores.get(c)));
        }
    }
    /**
     * devuelve el string asociado a la clave segun el Locale del gestor
     * @param key clave unica del recurso
     * @return String en el idiomal del Locale del gestor
     */
    public String getString(String key){return bundle.getString(key);}
}
